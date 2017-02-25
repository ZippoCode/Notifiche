package it.tesi.prochilo.notifiche;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.firebase.iid.FirebaseInstanceId;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.LinkedList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button buttone = (Button) findViewById(R.id.delete_topic);
        buttone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        ServerAsyncTask task = new ServerAsyncTask("http://192.168.1.7:8080/topic");
        Topic topic = Topic.Builder.create("id", "id")
                .addTopic("Listener_Bug")
                .addTimestamp("45865")
                .build();
        List<Topic> list = new LinkedList<>();
        list.add(topic);
        task.addTopics(list, "token_admin", new ServerListener() {

            @Override
            public void success() {
                System.out.println("CI SONO RIUSCITO");
            }

            @Override
            public void failure() {
                System.out.println("CI SONO RIUSCITO COMUNQUE");
            }
        });
        //(new TestToken()).execute();
    }


    private class TestToken extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            CustomFMS customFMS = new CustomFMS();
            System.out.println(customFMS.getTopics());
            return null;
        }
    }


}
