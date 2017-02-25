package it.tesi.prochilo.notifiche;

import android.os.AsyncTask;

import java.util.List;
import java.util.concurrent.ExecutionException;

public class ServerAsyncTask implements ServerMethod {

    private CustomServerManagement mCustomServerManagement;
    private CustomFMS customFMS = new CustomFMS();
    private String token;
    private ServerListener mServerListener;

    public ServerAsyncTask(String url) {
        mCustomServerManagement = new CustomServerManagement(url);
    }

    @Override
    public boolean addTopics(List<Topic> topic, String token, ServerListener serverListener) {
        PostAsyncTask task = new PostAsyncTask();
        this.token = token;
        boolean response = false;
        this.mServerListener = serverListener;
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
    public List<Topic> getTopics(String token, ServerListener serverListener) {
        GetAsyncTask task = new GetAsyncTask();
        this.token = token;
        this.mServerListener = serverListener;
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
    public boolean deleteTopics(List<Topic> topic, String token, ServerListener serverListener) {
        DeleteAsyncTask task = new DeleteAsyncTask();
        this.token = token;
        this.mServerListener = serverListener;
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

    private class PostAsyncTask extends AsyncTask<List<Topic>, Void, Boolean> {

        @Override
        protected Boolean doInBackground(List<Topic>... lists) {
            boolean flag = false;
            customFMS.subscribeToTopic(lists[0]);
            flag = mCustomServerManagement.postAndDeleteRequest(lists[0], token, CustomServerManagement.HttpMethod.POST);
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
            List<Topic> topics = mCustomServerManagement.getTopics(token);
            if (topics != null) {
                mServerListener.success();
            } else {
                mServerListener.failure();
            }
            return topics;
        }
    }

    private class DeleteAsyncTask extends AsyncTask<List<Topic>, Void, Boolean> {

        @Override
        protected Boolean doInBackground(List<Topic>... lists) {
            boolean flag = false;
            customFMS.unsubscribeFromTopic(lists[0]);
            flag = mCustomServerManagement.postAndDeleteRequest(lists[0], token, CustomServerManagement.HttpMethod.DELETE);
            if (flag == true) {
                mServerListener.success();
            } else {
                mServerListener.failure();
            }
            return flag;
        }
    }
}

