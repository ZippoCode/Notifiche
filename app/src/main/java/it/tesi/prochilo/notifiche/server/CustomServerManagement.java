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

import it.tesi.prochilo.notifiche.IOUtil;
import it.tesi.prochilo.notifiche.ServerListener;
import it.tesi.prochilo.notifiche.ServerRestMethod;
import it.tesi.prochilo.notifiche.Topic;

public class CustomServerManagement implements ServerRestMethod {

    public enum HttpMethod {
        GET, POST, DELETE;
    }

    /**
     * Sono i campi che compongono le richiesta JSONObject
     */
    public enum FieldJSONObject {
        id, userId, topic, timestamp;
    }

    private static final String TAG = CustomServerManagement.class.getSimpleName();
    private static final String AUTHORIZATION = "Authorization";
    private String mUrlString = null;
    private final int connectionTimeout = 5000;
    private final String mToken;
    private ServerListener mServerListener;

    public CustomServerManagement(String urlString, final String token) {
        this.mUrlString = urlString;
        this.mToken = token;
    }

    @Override
    public void setOnServerListener(ServerListener serverListener) {
        this.mServerListener = serverListener;
    }

    /**
     * Invia e sottoscrive il token ad una lista di topic sul Server Custom
     *
     * @param topicsList
     * @return True se la richiesta ha avuto successo, altrimenti ritorna false
     */
    @Override
    public boolean postTopics(List<String> topicsList) {
        return postAndDeleteRequest(topicsList, HttpMethod.POST);
    }

    /**
     * Ritorna la lista di Topic a cui è sottoscritto il token sul Server Custom
     *
     * @return La lista dei Topic
     */
    @Override
    public List<Topic> getTopics() {
        List<Topic> topicList = new LinkedList<>();
        String httpResponseMessage = null;
        URL url = null;
        HttpURLConnection httpURLConnection = null;
        boolean flag = true;
        try {
            url = new URL(mUrlString);
            httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setConnectTimeout(connectionTimeout);
            httpURLConnection.setRequestMethod(HttpMethod.GET.name());
            httpURLConnection.addRequestProperty("Content-Type", "application/json");
            httpURLConnection.addRequestProperty(AUTHORIZATION, "Bearer " + mToken);
            if (httpURLConnection != null) {
                InputStream inputStream = null;
                try {
                    if (httpURLConnection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                        inputStream = httpURLConnection.getInputStream();
                        topicList = new LinkedList<>();
                    }
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
                } catch (JSONException json) {
                    Topic topic = Topic.Builder.create("null", "null")
                            .addTopic("null")
                            .addTimestamp("null")
                            .build();
                    topicList.add(topic);
                    flag = false;
                    mServerListener.onFailure();
                } finally {
                    httpURLConnection.disconnect();
                }
            }
        } catch (IOException ioe) {
            flag = false;
            mServerListener.onFailure();
        }
        if (flag)
            mServerListener.onSuccess();
        return topicList;
    }

    /**
     * Elimina i topic a cui è sottoscritto il token
     *
     * @param topic
     * @return
     */
    @Override
    public boolean deleteTopics(List<String> topic) {
        return postAndDeleteRequest(topic, HttpMethod.DELETE);
    }


    /**
     * @param topicsList
     * @param method
     * @return
     */

    private boolean postAndDeleteRequest(List<String> topicsList, HttpMethod method) {
        int httpResponseCode = -1;
        URL url = null;
        HttpURLConnection httpURLConnection = null;
        boolean flag = true;
        try {
            url = new URL(mUrlString);
            httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setConnectTimeout(connectionTimeout);
            httpURLConnection.setRequestMethod(method.name());
            httpURLConnection.addRequestProperty("Content-Type", "application/json");
            httpURLConnection.addRequestProperty(AUTHORIZATION, "Bearer " + mToken);
            httpURLConnection.setDoOutput(true);
            if (httpURLConnection != null) {
                OutputStream outputStream = httpURLConnection.getOutputStream();
                JSONArray jsonArray = new JSONArray();
                for (int i = 0; i < topicsList.size(); i++) {
                    JSONObject topic = new JSONObject();
                    topic.put("topic", topicsList.get(i));
                    jsonArray.put(topic);
                }
                outputStream.write(jsonArray.toString().getBytes("UTF-8"));
                outputStream.flush();
                httpResponseCode = httpURLConnection.getResponseCode();
                String httpResponseMessage = httpURLConnection.getResponseMessage();
                Log.d(TAG, "Response from Server: " + httpResponseMessage);
            }
        } catch (JSONException json) {
            flag = false;
            mServerListener.onFailure();
        } catch (IOException ioe) {
            flag = false;
            mServerListener.onFailure();
        } finally {
            if (httpURLConnection != null) {
                httpURLConnection.disconnect();
            }
        }
        if (flag)
            mServerListener.onSuccess();
        return httpResponseCode == HttpURLConnection.HTTP_OK;
    }

}
