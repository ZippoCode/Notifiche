package it.tesi.prochilo.notifiche;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import org.json.JSONObject;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        TextView testo = (TextView) findViewById(R.id.testo);
        CustomFirebaseInstanceIdService customFirebaseInstanceIdService = new CustomFirebaseInstanceIdService();
        String token = customFirebaseInstanceIdService.getToken();
        testo.setText(token);
    }
}
