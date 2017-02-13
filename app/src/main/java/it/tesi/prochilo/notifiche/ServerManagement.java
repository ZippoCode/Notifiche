package it.tesi.prochilo.notifiche;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.LinkedList;
import java.util.List;

public class ServerManagement {

    public enum HttpMethod {
        GET, POST, PUT, DELETE;
    }

    private String url = null;
    private HttpURLConnection mHttpURLConnection;
    private URL mUrl = null;
    private InputStream mInputStream;
    private OutputStream mOutputStream;
    private final static String TAG = ServerManagement.class.getSimpleName();
    private final String CONTENT_TYPE = "Content-type";

    public ServerManagement(String url) {
        this.url = url;
        try {
            mUrl = new URL(url);
        } catch (IOException ioe) {

        }
    }

    public List<String> getTopics(JSONManagement jsonMangament) throws IOException {
        List<String> topics = null;
        try {
            mHttpURLConnection = (HttpURLConnection) mUrl.openConnection();
            mHttpURLConnection.setRequestMethod(HttpMethod.GET.name());
            mHttpURLConnection.addRequestProperty(CONTENT_TYPE, "application/json");
            final int httpRespondeCode = mHttpURLConnection.getResponseCode();
            if (httpRespondeCode >= HttpURLConnection.HTTP_OK
                    && httpRespondeCode > HttpURLConnection.HTTP_MULT_CHOICE) {
                mInputStream = mHttpURLConnection.getInputStream();
            } else {
                mInputStream = mHttpURLConnection.getErrorStream();
            }
            final String httpResponseMessage = mHttpURLConnection.getResponseMessage();
            Log.d(TAG, "Result from server: " + httpResponseMessage);
            //topics = jsonMangament.getTopics(mInputStream);
        } catch (IOException ioe) {
            Log.e(TAG, "Errore lettura");
        } finally {
            mHttpURLConnection.disconnect();
            mInputStream.close();
        }
        return topics;
    }

    public void addTopic(String topic) {
        //DA IMPLEMENTARE CON PUT
    }

    public void addTopics(List<String> topics, JSONManagement jsonmanagement) {
        try {
            mHttpURLConnection = (HttpURLConnection) mUrl.openConnection();
            mHttpURLConnection.setRequestMethod(HttpMethod.POST.name());
            mHttpURLConnection.setDoInput(true);
            mHttpURLConnection.setDoOutput(true);
            mOutputStream = mHttpURLConnection.getOutputStream();
            JSONObject richiesta = jsonmanagement.createPostRequest("topics", topics);
            mHttpURLConnection.addRequestProperty(CONTENT_TYPE ,"json");
            final int httpResponse = mHttpURLConnection.getResponseCode();
            //CONTROLLO RISPOSTA

        } catch (IOException ioe) {

        } finally {
            mHttpURLConnection.disconnect();
        }

    }

    public void deleteTopic() {
        //DA IMPLEMENTARE CON DELETE
    }
}
