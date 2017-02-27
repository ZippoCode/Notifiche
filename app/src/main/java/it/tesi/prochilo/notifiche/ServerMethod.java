package it.tesi.prochilo.notifiche;

import java.util.List;

public interface ServerMethod {

    public boolean subscribeToTopics(List<String> topicsList, String token, ServerListener serverListener);

    public List<Topic> getTopics(String token, ServerListener serverListener);

    public boolean unsubscribeFromTopics(List<String> topicsList, String token, ServerListener serverListener);

}
