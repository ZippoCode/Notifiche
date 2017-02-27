package it.tesi.prochilo.notifiche;

import java.util.List;

public interface ServerInterface {

    void setOnServerListener(ServerListener serverListener);

    boolean subscribeToTopics(List<String> topicsList, String token);

    List<Topic> getTopics(String topics);

    boolean unsubscribeFromTopics(List<String> topics, String token);

}
