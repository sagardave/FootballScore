package com.example.sdave.fcleaguescores.AsynchronousRequestTasks;

import android.os.AsyncTask;
import android.os.Environment;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;

/**
 * Created by sdave on 4/21/2015.
 */
public class ImageRequestTask extends AsyncTask<String, String, String> {
    public ImageSaveAsyncResponse delegate=null;

    public ImageRequestTask(ImageSaveAsyncResponse asyncResponse) {
        this.delegate = asyncResponse;
    }

    @Override
    protected String doInBackground(String... uri) {

        URL urlSave = null;
        InputStream input = null;
        File storagePath = null;
        String fileExtension = uri[0].substring(uri[0].length()-4,uri[0].length());

        try{
            urlSave = new URL(uri[0]);
            input = urlSave.openStream();
        }
        catch (Exception e) {

        }

        try {
            //The sdcard directory e.g. '/sdcard' can be used directly, or
            //more safely abstracted with getExternalStorageDirectory()
            if (new File(Environment.getExternalStorageDirectory().toString() + "\\FCLeaguesUnitedImages").mkdir()){
                storagePath = new File(Environment.getExternalStorageDirectory().toString() + "\\FCLeaguesUnitedImages");
            }
            else{
                storagePath = Environment.getExternalStorageDirectory();
            }
            OutputStream output = new FileOutputStream(new File(storagePath, uri[1].replace(' ', '_') + fileExtension));
            try {
                byte[] buffer = new byte[1024];
                int bytesRead = 0;
                while ((bytesRead = input.read(buffer, 0, buffer.length)) >= 0) {
                    output.write(buffer, 0, bytesRead);
                }

            } finally {
                output.close();
            }
        } catch (Exception e) {
            int t = 0;
        } finally {
            try{
                input.close();
            }
            catch (Exception e) {

            }
        }
        return storagePath.getPath() + "/" + uri[1].replace(' ', '_') + fileExtension;

    }

    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);

        delegate.processFinish(result);
    }

}
