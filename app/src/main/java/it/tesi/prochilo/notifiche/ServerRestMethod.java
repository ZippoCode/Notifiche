package it.tesi.prochilo.notifiche;

import org.json.JSONException;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.List;

public interface ServerRestMethod {

    public boolean postTopics(List<String> topicsList) throws IOException;

    public List<Topic> getTopics() throws IOException;

    public boolean deleteTopics(List<String> topicsList) throws IOException;

}
