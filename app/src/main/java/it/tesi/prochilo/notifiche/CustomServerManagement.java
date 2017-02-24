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

public class CustomServerManagement {

    public enum HttpMethod {
        GET, POST, DELETE;
    }

    public enum FieldJSONObject {
        id, userId, topic, timestamp;
    }

    private static final String TAG = CustomServerManagement.class.getSimpleName();
    private static final String AUTHORIZATION = "Authorization";
    private String mUrlString = null;
    private ServerListener mServerListener;

    public CustomServerManagement(String urlString) {
        this.mUrlString = urlString;
    }

    public List<Topic> getTopics(String token, ServerListener serverListener) {
        List<Topic> topicList = new LinkedList<>();
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
            if (httpResponseCode >= HttpURLConnection.HTTP_OK
                    && httpResponseCode < HttpURLConnection.HTTP_MULT_CHOICE) {
                inputStream = httpURLConnection.getInputStream();
                serverListener.success();
            } else {
                inputStream = httpURLConnection.getErrorStream();
                serverListener.failure();
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
            ioe.printStackTrace();
        } catch (JSONException json) {
            Log.d(TAG, "Error JSON");
            json.printStackTrace();
        } finally {
            if (httpURLConnection != null) {
                httpURLConnection.disconnect();
            }
        }
        return topicList;
    }

    public boolean postAndDeleteRequest(List<Topic> topicsList, String token, HttpMethod method, ServerListener serverListener) {
        String httpResponseMessage = null;
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
                topic.put("topic", topicsList.get(i).topic);
                jsonArray.put(topic);
            }
            outputStream.write(jsonArray.toString().getBytes("UTF-8"));
            outputStream.flush();
            int httpResponseCode = httpURLConnection.getResponseCode();
            if (httpResponseCode == HttpURLConnection.HTTP_OK) {
                serverListener.success();
            }
            httpResponseMessage = httpURLConnection.getResponseMessage();
            Log.d(TAG, "Response from Server: " + httpResponseMessage);
        } catch (IOException ioe) {
            Log.d(TAG, "Error open connection");
            ioe.printStackTrace();
        } catch (JSONException json) {
            Log.d(TAG, "Error JSON");
            json.printStackTrace();
        } finally {
            if (httpURLConnection != null) {
                httpURLConnection.disconnect();
            }
        }
        return httpResponseMessage.equals("OK");
    }

}
