package it.tesi.prochilo.notifiche;

import org.json.JSONException;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.List;

public interface ServerRestMethod {

    public boolean postTopics(List<String> topicsList, String token) throws IOException;

    public List<Topic> getTopics(String token) throws IOException;

    public boolean deleteTopics(List<String> topicsList, String token) throws IOException;

}
