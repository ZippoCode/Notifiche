package it.tesi.prochilo.notifiche;

import java.util.List;


interface InterfaceAPI {

    /**
     * Sincronizza i topic salvati sul database del server locale e quelli di Firebase.
     * In particolare ha il compito di richiedere la lista di topic dal Server Custom e
     * successivamente di sottoscriverli su Firebase Cloud Messaging
     *
     * @param serverListener Notifica se l'operazione è avvenuta con successo o no
     */
    void login(ServerListener serverListener);

    /**
     * Operazione inversa al login. Sincronizza i topic salvati sul database del server locale
     * e quelli di Firebase. In particolare ha il compito di richiedere la lista di topic dal
     * Server Custom e successivamente di rimuoverli su Firebase Cloud Messaging
     *
     * @param serverListener Notifica se l'operazione è avvenuta con successo o no
     */
    void logout(ServerListener serverListener);

    /**
     * Ritorla la lista dei Topic salvati sul server del database custom
     *
     * @param serverListener Notifica se l'operazione è avvenuta con successo o no
     * @return La lista dei Topic
     */
    List<Topic> getTopics(ServerListener serverListener);

    /**
     * Sottoscrive l'utente ad un Topic sia sul database del server custom sia su
     * Firebase Cloud Messaging
     *
     * @param topic          Il topic
     * @param serverListener Notifica se l'operazione è avvenuta con successo o no
     * @return True se l'operazione è andata a buon fine, false altrimenti
     */
    boolean subscribeToTopic(String topic, ServerListener serverListener);

    /**
     * Sottoscrive l'utente ad una lista di Topic sia sul database del server custom sia su
     * Firebase Cloud Messaging
     *
     * @param topicsList     La lista dei Topic
     * @param serverListener Notifica se l'operazione è avvenuta con successo o no
     * @return True se l'operazione è andata a buon fine, false altrimenti
     */
    boolean subscribeToTopics(List<String> topicsList, ServerListener serverListener);

    /**
     * Rimuove la sottoscrizione all'utente di un Topic sia sul database del server custom sia su
     * Firebase Cloud Messaging
     *
     * @param topic          Il topic
     * @param serverListener Notifica se l'operazione è avvenuta con successo o no
     * @return True se l'operazione è andata a buon fine, false altrimenti
     */
    boolean unsubscribeFromTopic(String topic, ServerListener serverListener);

    /**
     * Rimuove la sottoscrizione all'utente di una lista Topic sia sul database del server custom sia su
     * Firebase Cloud Messaging
     *
     * @param topicsList     La lista dei topic
     * @param serverListener Notifica se l'operazione è avvenuta con successo o no
     * @return True se l'operazione è andata a buon fine, false altrimenti
     */
    boolean unsubscribeFromTopics(List<String> topicsList, ServerListener serverListener);

}
