package it.tesi.prochilo.notifiche;

import org.json.JSONArray;

import java.util.List;

public interface ServerMethod {

    public boolean addTopics(List<Topic> topic, String token, ServerListener serverListener);

    public List<Topic> getTopics(String token, ServerListener serverListener);

    public boolean deleteTopics(List<Topic> topic, String token, ServerListener serverListener);

}
