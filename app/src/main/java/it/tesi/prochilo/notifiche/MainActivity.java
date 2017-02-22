package it.tesi.prochilo.notifiche;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.util.LinkedList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ServerAsyncTask task = new ServerAsyncTask("http://192.168.1.7:8080/topic");
        List<String> topics = new LinkedList<>();
        topics.add("topic1");
        topics.add("topics2");
        topics.add("topics3");
        task.addTopic(topics,"token_admin");
    }

}
