package it.tesi.prochilo.notifiche;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.LinkedList;
import java.util.List;

public class CustomServerManagement {

    public enum HttpMethod {
        GET, POST, PUT, DELETE;
    }

    public enum FieldJSONObject {
        id, userId, topic, timestamp;
    }

    private static final String TAG = CustomServerManagement.class.getSimpleName();
    private static final String AUTHORIZATION = "Authorization";
    private String mUrlString = null;

    public CustomServerManagement(String urlString) {
        this.mUrlString = urlString;
    }

    public List<Topic> getTopics(String token) {
        List<Topic> topicList = new LinkedList<>();
        HttpURLConnection httpURLConnection = null;
        URL url = null;
        String httpResponseMessage = null;
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
            } else {
                inputStream = httpURLConnection.getErrorStream();
            }
            JSONArray response = new JSONArray(getString(inputStream));
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

    public boolean postAndDeleteRequest(JSONArray jsonArray, String token, HttpMethod method) {
        HttpURLConnection httpURLConnection = null;
        URL url;
        String httpResponseMessage = null;
        try {
            url = new URL(mUrlString);
            httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod(method.name());
            httpURLConnection.addRequestProperty("Content-Type", "application/json");
            httpURLConnection.addRequestProperty(AUTHORIZATION, "Bearer " + token);
            httpURLConnection.setDoOutput(true);
            OutputStream outputStream = httpURLConnection.getOutputStream();
            outputStream.write(jsonArray.toString().getBytes("UTF-8"));
            outputStream.flush();
            httpResponseMessage = httpURLConnection.getResponseMessage();
            Log.d(TAG, "Response from Server: " + httpResponseMessage);
        } catch (IOException ioe) {
            Log.d(TAG, "Error open connection");
            ioe.printStackTrace();
        } finally {
            if (httpURLConnection != null) {
                httpURLConnection.disconnect();
            }
        }
        return httpResponseMessage.equals(HttpURLConnection.HTTP_OK);
    }

    private String getString(InputStream inputStream) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = br.readLine()) != null) {
            sb.append(line).append('\n');
        }
        return sb.toString();
    }

}
