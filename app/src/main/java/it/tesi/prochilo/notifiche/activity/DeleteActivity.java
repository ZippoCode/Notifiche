package it.tesi.prochilo.notifiche.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.LinkedList;

import it.tesi.prochilo.notifiche.IOUtil;
import it.tesi.prochilo.notifiche.R;
import it.tesi.prochilo.notifiche.ServerAsyncTask;
import it.tesi.prochilo.notifiche.ServerListener;

public class DeleteActivity extends AppCompatActivity {

    Button button;
    EditText topic1, topic2, topic3;
    String token;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.post_delete_layout);
        button = (Button) findViewById(R.id.postButton);
        token = getIntent().getStringExtra("token");
        final Toast success = Toast.makeText(this, "Rischiesta accettatta", Toast.LENGTH_LONG);
        final Toast insuccess = Toast.makeText(this, "Rischiesta rifiutata", Toast.LENGTH_LONG);
        topic1 = (EditText) findViewById(R.id.postTopicName1);
        topic2 = (EditText) findViewById(R.id.postTopicName2);
        topic3 = (EditText) findViewById(R.id.postTopicName3);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String topicName1 = topic1.getText().toString();
                String topicName2 = topic2.getText().toString();
                String topicName3 = topic3.getText().toString();
                LinkedList<String> topicsList = new LinkedList<String>();
                topicsList.add(topicName1);
                topicsList.add(topicName2);
                topicsList.add(topicName3);
                ServerAsyncTask task = new ServerAsyncTask("http://192.168.1.63:8080/topic", new ServerListener() {
                    @Override
                    public void success() {
                        success.show();
                    }

                    @Override
                    public void failure() {
                        insuccess.show();
                    }
                });
                task.unsubscribeFromTopics(topicsList, token);
            }
        });
    }

}
