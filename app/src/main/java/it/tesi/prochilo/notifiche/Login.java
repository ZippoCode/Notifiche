package it.tesi.prochilo.notifiche;

import android.util.Log;

import java.io.Serializable;
import java.util.List;

public class Login {

    private static final String TAG = Login.class.getCanonicalName();
    private final String mMail, mPassword, token;
    private ServerAsyncTask task;

    public Login(final String mail, final String password) {
        this.mMail = mail;
        this.mPassword = password;
        token = "token_admin";
        task = new ServerAsyncTask("http://192.168.1.7:8080/topic", token);
        task.setOnServerListener(new ServerListener() {
            @Override
            public void onSuccess() {
                Log.d(TAG, "Operazione eseguita con successo");
            }

            @Override
            public void onFailure() {
                Log.d(TAG, "Operazione non riuscita");
            }
        });
    }

    public boolean subscribeToTopics(List<String> list) {
        return task.subscribeToTopics(list);
    }

    public List<Topic> getTopics() {
        return task.getTopics();
    }

    public boolean unsubscribeToTopics(List<String> list) {
        return task.unsubscribeFromTopics(list);
    }

}
