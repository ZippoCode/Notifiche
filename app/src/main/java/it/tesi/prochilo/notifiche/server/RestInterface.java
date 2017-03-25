package it.tesi.prochilo.notifiche.server;

import java.util.List;

import it.tesi.prochilo.notifiche.ServerListener;
import it.tesi.prochilo.notifiche.Topic;

public interface RestInterface {

    boolean postTopics(List<String> topicsList, ServerListener serverListener);

    List<Topic> getTopics(ServerListener serverListener);

    boolean deleteTopics(List<String> topicsList, ServerListener serverListener);
}
