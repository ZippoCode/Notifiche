package it.tesi.prochilo.notifiche;

import org.json.JSONArray;

import java.util.List;

public interface ServerMethod {

    public boolean addTopic(List<String> topic, String token);

    public List<Topic> getTopic(String token);

    public boolean deleteTopic(List<String> topic, String token);

}
