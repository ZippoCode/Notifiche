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
        List<Topic> topics = getTopics(serverListener);
        for (Topic topic : topics) {
            topicsList.add(topic.topic);
        }
        if (topicsList.size() > 0)
            serverFirebase.connetti(topicsList, serverListener);
    }

    @Override
    public void logout(ServerListener serverListener) {
        List<String> topicsList = new LinkedList<>();
        List<Topic> topics = getTopics(serverListener);
        for (Topic topic : topics) {
            topicsList.add(topic.topic);
        }
        if (topicsList.size() > 0)
            serverFirebase.disconnetti(topicsList, serverListener);
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
        return serverCustom.subscribeToTopics(topicsList, new ServerListener() {
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
        return serverCustom.unsubscribeFromTopics(topicsList, new ServerListener() {
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
