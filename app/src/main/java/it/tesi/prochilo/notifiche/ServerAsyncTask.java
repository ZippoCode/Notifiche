package it.tesi.prochilo.notifiche;

import android.os.AsyncTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class ServerAsyncTask implements ServerMethod {

    private CustomServerManagement mCustomServerManagement;
    private CustomFMS customFMS = new CustomFMS();
    private String token;

    public ServerAsyncTask(String url) {
        mCustomServerManagement = new CustomServerManagement(url);
    }

    @Override
    public boolean addTopic(List<String> topic, String token) {
        PostAsyncTask task = new PostAsyncTask();
        this.token = token;
        boolean response = false;
        task.execute(topic);
        try {
            response = task.get();
        } catch (ExecutionException ee) {
            ee.printStackTrace();
        } catch (InterruptedException ie) {
            ie.printStackTrace();
        }
        return response;
    }

    @Override
    public List<Topic> getTopic(String token) {
        GetAsyncTask task = new GetAsyncTask();
        this.token = token;
        task.execute(token);
        List<Topic> response = null;
        try {
            response = task.get();
        } catch (ExecutionException ee) {
            ee.printStackTrace();
        } catch (InterruptedException ie) {
            ie.printStackTrace();
        }
        return response;
    }

    @Override
    public boolean deleteTopic(List<String> topic, String token) {
        DeleteAsyncTask task = new DeleteAsyncTask();
        this.token = token;
        boolean response = false;
        task.execute(topic);
        try {
            response = task.get();
        } catch (ExecutionException ee) {
            ee.printStackTrace();
        } catch (InterruptedException ie) {
            ie.printStackTrace();
        }
        return response;
    }

    private class PostAsyncTask extends AsyncTask<List<String>, Void, Boolean> {

        @Override
        protected Boolean doInBackground(List<String>... lists) {
            JSONArray array = new JSONArray();
            List<String> strings = lists[0];
            customFMS.subscribeToTopic(strings);
            for (int i = 0; i < strings.size(); i++) {
                JSONObject topic = new JSONObject();
                try {
                    topic.put("topic", strings.get(i));
                    array.put(topic);
                } catch (JSONException json) {
                    json.printStackTrace();
                }
            }
            customFMS.subscribeToTopic(strings);
            return mCustomServerManagement.postAndDeleteRequest(array, token, CustomServerManagement.HttpMethod.POST);
        }

    }

    private class GetAsyncTask extends AsyncTask<String, Void, List<Topic>> {
        @Override
        protected List<Topic> doInBackground(String... strings) {
            return mCustomServerManagement.getTopics(token);
        }
    }

    private class DeleteAsyncTask extends AsyncTask<List<String>, Void, Boolean> {

        @Override
        protected Boolean doInBackground(List<String>... lists) {
            JSONArray array = new JSONArray();
            List<String> strings = lists[0];
            for (int i = 0; i < strings.size(); i++) {
                JSONObject topic = new JSONObject();
                try {
                    topic.put("topic", strings.get(i));
                    array.put(topic);
                } catch (JSONException json) {
                    json.printStackTrace();
                }
            }
            customFMS.unsubscribeFromTopic(strings);
            return mCustomServerManagement.postAndDeleteRequest(array, token, CustomServerManagement.HttpMethod.DELETE);
        }
    }
}

