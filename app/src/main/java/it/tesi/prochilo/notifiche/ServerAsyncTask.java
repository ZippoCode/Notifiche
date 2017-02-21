package it.tesi.prochilo.notifiche;

import android.os.AsyncTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public class ServerAsyncTask extends AsyncTask<String, String, String> {

    private ServerManagement mServerManagement;

    public ServerAsyncTask(ServerManagement serverManagement) {
        this.mServerManagement = serverManagement;
    }

    @Override
    protected String doInBackground(String... strings) {
        /*
        try {
            JSONArray jsonArray = new JSONArray();
            jsonArray.put((new JSONObject()).put("topic", "first"));
            jsonArray.put((new JSONObject()).put("topic", "second"));
            mServerManagement.postAndDeleteRequest(jsonArray, "token_admin", ServerManagement.HttpMethod.DELETE);
        } catch (JSONException jsone) {
            jsone.printStackTrace();
        }
        */
        //mServerManagement.addUser("prova", "prova");
        return null;

    }
}
