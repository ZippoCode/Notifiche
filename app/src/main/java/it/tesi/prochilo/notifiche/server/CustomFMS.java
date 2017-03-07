package it.tesi.prochilo.notifiche.server;

import android.support.annotation.NonNull;
import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.regex.Pattern;

import it.tesi.prochilo.notifiche.IOUtil;
import it.tesi.prochilo.notifiche.ServerRestMethod;
import it.tesi.prochilo.notifiche.Topic;

public class CustomFMS extends FirebaseMessagingService implements ServerRestMethod {

    private static final String TAG = FirebaseMessagingService.class.getCanonicalName();
    private String mKey = null;
    private static final String REGULAREXPRESSION = "[a-zA-Z0-9-_.~%]{1,900}";
    private static final Pattern pattern;
    private final int connectionTimeout = 5000;
    private final String mToken;
    private static List<Integer> messageId;
    private static final Random random;
    private FirebaseMessaging mFirebaseMessaging;

    static {
        pattern = Pattern.compile(REGULAREXPRESSION);
        random = new Random();
        messageId = new LinkedList<>();
    }

    public CustomFMS() {
        this("");
    }

    public CustomFMS(final String token) {
        mKey = "AAAAMtllAlc:APA91bGrqrveOBwyh81ycpsEx-E1r9WJ4nAIdF6d6dvRFjz1NZyTc__z_N5DXE2RhVjlC3vkBwuYehnSewWpIJU9uf-Velr0qyOUS6FPzuE9Y-FnhNxY3_9qpkjaQ89HF77mUcIui1Pm";
        this.mToken = FirebaseInstanceId.getInstance().getToken();
        mFirebaseMessaging = FirebaseMessaging.getInstance();
        mFirebaseMessaging.send(new RemoteMessage.Builder("218395640407" + "@gcm.googleapis.com")
                .setMessageId(Integer.toString(getMessageId()))
                .setMessageType(".registraId")
                .addData("token", token)
                .build());
    }

    /**
     * Sottoscrive il token ad una lista di topic presso il Firebase Cloud Messaging
     *
     * @param topicsList La lista dei topic
     * @return True se l'operazione è andata a buon fine altrimenti ritorna false
     */
    @Override
    public boolean postTopics(List<String> topicsList) throws IOException {
        Map<String, String> map = new HashMap<>();
        for (int i = 0; i < topicsList.size(); i++) {
            if (Pattern.matches(REGULAREXPRESSION, topicsList.get(i))) {
                mFirebaseMessaging.subscribeToTopic(topicsList.get(i));
                map.put("topic" + i, topicsList.get(i));
            }
        }
        updateTopicsToCssXMPP(map);
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
        Map<String, String> map = new HashMap<>();
        for (int i = 0; i < topicsList.size(); i++) {
            if (Pattern.matches(REGULAREXPRESSION, topicsList.get(i))) {
                mFirebaseMessaging.unsubscribeFromTopic(topicsList.get(i));
                map.put("topic" + i, topicsList.get(i));
            }
        }
        updateTopicsToCssXMPP(map);
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

    private void updateTopicsToCssXMPP(Map<String, String> map) {
        mFirebaseMessaging.send(new RemoteMessage.Builder("218395640407" + "@gcm.googleapis.com")
                .setMessageType(".messaggio")
                .setMessageId(Integer.toString(getMessageId()))
                .setData(map)
                .addData("token", "token_admin")
                .addData("message_operation", "subscribe")
                .build());
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        Log.d(TAG, "From: " + remoteMessage.getFrom());
        if (remoteMessage.getData().size() > 0) {
            Log.d(TAG, "Message data payload: " + remoteMessage.getData());
            Map<String, String> payload = remoteMessage.getData();
            payloadElaborate(payload);
        }
        if (remoteMessage.getNotification() != null) {
            Log.d(TAG, "Message Notification Body: " + remoteMessage.getNotification().getBody());
        }
    }

    @NonNull
    private Integer getMessageId() {
        int id;
        while (messageId.contains((id = random.nextInt()))) {
        }
        messageId.add(id);
        return id;
    }

    private void payloadElaborate(Map<String, String> payload) {
        String operation = payload.get("message_operation");
        LinkedList<String> keys = new LinkedList<>(payload.keySet());
        LinkedList<String> topicsList = new LinkedList<>();
        for (String key : keys) {
            if (key.contains("topic"))
                topicsList.add(payload.get(key));
        }
        for (String topic : topicsList) {
            if (operation.equals("subscribe"))
                mFirebaseMessaging.subscribeToTopic(topic);
            else
                mFirebaseMessaging.unsubscribeFromTopic(topic);
        }
    }
}