package it.tesi.prochilo.notifiche.server;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.LinkedList;
import java.util.List;

import it.tesi.prochilo.notifiche.util.IOUtil;
import it.tesi.prochilo.notifiche.ServerListener;
import it.tesi.prochilo.notifiche.Topic;

public class CustomServerManagement {

    private enum HttpMethod {
        GET, POST, DELETE
    }

    /**
     * Sono i campi che compongono le richiesta JSONObject
     */
    private enum FieldJSONObject {
        id, userId, topic, timestamp
    }

    private static final String TAG = CustomServerManagement.class.getSimpleName();
    private static final String AUTHORIZATION = "Authorization";
    private static final String CONTENT_TYPE = "Content-Type";
    private String mUrlString = null;
    private final int connectionTimeout = 5000;
    private final String mToken;


    public CustomServerManagement(String urlString, final String token) {
        this.mUrlString = urlString;
        this.mToken = token;
    }


    /**
     * Iscrive i topic descritti nella lista sul server
     *
     * @param topicsList     La lista di topic
     * @param serverListener Il listener per notificare se l'operazione è avvenuta con successo o meno
     * @return True se l'operazione è andata a buon fine, altrimenti false
     */
    public boolean postRequest(List<String> topicsList, ServerListener serverListener) {
        return postAndDeleteRequest(topicsList, HttpMethod.POST, serverListener);
    }

    /**
     * Ritorna la lista di Topic a cui è sottoscritto il token sul Server Custom
     *
     * @param serverListener Il listener per notificare se l'operazione è avvenuta con successo o meno
     * @return La lista dei Topic
     */
    public List<Topic> getRequest(ServerListener serverListener) {
        URL url;
        HttpURLConnection httpURLConnection = null;
        List<Topic> topicList = null;
        try {
            url = new URL(mUrlString);
            httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setConnectTimeout(connectionTimeout);
            httpURLConnection.setRequestMethod(HttpMethod.GET.name());
            httpURLConnection.addRequestProperty("Content-Type", "application/json");
            httpURLConnection.addRequestProperty(AUTHORIZATION, "Bearer " + mToken);
            InputStream inputStream;
            if (httpURLConnection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                inputStream = httpURLConnection.getInputStream();
                topicList = new LinkedList<>();
                JSONArray response = new JSONArray(IOUtil.getString(inputStream));
                for (int i = 0; i < response.length(); i++) {
                    JSONObject object = response.getJSONObject(i);
                    Topic topic = Topic.Builder
                            .create(object.getString(FieldJSONObject.id.name())
                                    , object.getString(FieldJSONObject.userId.name()))
                            .addTopic(object.getString(FieldJSONObject.topic.name()))
                            .addTimestamp(object.getString(FieldJSONObject.timestamp.name()))
                            .build();
                    topicList.add(topic);
                }
                Log.d(TAG, "Richiesta " + HttpMethod.GET.name() + " eseguita");
                serverListener.onSuccess();
            }
        } catch (IOException | JSONException json) {
            Log.d(TAG, "Richiesta " + HttpMethod.GET.name() + " fallita");
            serverListener.onFailure();
            return null;
        } finally {
            if (httpURLConnection != null)
                httpURLConnection.disconnect();
        }
        return topicList;
    }

    /**
     * Elimina i topic descritti nella lista sul server
     *
     * @param topicsList     La lista di topic
     * @param serverListener Il listener per notificare se l'operazione è avvenuta con successo o meno
     * @return True se l'operazione è andata a buon fine, altrimenti false
     */

    public boolean deleteRequest(List<String> topicsList, ServerListener serverListener) {
        return postAndDeleteRequest(topicsList, HttpMethod.DELETE, serverListener);
    }


    /**
     * Esegue una richiesta HTTP al server di tipo POST o DELETE e ritorna
     * true se l'operazione è andata a buon fine, altrimenti false
     *
     * @param topicsList     La lista di topic
     * @param method         Il tipo di HTTP
     * @param serverListener Il listener per notificare se l'operazione è avvenuta con successo o meno
     * @return True se l'operazione è andata a buon fine, altrimenti false
     */
    private boolean postAndDeleteRequest(List<String> topicsList, HttpMethod method, ServerListener serverListener) {
        URL url;
        HttpURLConnection httpURLConnection = null;
        try {
            url = new URL(mUrlString);
            httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setConnectTimeout(connectionTimeout);
            httpURLConnection.setRequestMethod(method.name());
            httpURLConnection.addRequestProperty(CONTENT_TYPE, "application/json");
            httpURLConnection.addRequestProperty(AUTHORIZATION, "Bearer " + mToken);
            httpURLConnection.setDoOutput(true);
            OutputStream outputStream = httpURLConnection.getOutputStream();
            JSONArray jsonArray = new JSONArray();
            for (int i = 0; i < topicsList.size(); i++) {
                JSONObject topic = new JSONObject();
                topic.put("topic", topicsList.get(i));
                jsonArray.put(topic);
            }
            outputStream.write(jsonArray.toString().getBytes("UTF-8"));
            if (httpURLConnection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                Log.d(TAG, "Richiesta " + method.name() + " eseguita");
                serverListener.onSuccess();
                return true;
            } else {
                Log.d(TAG, "Richiesta " + method.name() + " fallita");
                serverListener.onFailure();
                return false;
            }
        } catch (JSONException | IOException e) {
            serverListener.onFailure();
            return false;
        } finally {
            if (httpURLConnection != null)
                httpURLConnection.disconnect();
        }
    }

}
