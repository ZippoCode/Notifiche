package it.tesi.prochilo.notifiche;

import android.os.AsyncTask;
import android.util.Log;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import it.tesi.prochilo.notifiche.server.CustomFMS;
import it.tesi.prochilo.notifiche.server.CustomServerManagement;

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
    private ServerListener mServerListener;

    public ServerAsyncTask(String url, String token) {
        mCustomServerManagement = new CustomServerManagement(url, token);
    }


    /**
     * Inoltra le richieste di sottoscrizione ai topic ai due server
     *
     * @param topicsList La lista dei topic
     * @return Ritorna true se l'operazione è andata a buon fine
     */
    @Override
    public boolean subscribeToTopics(List<String> topicsList, ServerListener serverListener) {
        PostAsyncTask task = new PostAsyncTask();
        this.mServerListener = serverListener;
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
    public List<Topic> getTopics(ServerListener serverListener) {
        GetAsyncTask task = new GetAsyncTask();
        this.mServerListener = serverListener;
        task.execute();
        List<Topic> response = new LinkedList<>();
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
     * Inoltre la richiesta di sottoscrizione
     *
     * @param topicsList La lista di topic
     * @return True se l'operazione è andata a buon fine, false altrimenti
     */
    @Override
    public boolean unsubscribeFromTopics(List<String> topicsList, ServerListener serverListener) {
        DeleteAsyncTask task = new DeleteAsyncTask();
        this.mServerListener = serverListener;
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
            return mCustomServerManagement.postTopics(lists[0], mServerListener);
        }

    }

    private class GetAsyncTask extends AsyncTask<Void, Void, List<Topic>> {
        @Override
        protected List<Topic> doInBackground(Void... voids) {
            return mCustomServerManagement.getTopics(mServerListener);
        }

        @Override
        protected void onPostExecute(List<Topic> topics) {
            super.onPostExecute(topics);
        }
    }

    private class DeleteAsyncTask extends AsyncTask<List<String>, Void, Boolean> {

        @Override
        protected Boolean doInBackground(List<String>... lists) {
            return mCustomServerManagement.deleteTopics(lists[0], mServerListener);
        }

    }
}

