package it.tesi.prochilo.notifiche;

import java.util.List;

public interface ServerInterface {

    /**
     * Sottoscrivere l'utente alla lista di Topic
     *
     * @param topicsList La lista di topic
     * @return True se l'operazione è andata a buon fine, false altrimenti
     */
    boolean subscribeToTopics(List<String> topicsList, ServerListener serverListener);

    /**
     * Ritorna la lista di topic a cui l'utente è attualmente iscritto
     *
     * @return La lista di Topic
     */
    List<Topic> getTopics(ServerListener serverListener);

    /**
     * Disiscrive l'utente ai topic presenti all'interno della lista
     *
     * @param topicsList La lista di Topic
     * @return True se l'operazione è andata a buon fine, false altrimenti
     */
    boolean unsubscribeFromTopics(List<String> topicsList, ServerListener serverListener);

}
