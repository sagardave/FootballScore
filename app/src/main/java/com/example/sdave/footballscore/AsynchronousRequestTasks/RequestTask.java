package com.example.sdave.footballscore.AsynchronousRequestTasks;

import android.os.AsyncTask;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.UnknownHostException;

/**
 * Created by sdave on 4/14/2015.
 */
public class RequestTask extends AsyncTask<String, String, String> {
    public AsyncResponse delegate=null;

    String apiKey = "41205ee945bb4cffa494ff653019fdb3";
    public RequestTask(AsyncResponse asyncResponse) {
        this.delegate = asyncResponse;
    }

    @Override
    protected String doInBackground(String... uri) {
        HttpClient httpclient = new DefaultHttpClient();
        HttpResponse response;
        String responseString = null;
        try {

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
                responseString = "Unknown Host";
            }
        }
        return responseString;
    }

    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);
        delegate.processFinish(result);

    }

}
