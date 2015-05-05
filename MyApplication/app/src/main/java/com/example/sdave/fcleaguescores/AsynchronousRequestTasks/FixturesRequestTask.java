package com.example.sdave.fcleaguescores.AsynchronousRequestTasks;

import android.content.Context;
import android.database.Cursor;
import android.os.AsyncTask;
import android.view.View;

import com.example.sdave.fcleaguescores.DBAdapter;
import com.example.sdave.fcleaguescores.Objects.Fixture;

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
 * Created by sdave on 4/14/2015.
 */
public class FixturesRequestTask extends AsyncTask<String, String, ArrayList<Fixture>> {
    public FixturesAsyncResponse delegate=null;
    DBAdapter myDB;
    String currentLeagueName;
    Context context;
    View view;

    String apiKey = "41205ee945bb4cffa494ff653019fdb3";
    public FixturesRequestTask(FixturesAsyncResponse asyncResponse, Context context, View view) {
        this.context = context;
        this.delegate = asyncResponse;
        this.view = view;
    }

    @Override
    protected ArrayList<Fixture> doInBackground(String... uri) {
        HttpClient httpclient = new DefaultHttpClient();
        HttpResponse response;
        String responseString = null;
        try {
            currentLeagueName = uri[1];
            HttpGet request = new HttpGet(uri[0]);
            request.addHeader("X-Auth-Token", apiKey);
            response = httpclient.execute(request);
            StatusLine statusLine = response.getStatusLine();
            if(statusLine.getStatusCode() == HttpStatus.SC_OK){
                ByteArrayOutputStream out = new ByteArrayOutputStream();
                response.getEntity().writeTo(out);
                responseString = out.toString();
                out.close();
            } else{
                //Closes the connection.
                response.getEntity().getContent().close();
                throw new IOException(statusLine.getReasonPhrase());
            }
        } catch (ClientProtocolException e) {

            responseString = "d";
            //TODO Handle problems..
        } catch (IOException e) {
            //TODO Handle problems..
            if(e instanceof UnknownHostException){
                return null;
            }
        }



        myDB = new DBAdapter(context);
        myDB.open();
        JSONArray jsonArray = null;
        ArrayList<Fixture> fixtureArray;

        try{
            JSONObject jsonObject = new JSONObject(responseString);
            jsonArray = new JSONArray(jsonObject.getString("fixtures"));
        }
        catch(JSONException e){

        }
        fixtureArray = saveFixturesToDB(jsonArray);

        myDB.close();

        return fixtureArray;
    }

    @Override
    protected void onPostExecute(ArrayList<Fixture> fixtureArray) {
        super.onPostExecute(fixtureArray);



        //ListView lv = (ListView)view.findViewById(R.id.list);

        //FixtureArrayAdapter listAdapter;
        //listAdapter = new FixtureArrayAdapter(context);

        //listAdapter.addAll(fixtureArray);
        //lv.setAdapter(listAdapter);

        delegate.processFinish(fixtureArray);

    }

    ArrayList<Fixture> saveFixturesToDB(JSONArray jsonArray){
        Fixture fixture;
        ArrayList<Fixture> fixtureArray = new ArrayList<>();
        for(int i = 0; i < jsonArray.length(); i++){
            try {
                JSONObject jsonFixureObj = jsonArray.getJSONObject(i);

                JSONObject jsonFixtureResultObj = (JSONObject) jsonFixureObj.get("result");
                String homeTeamImageLink = "";
                String awayTeamImageLink = "";

                Cursor cur1 = myDB.getTeamByName(jsonFixureObj.getString("awayTeamName"));
                try{
                    if(cur1.moveToFirst()){
                        awayTeamImageLink = cur1.getString(7);
                    }
                    cur1.close();

                    Cursor cur2 = myDB.getTeamByName(jsonFixureObj.getString("homeTeamName"));

                    if(cur2.moveToFirst()){
                        homeTeamImageLink = cur2.getString(7);
                    }
                    cur2.close();
                }
                catch(Exception e){

                }

                fixture = new Fixture();

                fixture.setGoalsAwayTeam(jsonFixtureResultObj.getInt("goalsAwayTeam"));
                fixture.setGoalsHomeTeam(jsonFixtureResultObj.getInt("goalsHomeTeam"));
                fixture.setAwayTeamName(jsonFixureObj.getString("awayTeamName"));
                fixture.setHomeTeamName(jsonFixureObj.getString("homeTeamName"));
                fixture.setDate(jsonFixureObj.getString("date"));
                fixture.setMatchDay(jsonFixureObj.getInt("matchday"));
                fixture.setStatus(jsonFixureObj.getString("status"));
                fixture.setLeague(currentLeagueName);
                fixture.setHomeTeamNameUrl(homeTeamImageLink);
                fixture.setAwayTeamNameUrl(awayTeamImageLink);
                int id = fixtureExists(fixture);
                if(id != -1){
                    myDB.updateFixture(fixture, String.valueOf(id));
                }
                else{
                    myDB.AddFixture(fixture);
                }

                fixtureArray.add(fixture);
            } catch (JSONException e) {
                //result = "Failed!";
                e.printStackTrace();
            }
            //adapter.notifyDataSetChanged();
        }
        return fixtureArray;
    }

    int fixtureExists(Fixture fixture){
        return myDB.getFixturesIdByDateAndTeamNames(fixture.getDate(), fixture.getHomeTeamName(), fixture.getAwayTeamName());
    }
}
