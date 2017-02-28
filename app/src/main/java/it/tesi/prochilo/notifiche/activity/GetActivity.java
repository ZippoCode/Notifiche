package it.tesi.prochilo.notifiche.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import it.tesi.prochilo.notifiche.R;
import it.tesi.prochilo.notifiche.ServerAsyncTask;
import it.tesi.prochilo.notifiche.ServerListener;
import it.tesi.prochilo.notifiche.Topic;

public class GetActivity extends AppCompatActivity {

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.get_layout);
        String token = getIntent().getStringExtra("token");
        final Toast success = Toast.makeText(this, "Richiesta accettatta", Toast.LENGTH_LONG);
        final Toast insuccess = Toast.makeText(this, "Richiesta rifiutata", Toast.LENGTH_LONG);
        ServerAsyncTask task = new ServerAsyncTask("http://192.168.1.63:8080/topic");
        task.setOnServerListener(new ServerListener() {
            @Override
            public void success() {
                success.show();
            }

            @Override
            public void failure() {
                insuccess.show();
            }
        });
        List<Topic> topic = task.getTopics(token);
        if (topic != null)
            ((TextView) findViewById(R.id.getTextTopics)).setText(topic.toString());
    }
}