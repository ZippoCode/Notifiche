package it.tesi.prochilo.notifiche;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
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
        JSONManagement management = new JSONManagement();
        JSONArray array = null;
        try {
            array = new JSONArray();
            JSONObject topic1 = new JSONObject();
            topic1.put("name", "topic1");
            topic1.put("date", "226655");
            array.put(topic1);
            topic1 = new JSONObject();
            topic1.put("name","topic2");
            topic1.put("date","180791");
            array.put(topic1);
        }catch (JSONException jsone){

        }
        testo.setText((management.getTopics(array)).toString());
    }
}
