package it.tesi.prochilo.notifiche.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.LinkedList;

import it.tesi.prochilo.notifiche.R;
import it.tesi.prochilo.notifiche.ServerAsyncTask;
import it.tesi.prochilo.notifiche.ServerListener;

public class PostActivity extends Activity {

    Button button;
    EditText topic1, topic2, topic3, token;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.post_delete_layout);
        button = (Button) findViewById(R.id.postButton);
        token = (EditText) findViewById(R.id.postTokenET);
        topic1 = (EditText) findViewById(R.id.postTopicName1);
        topic2 = (EditText) findViewById(R.id.postTopicName2);
        topic3 = (EditText) findViewById(R.id.postTopicName3);
        final Toast success = Toast.makeText(this, "Rischiesta accettatta", Toast.LENGTH_LONG);
        final Toast insuccess = Toast.makeText(this, "Rischiesta rifiutata", Toast.LENGTH_LONG);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String tokenString = (token.getText()).toString();
                String topicName1 = topic1.getText().toString();
                String topicName2 = topic2.getText().toString();
                String topicName3 = topic3.getText().toString();
                LinkedList<String> topicsList = new LinkedList<String>();
                topicsList.add(topicName1);
                topicsList.add(topicName2);
                topicsList.add(topicName3);
                ServerAsyncTask task = new ServerAsyncTask("http://192.168.1.7:8080/topic", new ServerListener() {
                    @Override
                    public void success() {
                        success.show();
                    }

                    @Override
                    public void failure() {
                        insuccess.show();
                    }
                });
                task.addTopics(topicsList, tokenString);
            }
        });
    }
}
