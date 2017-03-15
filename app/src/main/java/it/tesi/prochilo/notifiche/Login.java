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
public class Login implements ServerInterface {

    private ServerRestMethod mServerRestMethod;

    /**
     * @param account  L'indirizzo del Server Custom
     * @param password L'identificativo dell'utente
     */
    public Login(String account, String password) {
    }

    /**
     * Setta il server
     *
     * @param server
     */
    @Override
    public void setServerType(ServerRestMethod server) {
        mServerRestMethod = server;
        mServerRestMethod.setOnServerListener(new ServerListener() {
            @Override
            public void onSuccess() {
                Log.d("Operazione: ", "Riuscita");
            }

            @Override
            public void onFailure() {
                Log.d("Operazione: ", "Fallita");
            }
        });
    }

    /**
     * Inoltra le richieste di sottoscrizione ai topic ai due server
     *
     * @param topicsList La lista dei topic
     * @return Ritorna true se l'operazione è andata a buon fine
     */
    @Override
    public boolean subscribeToTopics(List<String> topicsList) {
        if(mServerRestMethod == null){
            throw new IllegalStateException("Server non settato");
        }
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
        if(mServerRestMethod == null){
            throw new IllegalStateException("Server non settato");
        }
        GetAsyncTask task = new GetAsyncTask();
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
     * Inoltre la richiesta di sottoscrizione ai Topic a due server
     *
     * @param topicsList La lista di topic
     * @return True se l'operazione è andata a buon fine, false altrimenti
     */
    @Override
    public boolean unsubscribeFromTopics(List<String> topicsList) {
        if(mServerRestMethod == null){
            throw new IllegalStateException("Server non settato");
        }
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
            return mServerRestMethod.postTopics(lists[0]);
        }

    }

    private class GetAsyncTask extends AsyncTask<Void, Void, List<Topic>> {
        @Override
        protected List<Topic> doInBackground(Void... voids) {
            return mServerRestMethod.getTopics();
        }
    }

    private class DeleteAsyncTask extends AsyncTask<List<String>, Void, Boolean> {

        @Override
        protected Boolean doInBackground(List<String>... lists) {
            return mServerRestMethod.deleteTopics(lists[0]);
        }

    }
}

