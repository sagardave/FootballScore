package com.example.sdave.fcleaguescores.Fragments;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.example.sdave.fcleaguescores.Adapters.LeagueTableAdapter;
import com.example.sdave.fcleaguescores.AsynchronousRequestTasks.LeagueTableAsyncResponse;
import com.example.sdave.fcleaguescores.DBAdapter;
import com.example.sdave.fcleaguescores.Objects.League;
import com.example.sdave.fcleaguescores.Objects.LeagueTableRow;
import com.example.sdave.fcleaguescores.AsynchronousRequestTasks.LeagueTableRequestTask;
import com.example.sdave.myapplication.R;
import com.github.rahatarmanahmed.cpv.CircularProgressView;

import java.util.ArrayList;

/**
 * Created by sdave on 4/30/2015.
 */
public class LeagueTableFragment extends Fragment {

    DBAdapter myDB;
    Bundle args;
    String currentLeagueName;
    ArrayList<LeagueTableRow> leagueTableRowArray = new ArrayList<>();
    League league;
    CircularProgressView progressView;
    TextView loadingView = null;
    View curView;

    public LeagueTableFragment() {

    }

    public static LeagueTableFragment newInstance(League league) {
        Bundle bundle = new Bundle();

        bundle.putSerializable("league", league);

        LeagueTableFragment fragment = new LeagueTableFragment();
        fragment.setArguments(bundle);

        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        args = getArguments();
        return inflater.inflate(R.layout.league_table_fragment, container, false);
    }

    @Override
    public void onViewCreated(final View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        curView = view;
        league = (League) args.getSerializable("league");
        loadingView = (TextView) view.findViewById(R.id.loadingView);

        String leagueTableToFetchUrl;
        progressView = (CircularProgressView) view.findViewById(R.id.progress_view);

        if (league == null) {
            currentLeagueName = "BL1";
            leagueTableToFetchUrl = "http://api.football-data.org/alpha/soccerseasons/351/leagueTable";
        } else {
            currentLeagueName = league.getName();
            leagueTableToFetchUrl = league.getLeagueTableUrl();
        }

        if (args != null) {

            openDB();
            leagueTableRowArray = getLeagueTableRows();

            LeagueTableRequestTask requestTask = new LeagueTableRequestTask(new LeagueTableAsyncResponse() {
                @Override
                public void processFinish(ArrayList<LeagueTableRow> result) {
                    leagueTableRowArray = result;
                    if (result.size() == 0) {
                        loadingView.setText("No Internet Connection!");
                        progressView.setVisibility(View.GONE);
                    } else {
                        populateLeagueTableList(leagueTableRowArray, view);
                        loadingView.setVisibility(View.GONE);
                        progressView.setVisibility(View.GONE);
                    }

                }
            }, getActivity(), view);

            if (leagueTableRowArray.size() == 0) {
                loadingView.setText("Loading...");
                requestTask.execute(leagueTableToFetchUrl, currentLeagueName);
            } else {
                populateLeagueTableList(leagueTableRowArray, view);
                loadingView.setVisibility(View.GONE);
                progressView.setVisibility(View.GONE);
            }

        }

    }

    ArrayList<LeagueTableRow> getLeagueTableRows(){
        ArrayList<LeagueTableRow> leagueTableRows = new ArrayList<>();
        Cursor cursor = null;
        try{
            cursor = myDB.getLeagueTableRowByLeague(currentLeagueName);
        }
        catch (Exception e){
            return leagueTableRows;
        }

        if(cursor == null){
            return leagueTableRows;
        }

        LeagueTableRow leagueTableRow;
        if(cursor.moveToFirst()){
            //process data
            do{
                leagueTableRow = new LeagueTableRow();

                leagueTableRow.setTeamName(cursor.getString(1));
                leagueTableRow.setPosition(cursor.getInt(2));
                leagueTableRow.setPlayedGames(cursor.getInt(3));
                leagueTableRow.setPoints(cursor.getInt(4));
                leagueTableRow.setGoals(cursor.getInt(5));
                leagueTableRow.setGoalsAgainst(cursor.getInt(6));
                leagueTableRow.setGoalDifference(cursor.getInt(7));
                leagueTableRow.setLeagueName(cursor.getString(8));
                String teamUrl = myDB.getTeamUrlByName(cursor.getString(1));
                leagueTableRow.setTeamUrl(teamUrl);

                leagueTableRows.add(leagueTableRow);

            }while(cursor.moveToNext());

        }
        return leagueTableRows;
    }

    void populateLeagueTableList(ArrayList<LeagueTableRow> tableListArray, View view){

        ListView lv = (ListView)view.findViewById(R.id.list);

        LeagueTableAdapter listAdapter;
        listAdapter = new LeagueTableAdapter(getActivity());

        listAdapter.addAll(tableListArray);
        lv.setAdapter(listAdapter);

        listAdapter.notifyDataSetChanged();

    }


    private void openDB() {
        myDB = new DBAdapter(getActivity());
        myDB.open();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(myDB != null){
            myDB.close();
        }

    }

    public void refresh(){

        progressView.setVisibility(View.VISIBLE);

        LeagueTableRequestTask stadingRequestTask = new LeagueTableRequestTask(new LeagueTableAsyncResponse() {
            @Override
            public void processFinish(ArrayList<LeagueTableRow> result) {
                leagueTableRowArray = result;
                if(result.size() == 0){
                    loadingView.setText("No Internet Connection!");
                    progressView.setVisibility(View.GONE);
                }
                else{
                    populateLeagueTableList(leagueTableRowArray, curView);
                    loadingView.setVisibility(View.GONE);
                    progressView.setVisibility(View.GONE);
                }

            }
        }, getActivity(), curView);

        if (league == null){
            stadingRequestTask.execute("http://api.football-data.org/alpha/soccerseasons/351/leaguetable", "BL1");
        }
        else{
            stadingRequestTask.execute(league.getLeagueTableUrl(), league.getName());
        }

    }

}