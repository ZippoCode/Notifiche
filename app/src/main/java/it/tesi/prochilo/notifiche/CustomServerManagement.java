package it.tesi.prochilo.notifiche;

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

public class CustomServerManagement implements ServerMethod {

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
    private static final int timeOut = 10000;
    private String mUrlString = null;

    public CustomServerManagement(String urlString) {
        this.mUrlString = urlString;
    }

    /**
     * Invia e sottoscrive il token ad una lista di topic sul Server Custom
     *
     * @param topicsList
     * @param token
     * @param serverListener
     * @return True se la richiesta ha avuto successo, altrimenti ritorna false
     */
    @Override
    public boolean subscribeToTopics(List<String> topicsList, String token, ServerListener serverListener) {
        return postAndDeleteRequest(topicsList, token, HttpMethod.POST, serverListener);
    }

    /**
     * Ritorna la lista di Topic a cui è sottoscritto il token sul Server Custom
     *
     * @param token
     * @param serverListener
     * @return La lista dei Topic
     */
    @Override
    public List<Topic> getTopics(String token, ServerListener serverListener) {
        List<Topic> topicList = null;
        String httpResponseMessage = null;
        URL url = null;
        HttpURLConnection httpURLConnection = null;
        try {
            url = new URL(mUrlString);
            httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod(HttpMethod.GET.name());
            httpURLConnection.addRequestProperty("Content-Type", "application/json");
            httpURLConnection.addRequestProperty(AUTHORIZATION, "Bearer " + token);
            InputStream inputStream = null;
            int httpResponseCode = httpURLConnection.getResponseCode();
            if (httpResponseCode == HttpURLConnection.HTTP_OK) {
                inputStream = httpURLConnection.getInputStream();
                topicList = new LinkedList<>();
                serverListener.success();
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
            httpResponseMessage = httpURLConnection.getResponseMessage();
            Log.d(TAG, "Result from server :" + httpResponseMessage);
        } catch (IOException ioe) {
            Log.d(TAG, "Error open connection");
            serverListener.failure();
            ioe.printStackTrace();
        } catch (JSONException json) {
            Log.d(TAG, "Error JSON");
            serverListener.failure();
            json.printStackTrace();
        } finally {
            if (httpURLConnection != null) {
                httpURLConnection.disconnect();
            }
        }
        return topicList;
    }

    /**
     * Elimina i topic a cui è sottoscritto il token
     *
     * @param topic
     * @param token
     * @return
     */
    @Override
    public boolean unsubscribeFromTopics(List<String> topic, String token, ServerListener serverListener) {
        return postAndDeleteRequest(topic, token, HttpMethod.DELETE, serverListener);
    }


    /**
     * @param topicsList
     * @param token
     * @param method
     * @param serverListener
     * @return
     */
    private boolean postAndDeleteRequest(List<String> topicsList, String token, HttpMethod method, ServerListener serverListener) {
        int httpResponseCode = -1;
        URL url = null;
        HttpURLConnection httpURLConnection = null;
        try {
            url = new URL(mUrlString);
            httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod(method.name());
            httpURLConnection.addRequestProperty("Content-Type", "application/json");
            httpURLConnection.addRequestProperty(AUTHORIZATION, "Bearer " + token);
            httpURLConnection.setDoOutput(true);
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
        } catch (IOException ioe) {
            Log.d(TAG, "Error open connection");
            serverListener.failure();
            ioe.printStackTrace();
        } catch (JSONException json) {
            Log.d(TAG, "Error JSON");
            serverListener.failure();
            json.printStackTrace();
        } finally {
            if (httpURLConnection != null) {
                httpURLConnection.disconnect();
            }
        }
        if (httpResponseCode == HttpURLConnection.HTTP_OK)
            serverListener.success();
        return httpResponseCode == HttpURLConnection.HTTP_OK;
    }

}
