package it.tesi.prochilo.notifiche;

import java.util.LinkedList;
import java.util.List;

import it.tesi.prochilo.notifiche.server.CustomFMS;
import it.tesi.prochilo.notifiche.server.CustomServerManagement;
import it.tesi.prochilo.notifiche.server.RestInterface;

public class API implements InterfaceAPI {

    private static CustomFMS serverFirebase = null;
    private static RestInterface serverCustom = null;

    public API(String url, String projectId, String token) {
        serverCustom = new CustomServerManagement(url, token);
        serverFirebase = new CustomFMS(projectId, token);
    }

    public API(RestInterface serverCustom, CustomFMS serverFirebase) {
        this.serverCustom = serverCustom;
        this.serverFirebase = serverFirebase;
    }

    @Override
    public void login(ServerListener serverListener) {
        List<Topic> topicsList = serverCustom.getTopics(serverListener);
        if (topicsList != null) {
            List<String> topicsNameList = new LinkedList<>();
            for (Topic topic : topicsList) {
                topicsNameList.add(topic.topic);
            }
            serverFirebase.connetti(topicsNameList, serverListener);
        }
    }

    @Override
    public void logout(ServerListener serverListener) {
        List<Topic> topicsList = serverCustom.getTopics(serverListener);
        if (topicsList != null) {
            List<String> topicsNameList = new LinkedList<>();
            for (Topic topic : topicsList) {
                topicsNameList.add(topic.topic);
            }
            if (topicsNameList.size() > 0)
                serverFirebase.disconnetti(topicsNameList, serverListener);
        }
    }

    @Override
    public List<Topic> getTopics(ServerListener serverListener) {
        return serverCustom.getTopics(serverListener);
    }

    @Override
    public boolean subscribeToTopic(final String topic, final ServerListener serverListener) {
        final LinkedList<String> topicsList = new LinkedList<>();
        topicsList.add(topic);
        return subscribeToTopics(topicsList, serverListener);
    }


    @Override
    public boolean unsubscribeFromTopic(final String topic, final ServerListener serverListener) {
        final List<String> topicsList = new LinkedList<>();
        topicsList.add(topic);
        return unsubscribeFromTopics(topicsList, serverListener);
    }

    @Override
    public boolean subscribeToTopics(final List<String> topicsList, final ServerListener serverListener) {
        return serverCustom.postTopics(topicsList, new ServerListener() {
            @Override
            public void onSuccess() {
                serverFirebase.subscribeToTopics(topicsList, serverListener);
            }

            @Override
            public void onFailure() {
                serverListener.onFailure();
            }
        });
    }


    @Override
    public boolean unsubscribeFromTopics(final List<String> topicsList, final ServerListener serverListener) {
        return serverCustom.deleteTopics(topicsList, new ServerListener() {
            @Override
            public void onSuccess() {
                serverFirebase.unsubscribeFromTopics(topicsList, serverListener);
            }

            @Override
            public void onFailure() {
                serverListener.onFailure();
            }
        });

    }

}
