package it.tesi.prochilo.notifiche;

import android.os.AsyncTask;

import com.google.firebase.iid.FirebaseInstanceId;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * Invoca in background le richieste presso il server FCM e quello Custom.
 * In particolare espone tre metodi che permettono di sottoscriversi ad una lista di topic, disiscriversi
 * e, fornito un identificato, ritorna la lista dei topic a cui l'utente è inscritto
 *
 * @author Salvatore Prochilo
 * @version 1.0
 */
public class ServerAsyncTask implements ServerInterface {

    private CustomServerManagement mCustomServerManagement;
    private CustomFMS mCustomFMS;
    private String mToken;
    private ServerListener mServerListener;

    /**
     * @param url   L'indirizzo del Server Custom
     * @param token L'identificativo dell'utente
     */
    public ServerAsyncTask(String url, String token) {
        mCustomServerManagement = new CustomServerManagement(url);
        mCustomFMS = new CustomFMS("");
        this.mToken = token;
    }

    /**
     * Setta il serverListener
     *
     * @param serverListener
     */
    @Override
    public void setOnServerListener(ServerListener serverListener) {
        this.mServerListener = serverListener;
    }

    /**
     * Inoltra le richieste di sottoscrizione ai topic ai due server
     *
     * @param topicsList La lista dei topic
     * @return Ritorna true se l'operazione è andata a buon fine
     */
    @Override
    public boolean subscribeToTopics(List<String> topicsList) {
        PostAsyncTask task = new PostAsyncTask();
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

    /**
     * Inoltra la richiesta di GET ai due server
     *
     * @return La lista dei topic
     */
    @Override
    public List<Topic> getTopics() {
        GetAsyncTask task = new GetAsyncTask();
        task.execute(mToken);
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

    /**
     * Inoltre la richiesta di sottoscrizione ai Topic a due server
     *
     * @param topicsList La lista di topic
     * @return True se l'operazione è andata a buon fine, false altrimenti
     */
    @Override
    public boolean unsubscribeFromTopics(List<String> topicsList) {
        DeleteAsyncTask task = new DeleteAsyncTask();
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
                mCustomFMS.postTopics(lists[0], null);
                flag = mCustomServerManagement.postTopics(lists[0], mToken);
            } catch (IOException ioe) {
                mServerListener.onFailure();
            } finally {
                mServerListener.onSuccess();
            }
            return flag;
        }

    }

    private class GetAsyncTask extends AsyncTask<String, Void, List<Topic>> {
        @Override
        protected List<Topic> doInBackground(String... strings) {
            List<Topic> topics = null;
            try {
                topics = mCustomServerManagement.getTopics(mToken);
                System.out.println(mCustomFMS.getTopics(FirebaseInstanceId.getInstance().getToken()));
            } catch (IOException ioe) {
                ioe.printStackTrace();
                mServerListener.onFailure();
            } finally {
                mServerListener.onSuccess();
            }
            return topics;
        }
    }

    private class DeleteAsyncTask extends AsyncTask<List<String>, Void, Boolean> {

        @Override
        protected Boolean doInBackground(List<String>... lists) {
            boolean flag = false;
            try {
                mCustomFMS.deleteTopics(lists[0], null);
                flag = mCustomServerManagement.deleteTopics(lists[0], mToken);
            } catch (IOException ioe) {
                mServerListener.onFailure();
            } finally {
                mServerListener.onSuccess();
            }
            return flag;
        }

    }
}

