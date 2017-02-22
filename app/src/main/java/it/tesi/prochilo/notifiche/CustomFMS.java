package it.tesi.prochilo.notifiche;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingService;

import java.util.List;

public class CustomFMS extends FirebaseMessagingService {


    public void subscribeToTopic(List<String> topic) {
        for (int i = 0; i < topic.size(); i++) {
            FirebaseMessaging.getInstance().subscribeToTopic(topic.get(i));
        }
    }

    public void unsubribeFromTopic(List<String> topic) {
        for (int i = 0; i < topic.size(); i++) {
            FirebaseMessaging.getInstance().unsubscribeFromTopic(topic.get(i));
        }
    }
}
