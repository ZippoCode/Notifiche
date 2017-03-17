package it.tesi.prochilo.notifiche;

import java.util.LinkedList;
import java.util.List;

import it.tesi.prochilo.notifiche.server.CustomFMS;

public class API implements InterfaceAPI {

    private static CustomFMS serverFirebase = null;
    private static ServerAsyncTask serverCustom = null;

    public API(String url, String token) {
        serverCustom = new ServerAsyncTask(url, token);
        serverFirebase = new CustomFMS(token);
    }

    @Override
    public void login(ServerListener serverListener) {
        List<String> topicsList = new LinkedList<>();
        List<Topic> list = serverCustom.getTopics(serverListener);
        for (Topic topic : list) {
            topicsList.add(topic.topic);
        }
        serverFirebase.connetti(topicsList, serverListener);
    }

    @Override
    public void logout(ServerListener serverListener) {
        List<String> topicsList = new LinkedList<>();
        for (Topic topic : serverCustom.getTopics(serverListener)) {
            topicsList.add(topic.topic);
        }
        serverFirebase.disconnetti(topicsList, serverListener);
    }

    @Override
    public List<Topic> getTopics(ServerListener serverListener) {
        return serverCustom.getTopics(serverListener);
    }

    @Override
    public void subscribeToTopic(String topic, ServerListener serverListener) {
        List<String> topicsList = new LinkedList<>();
        topicsList.add(topic);
        serverCustom.subscribeToTopics(topicsList, serverListener);
        serverFirebase.subscribeToTopics(topicsList, serverListener);
    }


    @Override
    public void unsubscribeFromTopic(String topic, ServerListener serverListener) {
        List<String> topicsList = new LinkedList<>();
        topicsList.add(topic);
        serverCustom.unsubscribeFromTopics(topicsList, serverListener);
        serverFirebase.unsubscribeFromTopics(topicsList, serverListener);
    }

    @Override
    public void subscribeToTopics(List<String> topicsList, ServerListener serverListener) {
        serverCustom.subscribeToTopics(topicsList, serverListener);
        serverFirebase.subscribeToTopics(topicsList, serverListener);
    }


    @Override
    public void unsubscribeFromTopics(List<String> topicsList, ServerListener serverListener) {
        serverCustom.unsubscribeFromTopics(topicsList, serverListener);
        serverFirebase.unsubscribeFromTopics(topicsList, serverListener);
    }

}
