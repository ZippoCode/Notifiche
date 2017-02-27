package it.tesi.prochilo.notifiche;

import android.os.AsyncTask;

import com.google.firebase.iid.FirebaseInstanceId;

import org.json.JSONException;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class ServerAsyncTask implements ServerInterface {

    private CustomServerManagement mCustomServerManagement;
    private CustomFMS customFMS = new CustomFMS();
    private String token;
    private ServerListener mServerListener;

    public ServerAsyncTask(String url) {
        mCustomServerManagement = new CustomServerManagement(url);
    }

    @Override
    public void setOnServerListener(ServerListener serverListener) {
        this.mServerListener = serverListener;
    }

    @Override
    public boolean subscribeToTopics(List<String> topicsList, String token) {
        PostAsyncTask task = new PostAsyncTask();
        this.token = token;
        boolean response = false;
        task.execute(topicsList);
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
    public List<Topic> getTopics(String token) {
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
    public boolean unsubscribeFromTopics(List<String> topicsList, String token) {
        DeleteAsyncTask task = new DeleteAsyncTask();
        this.token = token;
        boolean response = false;
        task.execute(topicsList);
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
            boolean flag = false;
            try {
                customFMS.postTopics(lists[0], null);
                flag = mCustomServerManagement.postTopics(lists[0], token);
            } catch (IOException ioe) {
                mServerListener.failure();
            } finally {
                mServerListener.success();
            }
            return flag;
        }

    }

    private class GetAsyncTask extends AsyncTask<String, Void, List<Topic>> {
        @Override
        protected List<Topic> doInBackground(String... strings) {
            List<Topic> topics = null;
            try {
                topics = mCustomServerManagement.getTopics(token);
            } catch (IOException ioe) {
                mServerListener.failure();
            } finally {
                mServerListener.success();
            }
            return topics;
        }
    }

    private class DeleteAsyncTask extends AsyncTask<List<String>, Void, Boolean> {

        @Override
        protected Boolean doInBackground(List<String>... lists) {
            boolean flag = false;
            try {
                customFMS.deleteTopics(lists[0], null);
                flag = mCustomServerManagement.deleteTopics(lists[0], token);
            } catch (IOException ioe) {
                mServerListener.failure();
            } finally {
                mServerListener.success();
            }
            return flag;
        }

    }
}

