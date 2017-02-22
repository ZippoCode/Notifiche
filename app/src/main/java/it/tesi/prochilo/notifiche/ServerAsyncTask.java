package it.tesi.prochilo.notifiche;

import android.os.AsyncTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.LinkedList;
import java.util.List;

public class ServerAsyncTask implements ServerMethod {

    private CustomServerManagement mCustomServerManagement;
    private CustomFMS customFMS = new CustomFMS();
    private String token;
    private List<Topic> list_topic = new LinkedList<>();

    public ServerAsyncTask(String url) {
        mCustomServerManagement = new CustomServerManagement(url);
    }

    @Override
    public boolean addTopic(List<String> topic, String token) {
        PostAsyncTask task = new PostAsyncTask();
        this.token = token;
        task.execute(topic);
        return true;
    }

    @Override
    public List<Topic> getTopic(String token) {
        GetAsyncTask task = new GetAsyncTask();
        this.token = token;
        task.execute(token);
        return list_topic;
    }

    @Override
    public boolean deleteTopic(List<String> topic, String token) {
        DeleteAsyncTask task = new DeleteAsyncTask();
        this.token = token;
        task.execute(topic);
        return false;
    }

    private class PostAsyncTask extends AsyncTask<List<String>, Void, Void> {
        private boolean rispostaGet;

        @Override
        protected Void doInBackground(List<String>... lists) {
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
            rispostaGet = mCustomServerManagement.postAndDeleteRequest(array, token, CustomServerManagement.HttpMethod.POST);
            return null;
        }
    }

    private class GetAsyncTask extends AsyncTask<String, Void, List<Topic>> {
        @Override
        protected List<Topic> doInBackground(String... strings) {
            ServerAsyncTask.this.list_topic = mCustomServerManagement.getTopics(strings[0]);
            return null;
        }

        @Override
        protected void onPostExecute(List<Topic> topics) {
            super.onPostExecute(topics);
        }
    }

    private class DeleteAsyncTask extends AsyncTask<List<String>, Void, Void> {
        private boolean rispostaDelete;

        @Override
        protected Void doInBackground(List<String>... lists) {
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
            rispostaDelete = mCustomServerManagement.postAndDeleteRequest(array, token, CustomServerManagement.HttpMethod.DELETE);
            return null;
        }
    }
}

