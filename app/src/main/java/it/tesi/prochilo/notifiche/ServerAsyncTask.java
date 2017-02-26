package it.tesi.prochilo.notifiche;

import android.os.AsyncTask;

import com.google.firebase.iid.FirebaseInstanceId;

import java.util.List;
import java.util.concurrent.ExecutionException;

public class ServerAsyncTask {

    private CustomServerManagement mCustomServerManagement;
    private CustomFMS customFMS = new CustomFMS();
    private String token;
    private ServerListener mServerListener;

    public ServerAsyncTask(String url, ServerListener mServerListener) {
        mCustomServerManagement = new CustomServerManagement(url);
        this.mServerListener = mServerListener;
    }

    public boolean addTopics(List<String> topic, String token) {
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

    public boolean deleteTopics(List<String> topic, String token) {
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
            boolean flag = false;
            customFMS.subscribeToTopics(lists[0], null, mServerListener);
            flag = mCustomServerManagement.subscribeToTopics(lists[0], token, mServerListener);
            if (flag == true) {
                mServerListener.success();
            } else {
                mServerListener.failure();
            }
            return flag;
        }

    }

    private class GetAsyncTask extends AsyncTask<String, Void, List<Topic>> {
        @Override
        protected List<Topic> doInBackground(String... strings) {
            List<Topic> topics = mCustomServerManagement.getTopics(token, mServerListener);
            System.out.println(customFMS.getTopics(FirebaseInstanceId.getInstance().getToken(), mServerListener));
            return topics;
        }
    }

    private class DeleteAsyncTask extends AsyncTask<List<String>, Void, Boolean> {

        @Override
        protected Boolean doInBackground(List<String>... lists) {
            boolean flag = false;
            customFMS.unsubscribeFromTopics(lists[0], null, mServerListener);
            flag = mCustomServerManagement.unsubscribeFromTopics(lists[0], token, mServerListener);
            return flag;
        }
    }
}

