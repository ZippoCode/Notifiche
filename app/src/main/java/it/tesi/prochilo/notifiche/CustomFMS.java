package it.tesi.prochilo.notifiche;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.util.JsonReader;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingService;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

public class CustomFMS extends FirebaseMessagingService {

    private String key = "AAAAMtllAlc:APA91bGrqrveOBwyh81ycpsEx-E1r9WJ4nAIdF6d6dvRFjz1NZyTc__z_N5DXE2RhVjlC3vkBwuYehnSewWpIJU9uf-Velr0qyOUS6FPzuE9Y-FnhNxY3_9qpkjaQ89HF77mUcIui1Pm";

    public JSONObject getTopics(ServerListener serverListener) {
        HttpURLConnection httpURLConnection;
        URL url;
        JSONObject response = null;
        try {
            String token = FirebaseInstanceId.getInstance().getToken();
            url = new URL("https://iid.googleapis.com/iid/info/" + token + "?details=true");
            httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.addRequestProperty("Content-Type", "application/json");
            httpURLConnection.addRequestProperty("Authorization", "key=" + key);
            httpURLConnection.setRequestMethod("GET");
            InputStream inputStream = httpURLConnection.getInputStream();
            response = new JSONObject(IOUtil.getString(inputStream));
        } catch (IOException ioe) {
            ioe.printStackTrace();
        } catch (JSONException json) {
            json.printStackTrace();
        }
        return response;
    }

    public void subscribeToTopic(List<Topic> topic) {
        for (int i = 0; i < topic.size(); i++) {
            FirebaseMessaging.getInstance().subscribeToTopic(topic.get(i).topic);
        }
    }

    public void unsubscribeFromTopic(List<Topic> topic) {
        for (int i = 0; i < topic.size(); i++) {
            FirebaseMessaging.getInstance().unsubscribeFromTopic(topic.get(i).topic);
        }
    }

    public JSONObject subscribeToToken(String topic, List<String> tokenString, ServerListener serverListener) {
        HttpURLConnection httpURLConnection;
        URL url;
        JSONObject response = null;
        try {
            url = new URL("https://iid.googleapis.com/iid/v1:batchAdd");
            httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.addRequestProperty("Content-Type", "application/json");
            httpURLConnection.addRequestProperty("Authorization", "key=" + key);
            httpURLConnection.setDoOutput(true);
            httpURLConnection.setDoInput(true);
            JSONObject request = new JSONObject();
            request.put("to", topic);
            JSONArray tokens = new JSONArray();
            for (int i = 0; i < tokenString.size(); i++) {
                tokens.put(tokenString.get(i));
            }
            request.put("registration_tokens", tokens);
            OutputStream outputStream = httpURLConnection.getOutputStream();
            outputStream.write(request.toString().getBytes("UTF-8"));
            InputStream inputStream = httpURLConnection.getInputStream();
            response = new JSONObject(IOUtil.getString(inputStream));
        } catch (IOException ioe) {
            ioe.printStackTrace();
        } catch (JSONException json) {
            json.printStackTrace();
        }
        return response;
    }
}
