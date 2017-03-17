package it.tesi.prochilo.notifiche;

import java.util.List;


public interface InterfaceAPI {

    void login(ServerListener serverListener);

    void logout(ServerListener serverListener);

    List<Topic> getTopics(ServerListener serverListener);

    boolean subscribeToTopic(String topic, ServerListener serverListener);

    boolean subscribeToTopics(List<String> topicsList, ServerListener serverListener);

    boolean unsubscribeFromTopic(String topic, ServerListener serverListener);

    boolean unsubscribeFromTopics(List<String> topicsList, ServerListener serverListener);

}
