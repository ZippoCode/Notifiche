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
     * @return True se la richiesta ha avuto successo, altrimenti ritorna false
     */
    @Override
    public boolean postTopics(List<String> topicsList, String token) throws IOException {
        return postAndDeleteRequest(topicsList, token, HttpMethod.POST);
    }

    /**
     * Ritorna la lista di Topic a cui è sottoscritto il token sul Server Custom
     *
     * @param token
     * @return La lista dei Topic
     */
    @Override
    public List<Topic> getTopics(String token) throws IOException {
        List<Topic> topicList = null;
        String httpResponseMessage = null;
        URL url = null;
        HttpURLConnection httpURLConnection = null;
        url = new URL(mUrlString);
        httpURLConnection = (HttpURLConnection) url.openConnection();
        httpURLConnection.setRequestMethod(HttpMethod.GET.name());
        httpURLConnection.addRequestProperty("Content-Type", "application/json");
        httpURLConnection.addRequestProperty(AUTHORIZATION, "Bearer " + token);
        InputStream inputStream = null;
        try {
            int httpResponseCode = httpURLConnection.getResponseCode();
            if (httpResponseCode == HttpURLConnection.HTTP_OK) {
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
            topicList = new LinkedList<>();
            topicList.add(topic);
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
    public boolean deleteTopics(List<String> topic, String token) throws IOException {
        return postAndDeleteRequest(topic, token, HttpMethod.DELETE);
    }


    /**
     * @param topicsList
     * @param token
     * @param method
     * @return
     */

    private boolean postAndDeleteRequest(List<String> topicsList, String token, HttpMethod method) throws IOException {
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
        } catch (JSONException json) {

        } finally {
            if (httpURLConnection != null) {
                httpURLConnection.disconnect();
            }
        }
        return httpResponseCode == HttpURLConnection.HTTP_OK;
    }

}
