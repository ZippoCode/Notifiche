package it.tesi.prochilo.notifiche;

import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
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


    private String mKey = null;
    private static final String REGULAREXPRESSION = "[a-zA-Z0-9-_.~%]{1,900}";
    private static final Pattern pattern;
    private final int connectionTimeout = 5000;
    private final String mToken;

    static {
        pattern = Pattern.compile(REGULAREXPRESSION);
    }

    public CustomFMS() {
        mToken = FirebaseInstanceId.getInstance().getToken();
    }

    /**
     * @param key
     */
    public CustomFMS(final String key) {
        mKey = "AAAAMtllAlc:APA91bGrqrveOBwyh81ycpsEx-E1r9WJ4nAIdF6d6dvRFjz1NZyTc__z_N5DXE2RhVjlC3vkBwuYehnSewWpIJU9uf-Velr0qyOUS6FPzuE9Y-FnhNxY3_9qpkjaQ89HF77mUcIui1Pm";
        this.mToken = FirebaseInstanceId.getInstance().getToken();
        //this.mKey = key;
    }

    /**
     * Sottoscrive il token ad una lista di topic presso il Firebase Cloud Messaging
     *
     * @param topicsList La lista dei topic
     * @return True se l'operazione è andata a buon fine altrimenti ritorna false
     */
    @Override
    public boolean postTopics(List<String> topicsList) throws IOException {
        for (int i = 0; i < topicsList.size(); i++) {
            String topic = topicsList.get(i);
            if (topic != null && topic.startsWith("/topics/")) {
                Log.w("FirebaseMessaging",
                        "Format /topics/topics-name is deprecated. Only 'topic-name' should be used in subscribe Topics ");
                topic = topic.substring("/topics/".length());
            }
            if (topic == null || !pattern.matcher(topic).matches()) {
                throw new IllegalArgumentException("Valore errato.");
            }
            FirebaseInstanceId instance = FirebaseInstanceId.getInstance();
            String valueOf2 = String.valueOf("S!");
            String valueOf3 = String.valueOf(topic);
            instance.zzjt(valueOf3.length() != 0 ? valueOf2.concat(valueOf3) : new String(valueOf2));
        }
        return true;
    }

    /**
     * Ritorna la lista di topic a cui è iscritto il token sul Firebase Cloud Messaging
     *
     * @return La lista dei Topic
     */
    @Override
    public List<Topic> getTopics() throws IOException {
        HttpURLConnection httpURLConnection = null;
        URL url;
        JSONObject response = null;
        List<Topic> topicList = null;
        try {
            url = new URL("https://iid.googleapis.com/iid/info/" + mToken + "?details=true");
            httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setConnectTimeout(connectionTimeout);
            httpURLConnection.addRequestProperty("Content-Type", "application/json");
            httpURLConnection.addRequestProperty("Authorization", "key=" + mKey);
            httpURLConnection.setRequestMethod("GET");
            httpURLConnection.connect();
            if (httpURLConnection != null) {
                InputStream inputStream = httpURLConnection.getInputStream();
                response = new JSONObject(IOUtil.getString(inputStream));
                topicList = topicsElaborator(response);
            }
        } catch (JSONException jsone) {
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
     * Disiscrive il token alla lista presso il Firebase Cloud Messaging
     *
     * @param topicsList La lista dei topic
     * @return True se l'operazione è andata a buon fine, altrimenti false
     */
    @Override
    public boolean deleteTopics(List<String> topicsList) throws IOException {
        for (int i = 0; i < topicsList.size(); i++) {
            if (Pattern.matches(REGULAREXPRESSION, topicsList.get(i)))
                FirebaseMessaging.getInstance().unsubscribeFromTopic(topicsList.get(i));
        }
        return true;
    }

    /**
     * Elabora la lista dei topic che riceve quando si invoca la richiesta di GET
     *
     * @param jsonObject Deve contenere la risposta del server FCM
     * @return La lista di topic
     */
    private List<Topic> topicsElaborator(JSONObject jsonObject) {
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
        }
        return topicsList;
    }

}
