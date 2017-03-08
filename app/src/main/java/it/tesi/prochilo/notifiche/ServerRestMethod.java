package it.tesi.prochilo.notifiche;

import java.util.List;

public interface ServerRestMethod {

    void setOnServerListener(ServerListener serverListener);

    boolean postTopics(List<String> topicsList);

    List<Topic> getTopics();

    boolean deleteTopics(List<String> topicsList);

}
