package com.example.sdave.footballscore.Fragments;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sdave.footballscore.Adapters.TeamArrayAdapter;
import com.example.sdave.footballscore.DBAdapter;
import com.example.sdave.footballscore.Objects.League;
import com.example.sdave.footballscore.AsynchronousRequestTasks.AsyncResponse;
import com.example.sdave.footballscore.AsynchronousRequestTasks.RequestTask;
import com.example.sdave.footballscore.Objects.Team;
import com.example.sdave.footballscore.R;
import com.github.rahatarmanahmed.cpv.CircularProgressView;
import com.melnykov.fab.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by sdave on 4/30/2015.
 */
public class TeamsFragment extends Fragment {

    DBAdapter myDB;
    Bundle args;
    String currentLeagueName;
    League league;
    CircularProgressView progressView;
    TextView loadingView = null;
    View curView;

    public TeamsFragment() {
    }

    public static TeamsFragment newInstance(League league) {
        Bundle bundle = new Bundle();

        bundle.putSerializable("league", league);

        TeamsFragment fragment = new TeamsFragment();
        fragment.setArguments(bundle);

        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        args = getArguments();
        return inflater.inflate(R.layout.team_list_fragment, container, false);
    }

    @Override
    public void onViewCreated(final View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        curView = view;
        league = (League) args.getSerializable("league");
        loadingView = (TextView)view.findViewById(R.id.loadingView);

        String teamToFetchUrl;
        progressView = (CircularProgressView)view.findViewById(R.id.progress_view);

        if (league == null){
            currentLeagueName = "BL1";
            teamToFetchUrl = "http://api.football-data.org/alpha/soccerseasons/351/teams";
        }
        else{
            currentLeagueName = league.getName();
            teamToFetchUrl = league.getTeamsUrl();
        }

        if (args != null) {

            openDB();
            //startAnimationThreadStuff(1000);
            RequestTask requestTask = new RequestTask(new AsyncResponse() {
                @Override
                public void processFinish(String result) {
                    JSONArray jsonArray;
                    try {
                        if(result == "Unknown Host"){
                            loadingView.setText("No Internet Connection!");
                        }
                        else{
                            JSONObject jsonObject = new JSONObject(result);
                            jsonArray = new JSONArray(jsonObject.getString("teams"));
                            saveToDB(jsonArray);
                            loadingView.setVisibility(View.GONE);
                            progressView.setVisibility(View.GONE);
                            populateTeamList(view);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });

            if (populateTeamList(view) == false){
                loadingView.setText("Loading...");
                requestTask.execute(teamToFetchUrl);
            }
            else{
                loadingView.setVisibility(View.GONE);
                progressView.setVisibility(View.GONE);
            }
        }
    }

    boolean populateTeamList(View view){
        Cursor cursor = myDB.getTeamsByLeague(currentLeagueName);
        //Cursor cursor = myDB.getAllTeams();

        if (cursor == null){
            return false;
        }

        ListView lv = (ListView)view.findViewById(R.id.list);
        ArrayList<Team> teamListArray = new ArrayList<>();

        FloatingActionButton fab = (FloatingActionButton) view.findViewById(R.id.fab);
        fab.attachToListView(lv);
        View.OnClickListener cl = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), "Button Clicked!", Toast.LENGTH_LONG).show();
            }
        };

        fab.setOnClickListener(cl);

        Team team;
        if(cursor.moveToFirst()){
            //process data
            do{
                team = new Team();

                team.setName(cursor.getString(1));
                team.setLeague(cursor.getString(2));
                team.setMarketValue(cursor.getString(3));
                team.setShortName(cursor.getString(4));
                team.setPlayersURL(cursor.getString(5));
                team.setCode(cursor.getString(6));
                team.setImageUrl(cursor.getString(7));

                teamListArray.add(team);
                //teamname = teamname + "\n" + cursor.getString(R.integer.team_name_col);

            }while(cursor.moveToNext());

        }
        else{
            return false;
        }
        TeamArrayAdapter listAdapter;
        listAdapter = new TeamArrayAdapter(getActivity());

        listAdapter.addAll(teamListArray);
        lv.setAdapter(listAdapter);
        return true;
    }

    void saveToDB(JSONArray jsonArray){
        Team team;
        ArrayList<Team> teamArray = new ArrayList<>();
        for(int i = 0; i < jsonArray.length(); i++){
            try {
                JSONObject jsonTeamObj = jsonArray.getJSONObject(i);
                JSONObject jsonTeamLinksObj = (JSONObject) jsonTeamObj.get("_links");


                team = new Team();

                team.setName(jsonTeamObj.getString("name"));
                team.setShortName(jsonTeamObj.getString("shortName"));
                team.setImageUrl(jsonTeamObj.getString("crestUrl"));
                team.setPlayersURL(jsonTeamLinksObj.getString("players"));
                team.setMarketValue(jsonTeamObj.getString("squadMarketValue"));
                team.setLeague(currentLeagueName);
                team.setCode(jsonTeamObj.getString("code"));

                teamArray.add(team);
            } catch (JSONException e) {
                //result = "Failed!";
                e.printStackTrace();
            }
            //adapter.notifyDataSetChanged();
        }
        myDB.AddTeam(teamArray);

    }

    private void openDB() {
        myDB = new DBAdapter(getActivity());
        myDB.open();
    }
}
