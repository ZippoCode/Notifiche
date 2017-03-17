package it.tesi.prochilo.notifiche;

import java.util.List;


public interface InterfaceAPI {

    void login(ServerListener serverListener);

    void logout(ServerListener serverListener);

    List<Topic> getTopics(ServerListener serverListener);

    void subscribeToTopic(String topic, ServerListener serverListener);

    void subscribeToTopics(List<String> topicsList, ServerListener serverListener);

    void unsubscribeFromTopic(String topic, ServerListener serverListener);

    void unsubscribeFromTopics(List<String> topicsList, ServerListener serverListener);

}
