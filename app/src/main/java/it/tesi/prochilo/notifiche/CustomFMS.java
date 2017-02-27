package it.tesi.prochilo.notifiche;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingService;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Pattern;

public class CustomFMS extends FirebaseMessagingService implements ServerRestMethod {

    private String key = "AAAAMtllAlc:APA91bGrqrveOBwyh81ycpsEx-E1r9WJ4nAIdF6d6dvRFjz1NZyTc__z_N5DXE2RhVjlC3vkBwuYehnSewWpIJU9uf-Velr0qyOUS6FPzuE9Y-FnhNxY3_9qpkjaQ89HF77mUcIui1Pm";
    private String regularExpression = "[a-zA-Z0-9-_.~%]{1,900}";

    /**
     * Sottoscrive il token ad una lista di topic presso il Firebase Cloud Messaging
     *
     * @param topic
     * @param token
     * @return
     */
    @Override
    public boolean postTopics(List<String> topic, String token) throws IOException {
        for (int i = 0; i < topic.size(); i++) {
            if (Pattern.matches(regularExpression, topic.get(i)))
                FirebaseMessaging.getInstance().subscribeToTopic(topic.get(i));
        }
        return true;
    }

    /**
     * Ritorna la lista di topic a cui è iscritto il token sul Firebase Cloud Messaging
     *
     * @param token
     * @return
     */
    @Override
    public List<Topic> getTopics(String token) throws IOException {
        HttpURLConnection httpURLConnection = null;
        URL url;
        JSONObject response = null;
        List<Topic> topicList;
        try {
            url = new URL("https://iid.googleapis.com/iid/info/" + token + "?details=true");
            httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.addRequestProperty("Content-Type", "application/json");
            httpURLConnection.addRequestProperty("Authorization", "key=" + key);
            httpURLConnection.setRequestMethod("GET");
            InputStream inputStream = httpURLConnection.getInputStream();
            response = new JSONObject(IOUtil.getString(inputStream));
            int httpResponseCode = httpURLConnection.getResponseCode();
            topicList = elaborateTopics(response);
        } catch (JSONException jsone) {

        } finally {
            Topic topic = Topic.Builder.create("null", "null")
                    .addTopic("null")
                    .addTimestamp("null")
                    .build();
            topicList = new LinkedList<>();
            topicList.add(topic);
            if (httpURLConnection != null) {
                httpURLConnection.disconnect();
            }
        }
        return topicList;
    }

    /**
     * Disiscrive il token alla lista presso il Firebase Cloud Messaging
     *
     * @param topic
     * @param token
     * @return
     */
    @Override
    public boolean deleteTopics(List<String> topic, String token) throws IOException {
        for (int i = 0; i < topic.size(); i++) {
            if (Pattern.matches(regularExpression, topic.get(i)))
                FirebaseMessaging.getInstance().unsubscribeFromTopic(topic.get(i));
        }
        return true;
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
