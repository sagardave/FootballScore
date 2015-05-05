package com.example.sdave.fcleaguescores;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.sdave.fcleaguescores.Objects.League;
import com.example.sdave.fcleaguescores.SlidingTabs.SlidingTabsColorsFragment;
import com.example.sdave.fcleaguescores.AsynchronousRequestTasks.AsyncResponse;
import com.example.sdave.fcleaguescores.AsynchronousRequestTasks.RequestTask;
import com.example.sdave.fcleaguescores.Adapters.LeaguesListAdapter;
import com.example.sdave.myapplication.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by sdave on 4/22/2015.
 */
public class SlidingListListener extends ListFragment {

    DBAdapter myDB;
    ArrayList<League> leaguesList = new ArrayList<>();
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.list, container, false);

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        openDB();

        RequestTask requestTask = new RequestTask(new AsyncResponse() {
            @Override
            public void processFinish(String result) {
                JSONArray jsonArray = null;
                try {
                    jsonArray = new JSONArray(result);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                saveLeagueToDB(jsonArray);
                leaguesList = myDB.getAllLeagues();
                populateleaguesList(leaguesList);
                //loadingView.setVisibility(View.GONE);
            }
        });

        leaguesList = myDB.getAllLeagues();

        if (leaguesList.size() == 0){
            //loadingView.setText("Loading...");
            requestTask.execute("http://api.football-data.org/alpha/soccerseasons");

        }
        else{
            //loadingView.setVisibility(View.GONE);
            populateleaguesList(leaguesList);
        }
        //setListAdapter(new LeaguesListAdapter(getActivity(), R.layout.item_leagues, leaguesList));

    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        League league = leaguesList.get(position);

        SlidingTabsColorsFragment newContent = new SlidingTabsColorsFragment();

        if (newContent != null)
            switchFragment(newContent, league);
    }

    // the meat of switching the above fragment
    private void switchFragment(Fragment fragment, League league) {
        if (getActivity() == null)
            return;

        Bundle bundle = new Bundle();
        bundle.putSerializable("league", league);
        fragment.setArguments(bundle);

        if (getActivity() instanceof MainActivity) {
            MainActivity ra = (MainActivity) getActivity();
            ra.switchContent(fragment, league.getCaption());
        }
    }

    private void openDB() {
        myDB = new DBAdapter(getActivity());
        myDB.open();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        myDB.close();

    }

    void saveLeagueToDB(JSONArray jsonArray){

        ArrayList<League> leaguesArray = new ArrayList<>();
        if(jsonArray != null) {
            for (int i = 0; i < jsonArray.length(); i++) {
                try {
                    JSONObject jsonLeagueObj = jsonArray.getJSONObject(i);
                    JSONObject jsonLeagueLinksObj = (JSONObject) jsonLeagueObj.get("_links");
                    JSONObject jsonLeagueTeamsObj = (JSONObject) jsonLeagueLinksObj.get("teams");
                    JSONObject jsonLeagueFixturesObj = (JSONObject) jsonLeagueLinksObj.get("fixtures");
                    JSONObject jsonLeagueTableObj = (JSONObject) jsonLeagueLinksObj.get("leagueTable");

                    League league = new League();

                    league.setName(jsonLeagueObj.getString("league"));
                    league.setFixturesUrl(jsonLeagueFixturesObj.getString("href"));
                    league.setCaption(jsonLeagueObj.getString("caption"));
                    league.setLastUpdated(jsonLeagueObj.getString("lastUpdated"));
                    league.setNumOfGame(jsonLeagueObj.getInt("numberOfGames"));
                    league.setNumOfTeams(jsonLeagueObj.getInt("numberOfTeams"));
                    league.setTeamsUrl(jsonLeagueTeamsObj.getString("href"));
                    league.setYear(jsonLeagueObj.getInt("year"));
                    league.setLeagueTableUrl(jsonLeagueTableObj.getString("href"));

                    leaguesArray.add(league);
                } catch (JSONException e) {
                    //result = "Failed!";
                    e.printStackTrace();
                }
                //adapter.notifyDataSetChanged();
            }
            myDB.AddLeague(leaguesArray);
        }

    }

    void populateleaguesList(ArrayList<League> leaguesList){
        LeaguesListAdapter adapter = new LeaguesListAdapter(getActivity());
        adapter.addAll(leaguesList);

        setListAdapter(adapter);
    }

}
