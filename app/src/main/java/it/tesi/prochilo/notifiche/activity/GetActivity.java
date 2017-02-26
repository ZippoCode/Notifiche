package it.tesi.prochilo.notifiche.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import it.tesi.prochilo.notifiche.R;
import it.tesi.prochilo.notifiche.ServerAsyncTask;
import it.tesi.prochilo.notifiche.ServerListener;

public class GetActivity extends Activity {

    Button ok;
    EditText et;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.get_layout);
        ok = (Button) findViewById(R.id.getInvia);
        et = (EditText) findViewById(R.id.getText);
        final Toast success = Toast.makeText(this, "Rischiesta accettatta", Toast.LENGTH_LONG);
        final Toast insuccess = Toast.makeText(this, "Rischiesta rifiutata", Toast.LENGTH_LONG);
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Editable token = et.getText();
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
                ((TextView) findViewById(R.id.getTextTopics)).setText(task.getTopics(token.toString()).toString());
            }
        });
    }

}
