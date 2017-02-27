package it.tesi.prochilo.notifiche;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingService;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Pattern;

public class CustomFMS extends FirebaseMessagingService implements ServerMethod {

    private String key = "AAAAMtllAlc:APA91bGrqrveOBwyh81ycpsEx-E1r9WJ4nAIdF6d6dvRFjz1NZyTc__z_N5DXE2RhVjlC3vkBwuYehnSewWpIJU9uf-Velr0qyOUS6FPzuE9Y-FnhNxY3_9qpkjaQ89HF77mUcIui1Pm";
    private String regularExpression = "[a-zA-Z0-9-_.~%]{1,900}";

    /**
     * Sottoscrive il token ad una lista di topic presso il Firebase Cloud Messaging
     *
     * @param topic
     * @param token
     * @param serverListener
     * @return
     */
    @Override
    public boolean subscribeToTopics(List<String> topic, String token, ServerListener serverListener) {
        boolean flag = true;
        for (int i = 0; i < topic.size(); i++) {
            if (Pattern.matches(regularExpression, topic.get(i)))
                FirebaseMessaging.getInstance().subscribeToTopic(topic.get(i));
            else
                flag = false;
        }
        if (flag)
            serverListener.success();
        else
            serverListener.failure();
        return true;
    }

    /**
     * Ritorna la lista di topic a cui è iscritto il token sul Firebase Cloud Messaging
     *
     * @param token
     * @return
     */
    @Override
    public List<Topic> getTopics(String token, ServerListener serverListener) {
        HttpURLConnection httpURLConnection;
        URL url;
        JSONObject response = null;
        try {
            url = new URL("https://iid.googleapis.com/iid/info/" + token + "?details=true");
            httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.addRequestProperty("Content-Type", "application/json");
            httpURLConnection.addRequestProperty("Authorization", "key=" + key);
            httpURLConnection.setRequestMethod("GET");
            InputStream inputStream = httpURLConnection.getInputStream();
            response = new JSONObject(IOUtil.getString(inputStream));
            int httpResponseCode = httpURLConnection.getResponseCode();
            if (httpResponseCode == HttpURLConnection.HTTP_OK) {
                serverListener.success();
            }
        } catch (IOException ioe) {
            ioe.printStackTrace();
            serverListener.failure();
        } catch (JSONException json) {
            json.printStackTrace();
            serverListener.failure();
        }
        return elaborateTopics(response);
    }

    /**
     * Disiscrive il token alla lista presso il Firebase Cloud Messaging
     *
     * @param topic
     * @param token
     * @return
     */
    @Override
    public boolean unsubscribeFromTopics(List<String> topic, String token, ServerListener serverListener) {
        boolean flag = true;
        for (int i = 0; i < topic.size(); i++) {
            if (Pattern.matches(regularExpression, topic.get(i)))
                FirebaseMessaging.getInstance().unsubscribeFromTopic(topic.get(i));
            else
                flag = false;
        }
        if (flag)
            serverListener.success();
        else
            serverListener.failure();
        return true;
    }

    public JSONObject subscribeToToken(String topic, List<String> tokenString) {
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
            int httpResponseCode = httpURLConnection.getResponseCode();
            if (httpResponseCode == HttpURLConnection.HTTP_OK) {
                //RICHIESTA OK
            }
        } catch (IOException ioe) {
            ioe.printStackTrace();
        } catch (JSONException json) {
            json.printStackTrace();
        }
        return response;
    }

    private List<Topic> elaborateTopics(JSONObject jsonObject) {
        List<Topic> topicsList = new LinkedList<>();
        try {
            JSONObject topics = (jsonObject.getJSONObject("rel")).getJSONObject("topics");
            Iterator<String> topicsIterator = topics.keys();
            while (topicsIterator.hasNext()) {
                String topicName = topicsIterator.next();
                JSONObject topicInfo = topics.getJSONObject(topicName);
                Topic topic = Topic.Builder.create("", "")
                        .addTopic(topicName)
                        .addTimestamp(topicInfo.getString("addDate"))
                        .build();
                topicsList.add(topic);
            }
        } catch (JSONException jsone) {
            jsone.printStackTrace();
        }
        return topicsList;
    }
}
