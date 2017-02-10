package it.tesi.prochilo.notifiche;

import com.google.firebase.messaging.FirebaseMessaging;

public class CustomFirebaseMessaging {

    private CustomFirebaseInstanceIdService customFirebaseInstanceIdService;
    private FirebaseMessaging messaging;


    public CustomFirebaseMessaging(){
        messaging = FirebaseMessaging.getInstance();
    }
    public void subscribeToTopic(String topic) {
        messaging.subscribeToTopic(topic);
    }

    public void subscribeMoreToTopic() {

    }

    public void unsuscribeToTopic(String topic) {
        messaging.unsubscribeFromTopic(topic);
    }

    public void unsubscribeMoreToTopic() {

    }
}
