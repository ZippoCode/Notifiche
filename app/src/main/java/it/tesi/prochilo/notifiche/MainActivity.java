package it.tesi.prochilo.notifiche;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.util.LinkedList;

import it.tesi.prochilo.notifiche.activity.DeleteActivity;
import it.tesi.prochilo.notifiche.activity.GetActivity;
import it.tesi.prochilo.notifiche.activity.PostActivity;

public class MainActivity extends AppCompatActivity {

    private Button inviaTopic, getTopic, deleteTopic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final ServerListener listener = new ServerListener() {
            @Override
            public void success() {
                Log.d("Richiesta", "Confermata");
            }

            @Override
            public void failure() {
                Log.d("Richiesta", "Annullata");
            }
        };
        inviaTopic = (Button) findViewById(R.id.invia_topic);
        getTopic = (Button) findViewById(R.id.ricevi_topic);
        deleteTopic = (Button) findViewById(R.id.delete_topic);
        inviaTopic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                passaggio(new PostActivity());
            }
        });
        getTopic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                passaggio(new GetActivity());
                //System.out.println(task.getTopics("token_admin", listener));
            }
        });
        deleteTopic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                passaggio(new DeleteActivity());
            }
        });
    }

    private void passaggio(Activity activity) {
        Intent intent = new Intent(this, activity.getClass());
        startActivity(intent);
    }

}
