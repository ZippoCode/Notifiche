package it.tesi.prochilo.notifiche.server;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.regex.Pattern;

import it.tesi.prochilo.notifiche.R;
import it.tesi.prochilo.notifiche.ServerListener;
import it.tesi.prochilo.notifiche.Topic;

public class CustomFMS extends FirebaseMessagingService {

    private static final String TAG = FirebaseMessagingService.class.getCanonicalName();
    private static final String REGULAREXPRESSION = "[a-zA-Z0-9-_.~%]{1,900}";
    private final String mToken;
    private static List<Integer> messageId;
    private static final Random random;
    private FirebaseMessaging mFirebaseMessaging;
    private String projectId = "77244763443";

    static {
        random = new Random();
        messageId = new LinkedList<>();
    }

    public CustomFMS() {
        this("");
    }

    public CustomFMS(final String token) {
        this.mToken = token;
        mFirebaseMessaging = FirebaseMessaging.getInstance();
    }

    /**
     * Sottoscrive il token ad una lista di topic presso il Firebase Cloud Messaging
     *
     * @param topicsList     La lista dei topic
     * @param serverListener
     * @return True se l'operazione è andata a buon fine altrimenti ritorna false
     */
    public boolean subscribeToTopics(List<String> topicsList, ServerListener serverListener) {
        Map<String, String> map = new HashMap<>();
        synchronized (map) {
            for (int i = 0; i < topicsList.size(); i++) {
                if (Pattern.matches(REGULAREXPRESSION, topicsList.get(i))) {
                    mFirebaseMessaging.subscribeToTopic(topicsList.get(i));
                    map.put("topic" + i, topicsList.get(i));
                }
            }
            updateTopicsToCssXMPP(map, "subscribe");
            serverListener.onSuccess();
        }
        return true;
    }

    /**
     * Disiscrive il token alla lista presso il Firebase Cloud Messaging
     *
     * @param topicsList     La lista dei topic
     * @param serverListener
     * @return True se l'operazione è andata a buon fine, altrimenti false
     */
    public boolean unsubscribeFromTopics(List<String> topicsList, ServerListener serverListener) {
        Map<String, String> map = new HashMap<>();
        synchronized (map) {
            for (int i = 0; i < topicsList.size(); i++) {
                if (Pattern.matches(REGULAREXPRESSION, topicsList.get(i))) {
                    mFirebaseMessaging.unsubscribeFromTopic(topicsList.get(i));
                    map.put("topic" + i, topicsList.get(i));
                }
            }
            updateTopicsToCssXMPP(map, "unsubscribe");
            serverListener.onSuccess();
        }
        return true;
    }

    /**
     * Elabora la lista dei topic che riceve quando si invoca la richiesta di GET
     *
     * @param jsonObject Deve contenere la risposta del server FCM
     * @return La lista di topic
     */
    private List<Topic> topicsElaborator(JSONObject jsonObject) throws JSONException {
        List<Topic> topicsList = new LinkedList<>();
        JSONObject topics = (jsonObject.getJSONObject("rel")).getJSONObject("topics");
        Iterator<String> topicsIterator = topics.keys();
        synchronized (topicsIterator) {
            while (topicsIterator.hasNext()) {
                String topicName = topicsIterator.next();
                JSONObject topicInfo = topics.getJSONObject(topicName);
                Topic topic = Topic.Builder.create("", "")
                        .addTopic(topicName)
                        .addTimestamp(topicInfo.getString("addDate"))
                        .build();
                topicsList.add(topic);
            }
        }
        return topicsList;
    }

    public void connetti(List<String> topicsList, ServerListener serverListener) {
        if (topicsList == null || mFirebaseMessaging == null)
            serverListener.onFailure();
        for (int i = 0; i < topicsList.size(); i++) {
            if (Pattern.matches(REGULAREXPRESSION, topicsList.get(i)))
                mFirebaseMessaging.subscribeToTopic(topicsList.get(i));
        }
        mFirebaseMessaging.send(new RemoteMessage.Builder(projectId + "@gcm.googleapis.com")
                .setMessageId(Integer.toString(getMessageId()))
                .setMessageType(".registraId")
                .addData("token", mToken)
                .build());
        serverListener.onSuccess();
    }

    public void disconnetti(List<String> topicsList, ServerListener serverListener) {
        if (topicsList == null || mFirebaseMessaging == null)
            serverListener.onFailure();
        for (int i = 0; i < topicsList.size(); i++) {
            if (Pattern.matches(REGULAREXPRESSION, topicsList.get(i)))
                mFirebaseMessaging.unsubscribeFromTopic(topicsList.get(i));
        }
        mFirebaseMessaging.send(new RemoteMessage.Builder(projectId + "@gcm.googleapis.com")
                .setMessageId(Integer.toString(getMessageId()))
                .setMessageType(".eliminaId")
                .addData("token", mToken)
                .build());
        serverListener.onSuccess();
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
            Notification notification = new Notification.Builder(this)
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setContentTitle(remoteMessage.getNotification().getTitle())
                    .setContentText(remoteMessage.getNotification().getTitle())
                    .build();
            NotificationManager notificationManager = (NotificationManager)
                    getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.notify(001, notification);
        }
    }

    private void updateTopicsToCssXMPP(Map<String, String> map, String message_operation) {
        synchronized (map) {
            mFirebaseMessaging.send(new RemoteMessage.Builder(projectId + "@gcm.googleapis.com")
                    .setMessageType(".messaggio")
                    .setMessageId(Integer.toString(getMessageId()))
                    .setData(map)
                    .addData("token", mToken)
                    .addData("message_operation", message_operation)
                    .build());
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
