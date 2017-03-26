package it.tesi.prochilo.notifiche.ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.LinkedList;
import java.util.List;

import it.tesi.prochilo.notifiche.R;
import it.tesi.prochilo.notifiche.ServerListener;
import it.tesi.prochilo.notifiche.util.Login;

public class DeleteActivity extends AppCompatActivity {


    private EditText topic1, topic2;
    private Toast mSuccess, mFailure;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.post_delete_layout);
        topic1 = (EditText) findViewById(R.id.et_topic1);
        topic2 = (EditText) findViewById(R.id.et_topic2);
        mSuccess = Toast.makeText(this, "Sottoscrizione eseguita", Toast.LENGTH_SHORT);
        mFailure = Toast.makeText(this, "Sottoscrizione fallita", Toast.LENGTH_SHORT);
        Button button = (Button) findViewById(R.id.button_post_delete);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                List<String> list = new LinkedList<>();
                list.add(topic1.getText().toString());
                list.add(topic2.getText().toString());
                Login.getAPI().unsubscribeFromTopics(list, new ServerListener() {
                    @Override
                    public void onSuccess() {
                        mSuccess.show();
                    }

                    @Override
                    public void onFailure() {
                        mFailure.show();
                    }
                });
            }
        });
    }
}
