package com.example.sdave.footballscore.AsynchronousRequestTasks;

import android.content.Context;
import android.os.AsyncTask;
import android.view.View;

import com.example.sdave.footballscore.DBAdapter;
import com.example.sdave.footballscore.Objects.LeagueTableRow;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.UnknownHostException;
import java.util.ArrayList;

/**
 * Created by Sagar on 4/26/2015.
 */

public class LeagueTableRequestTask extends AsyncTask<String, String, JSONArray> {
    public LeagueTableAsyncResponse delegate = null;
    DBAdapter myDB;
    String currentLeagueName;
    Context context;
    View view;

    String apiKey = "41205ee945bb4cffa494ff653019fdb3";

    public LeagueTableRequestTask(LeagueTableAsyncResponse asyncResponse, Context context, View view) {
        this.context = context;
        this.delegate = asyncResponse;
        this.view = view;
    }

    @Override
    protected JSONArray doInBackground(String... uri) {
        HttpClient httpclient = new DefaultHttpClient();
        HttpResponse response;
        String responseString = null;
        try {
            currentLeagueName = uri[1];
            HttpGet request = new HttpGet(uri[0]);
            request.addHeader("X-Auth-Token", apiKey);
            response = httpclient.execute(request);
            StatusLine statusLine = response.getStatusLine();
            if (statusLine.getStatusCode() == HttpStatus.SC_OK) {
                ByteArrayOutputStream out = new ByteArrayOutputStream();
                response.getEntity().writeTo(out);
                responseString = out.toString();
                out.close();
            } else {
                //Closes the connection.
                response.getEntity().getContent().close();
                throw new IOException(statusLine.getReasonPhrase());
            }
        } catch (ClientProtocolException e) {

            responseString = "d";
            //TODO Handle problems..
        } catch (IOException e) {
            //TODO Handle problems..
            if (e instanceof UnknownHostException) {
                return null;
            }
        }



        JSONArray jsonArray = new JSONArray();

        if(responseString == null){
            return jsonArray;
        }
        try {
            JSONObject jsonObject = new JSONObject(responseString);
            jsonArray = new JSONArray(jsonObject.getString("standing"));
        } catch (JSONException e) {
            return jsonArray;
        }


        return jsonArray;
    }


    @Override
    protected void onPostExecute(JSONArray jsonArray) {
        super.onPostExecute(jsonArray);

        myDB = new DBAdapter(context);
        myDB.open();

        ArrayList<LeagueTableRow> leagueTableRows = saveLeagueTableToDB(jsonArray);

        myDB.close();

        delegate.processFinish(leagueTableRows);

    }

    private ArrayList<LeagueTableRow> saveLeagueTableToDB(JSONArray jsonArray) {
        ArrayList<LeagueTableRow> leagueTableRows = new ArrayList<>();
        LeagueTableRow leagueTableRow;
        for(int i = 0; i < jsonArray.length(); i++){
            try {
                JSONObject jsonLeagueTableObj = jsonArray.getJSONObject(i);
                String teamUrl = myDB.getTeamUrlByName(jsonLeagueTableObj.getString("teamName"));
                leagueTableRow = new LeagueTableRow();

                leagueTableRow.setTeamName(jsonLeagueTableObj.getString("teamName"));
                leagueTableRow.setPosition(jsonLeagueTableObj.getInt("position"));
                leagueTableRow.setPlayedGames(jsonLeagueTableObj.getInt("playedGames"));
                leagueTableRow.setPoints(jsonLeagueTableObj.getInt("points"));
                leagueTableRow.setGoals(jsonLeagueTableObj.getInt("goals"));
                leagueTableRow.setGoalsAgainst(jsonLeagueTableObj.getInt("goalsAgainst"));
                leagueTableRow.setGoalDifference(jsonLeagueTableObj.getInt("goalDifference"));
                leagueTableRow.setLeagueName(currentLeagueName);
                leagueTableRow.setTeamUrl(teamUrl);

                int id = leagueTableExists(leagueTableRow.getTeamName());
                if(id != -1){
                    myDB.updateLeagueTableRow(leagueTableRow, String.valueOf(id));
                }
                else{
                    myDB.AddLeagueTableRow(leagueTableRow);
                }

                leagueTableRows.add(leagueTableRow);
            } catch (JSONException e) {
                //result = "Failed!";
                e.printStackTrace();
            }
            //adapter.notifyDataSetChanged();
        }
        //myDB.AddLeagueTableRow(leagueTableRows);

        return leagueTableRows;
    }

    int leagueTableExists(String teamName){
        return myDB.getLeagueTableRowId(teamName);
    }

}
