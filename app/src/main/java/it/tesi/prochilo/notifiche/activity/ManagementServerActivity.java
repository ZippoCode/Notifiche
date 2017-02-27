package it.tesi.prochilo.notifiche.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import it.tesi.prochilo.notifiche.R;

public class ManagementServerActivity extends AppCompatActivity {

    private Button inviaTopic, getTopic, deleteTopic;
    private String token;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.server_layout);
        this.token = getIntent().getStringExtra("token");
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
        intent.putExtra("token", token);
        startActivity(intent);
    }

}