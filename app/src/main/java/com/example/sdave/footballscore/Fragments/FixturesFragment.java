package com.example.sdave.footballscore.Fragments;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.TextView;

import com.example.sdave.footballscore.Adapters.FixtureArrayAdapter;
import com.example.sdave.footballscore.AsynchronousRequestTasks.FixturesAsyncResponse;
import com.example.sdave.footballscore.AsynchronousRequestTasks.FixturesRequestTask;
import com.example.sdave.footballscore.DBAdapter;
import com.example.sdave.footballscore.Objects.Fixture;
import com.example.sdave.footballscore.Objects.League;
import com.example.sdave.footballscore.Objects.LeagueTableRow;
import com.example.sdave.footballscore.R;
import com.github.rahatarmanahmed.cpv.CircularProgressView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by sdave on 4/30/2015.
 */
public class FixturesFragment extends Fragment {

    DBAdapter myDB;
    Bundle args;
    String currentLeagueName;
    ArrayList<Fixture> fixtureArray = new ArrayList<>();
    ArrayList<LeagueTableRow> leagueTableRowArray = new ArrayList<>();
    League league;
    CircularProgressView progressView;
    Thread updateThread;
    TextView loadingView = null;
    View curView;

    public FixturesFragment() {
    }

    public static FixturesFragment newInstance(League league) {
        Bundle bundle = new Bundle();

        bundle.putSerializable("league", league);

        FixturesFragment fragment = new FixturesFragment();
        fragment.setArguments(bundle);

        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        args = getArguments();
        return inflater.inflate(R.layout.fixtures_fragment, container, false);
    }

    @Override
    public void onViewCreated(final View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        curView = view;
        league = (League) args.getSerializable("league");
        loadingView = (TextView)view.findViewById(R.id.loadingView);

        String fixturesToFetchUrl;
        progressView = (CircularProgressView)view.findViewById(R.id.progress_view);
        ExpandableListView lv = (ExpandableListView)view.findViewById(R.id.fixturesList);

        if (league == null){
            currentLeagueName = "BL1";
            fixturesToFetchUrl = "http://api.football-data.org/alpha/soccerseasons/351/fixtures";
        }
        else{
            currentLeagueName = league.getName();
            fixturesToFetchUrl = league.getFixturesUrl();
        }

        if (args != null) {

            openDB();

            //startAnimationThreadStuff(1000);
            FixturesRequestTask requestTask = new FixturesRequestTask(new FixturesAsyncResponse() {
                @Override
                public void processFinish(ArrayList<Fixture> result) {

                    if(result == null){
                        loadingView.setText("No Internet Connection!");
                        progressView.setVisibility(View.GONE);
                    }
                    else{
                        loadingView.setVisibility(View.GONE);
                        progressView.setVisibility(View.GONE);
                        populateFixturesList(result, view);
                    }

                }
            }, getActivity(), view);

            fixtureArray = getFixturesList();
            if (fixtureArray.size() == 0){
                loadingView.setText("Loading...");
                requestTask.execute(fixturesToFetchUrl, currentLeagueName);
            }
            else{
                populateFixturesList(fixtureArray, view);
                loadingView.setVisibility(View.GONE);
                progressView.setVisibility(View.GONE);
            }


        }

    }

    boolean populateFixturesList(ArrayList<Fixture> fixturesListArray, View view){

        ExpandableListView lv = (ExpandableListView)view.findViewById(R.id.fixturesList);

        FixtureArrayAdapter listAdapter;

        Map<String, ArrayList<Fixture>> fixturesCollections = new LinkedHashMap();
        ArrayList<String> dates = new ArrayList();

        if(fixturesListArray.size() == 0){
            listAdapter = new FixtureArrayAdapter(getActivity(), dates, fixturesCollections);

            lv.setAdapter(listAdapter);
            return true;
        }

        String curDate;


        int pos = -1;
        for(int i = 0; i < fixturesListArray.size(); i++){

            //curDate = fixturesListArray.get(i).getDate();
            curDate = getLocalDateString(fixturesListArray.get(i).getDate());
            curDate = curDate.trim();
            ArrayList<Fixture> curFixtureArray = fixturesCollections.get(curDate);

            if(curFixtureArray == null){
                curFixtureArray = new ArrayList();
            }


            curFixtureArray.add(fixturesListArray.get(i));
            fixturesCollections.put(curDate,curFixtureArray);

        }

        for(int j = 0; j < fixturesCollections.keySet().toArray().length; j++){

            dates.add(fixturesCollections.keySet().toArray()[j].toString());
            if(fixturesCollections.get(dates.get(j)).get(0).getStatus().equals("TIMED") && pos == -1){
                pos = j;
            }
        }

        listAdapter = new FixtureArrayAdapter(getActivity(), dates, fixturesCollections);

        lv.setAdapter(listAdapter);

        if (pos == -1){
            pos = listAdapter.getGroupCount();

            for ( int i = pos-2; i < listAdapter.getGroupCount(); i++ ) {
                lv.expandGroup(i);
            }
        }
        else{
            for ( int i = pos; i < pos+3; i++ ) {
                lv.expandGroup(i);
            }
        }

        if(pos==-1){
            pos = dates.size()-1;
        }
        lv.setSelectedGroup(pos-1);
        listAdapter.notifyDataSetChanged();

        return true;
    }

    ArrayList<Fixture> getFixturesList(){
        Cursor cursor = myDB.getFixturesByLeague(currentLeagueName);
        ArrayList<Fixture> fixturesListArray = new ArrayList<>();

        if (cursor == null){
            return fixturesListArray;
        }

        Fixture fixture;
        if(cursor.moveToFirst()){
            do{
                fixture = new Fixture();

                fixture.setStatus(cursor.getString(1));
                fixture.setMatchDay(cursor.getInt(2));
                fixture.setHomeTeamName(cursor.getString(3));
                fixture.setAwayTeamName(cursor.getString(4));
                fixture.setGoalsAwayTeam(cursor.getInt(5));
                fixture.setGoalsHomeTeam(cursor.getInt(6));
                fixture.setDate(cursor.getString(7));
                fixture.setLeague(cursor.getString(8));
                fixture.setHomeTeamNameUrl(cursor.getString(9));
                fixture.setAwayTeamNameUrl(cursor.getString(10));

                fixturesListArray.add(fixture);

            }while(cursor.moveToNext());
            cursor.close();
        }
        else{
            return fixturesListArray;
        }
        return fixturesListArray;
    }

    private void openDB() {
        myDB = new DBAdapter(getActivity());
        myDB.open();
    }

    String getLocalDateString(String date){
        Date utcFormat = null;
        try {
            utcFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'").parse(date);
        }
        catch(Exception e){

        }
        //utcFormat.setTimeZone(TimeZone.getTimeZone("UTC"));

        String mon = new SimpleDateFormat("MMM").format(utcFormat);
        String day = new SimpleDateFormat("EE").format(utcFormat);

        String dateString = day + " " + mon + " " + utcFormat.getDate() + ", " + (utcFormat.getYear()+1900);

        return dateString;
    }

    public void refresh(){

        progressView.setVisibility(View.VISIBLE);

        FixturesRequestTask requestTask = new FixturesRequestTask(new FixturesAsyncResponse() {
            @Override
            public void processFinish(ArrayList<Fixture> result) {

                if(result == null){
                    loadingView.setText("No Internet Connection!");
                    progressView.setVisibility(View.GONE);
                }
                else{
                    loadingView.setVisibility(View.GONE);
                    progressView.setVisibility(View.GONE);
                    populateFixturesList(result, curView);
                }

            }
        }, getActivity(), curView);

        if (league == null){
            requestTask.execute("http://api.football-data.org/alpha/soccerseasons/351/fixtures", "BL1");
        }
        else{
            requestTask.execute(league.getFixturesUrl(), league.getName());
        }

    }

}
