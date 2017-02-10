package it.tesi.prochilo.notifiche;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

public class ServerManagement {

    public enum HttpMethod {
        GET, POST, PUT, DELETE;
    }

    private String url;
    private HttpURLConnection mHttpURLConnection;
    private URL mUrl = null;

    public ServerManagement(String url) {
        this.url = url;
        try {
            mUrl = new URL(url);
        } catch (IOException ioe) {

        }
    }

    public List<String> getTopic() throws IOException {
        try {
            mHttpURLConnection = (HttpURLConnection) mUrl.openConnection();
            mHttpURLConnection.setRequestMethod(HttpMethod.GET.name());
            //DA MODIFICARE
            mHttpURLConnection.addRequestProperty("content_type", "application.json");
            final int httpRespondeCode = mHttpURLConnection.getResponseCode();
            InputStream inputStream;
            if (true) { //VERIFICA IL NUMERO
                inputStream = mHttpURLConnection.getInputStream();
            } else {
                inputStream = mHttpURLConnection.getErrorStream();
            }
            final String httpResponseMessage = mHttpURLConnection.getResponseMessage();
            //Leggi il contenuto tramite l'inputStream
        } catch (IOException ioe) {

        } finally {
            mHttpURLConnection.disconnect();
        }
        //ritorna la lista dei topic
        return null;
    }

    public void addTopic(String topic) {
        //DA IMPLEMENTARE CON PUT
    }

    public void addMoreTopic(String listaTopic) {
        try {
            mHttpURLConnection = (HttpURLConnection) mUrl.openConnection();
            mHttpURLConnection.setRequestMethod(HttpMethod.POST.name());
            mHttpURLConnection.setDoInput(true);
            mHttpURLConnection.setDoOutput(true);
            final OutputStream outputStream = mHttpURLConnection.getOutputStream();
            outputStream.write(listaTopic.getBytes("UTF-8"));
            //DA MODIFICARE
            mHttpURLConnection.addRequestProperty("content_type", "json");
            final int httpResponse = mHttpURLConnection.getResponseCode();
            //CONTROLLO RISPOSTA
            InputStream inputStream = mHttpURLConnection.getInputStream();

        } catch (IOException ioe) {

        } finally {
            mHttpURLConnection.disconnect();
        }

    }

    public void deleteTopic() {
        //DA IMPLEMENTARE CON DELETE
    }
}
