package it.tesi.prochilo.notifiche;

import android.os.AsyncTask;
import android.util.JsonReader;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingService;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

public class CustomFMS extends FirebaseMessagingService {

    private String key = "AAAAMtllAlc:APA91bGrqrveOBwyh81ycpsEx-E1r9WJ4nAIdF6d6dvRFjz1NZyTc__z_N5DXE2RhVjlC3vkBwuYehnSewWpIJU9uf-Velr0qyOUS6FPzuE9Y-FnhNxY3_9qpkjaQ89HF77mUcIui1Pm";

    public JSONObject getTopic() {
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

    public void subscribeToTopic(List<String> topic) {
        for (int i = 0; i < topic.size(); i++) {
            FirebaseMessaging.getInstance().subscribeToTopic(topic.get(i));
        }
    }

    public void unsubscribeFromTopic(List<String> topic) {
        for (int i = 0; i < topic.size(); i++) {
            FirebaseMessaging.getInstance().unsubscribeFromTopic(topic.get(i));
        }
    }
}