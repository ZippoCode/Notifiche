package it.tesi.prochilo.notifiche;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.util.LinkedList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        (new TestToken()).execute();
        ServerAsyncTask task = new ServerAsyncTask("http://192.168.1.7:8080/topic");
        List<String> topics = new LinkedList<>();
        topics.add("topics3");
        topics.add("prova_finale");
        task.deleteTopic(topics, "token_admin");
    }


    private class TestToken extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            CustomFMS customFMS = new CustomFMS();
            System.out.println(customFMS.getTopic().toString());
            return null;
        }
    }


}
