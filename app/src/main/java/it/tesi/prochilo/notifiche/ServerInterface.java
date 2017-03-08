package it.tesi.prochilo.notifiche;

import java.util.List;

public interface ServerInterface {

    enum ServerType {SERVERCUSTOM, SERVERFIREBASE}

    void setServerType(ServerType server);

    boolean subscribeToTopics(List<String> topicsList);

    List<Topic> getTopics();

    boolean unsubscribeFromTopics(List<String> topicsList);

}
