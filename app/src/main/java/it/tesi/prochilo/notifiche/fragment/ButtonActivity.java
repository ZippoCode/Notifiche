package it.tesi.prochilo.notifiche.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.Button;

import java.util.List;

import it.tesi.prochilo.notifiche.Login;
import it.tesi.prochilo.notifiche.ServerInterface;
import it.tesi.prochilo.notifiche.server.CustomServerManagement;
import it.tesi.prochilo.notifiche.R;
import it.tesi.prochilo.notifiche.Topic;

public class ButtonActivity extends FragmentActivity {

    private Button inviaTopic, getTopic, deleteTopic;
    private String email, password;
    private Login serverFirebase;
    private Login serverCustom;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.server_layout);
        email = getIntent().getStringExtra("email");
        password = getIntent().getStringExtra("password");
        serverFirebase = new Login(email, password);
        serverCustom = new Login(email, password);
        serverFirebase.setServerType(ServerInterface.ServerType.SERVERFIREBASE);
        serverCustom.setServerType(ServerInterface.ServerType.SERVERCUSTOM);
        inviaTopic = (Button) findViewById(R.id.invia_topic);
        getTopic = (Button) findViewById(R.id.ricevi_topic);
        deleteTopic = (Button) findViewById(R.id.elimina_topic);
        inviaTopic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showFragment(inviaTopic);
            }
        });
        getTopic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showFragment(getTopic);
            }
        });
        deleteTopic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showFragment(deleteTopic);
            }
        });
    }

    private void showFragment(Button button) {
        final int itemId = button.getId();
        final Fragment nextFragment;
        switch (itemId) {
            case R.id.invia_topic:
                nextFragment = new PostFragment();
                break;
            case R.id.ricevi_topic:
                nextFragment = new GetFragment();
                break;
            case R.id.elimina_topic:
                nextFragment = new DeleteFragment();
                break;
            default:
                throw new IllegalArgumentException("No fragment for given item");
        }
        getSupportFragmentManager().beginTransaction()
                .add(R.id.server_layout, nextFragment)
                .commit();
    }

    public boolean operation1(List<String> list, CustomServerManagement.HttpMethod httpMethod) {
        if (httpMethod.equals(CustomServerManagement.HttpMethod.POST)) {
            serverCustom.subscribeToTopics(list);
            serverFirebase.subscribeToTopics(list);
            return true;
        }
        serverCustom.unsubscribeFromTopics(list);
        serverFirebase.unsubscribeFromTopics(list);
        return true;
    }

    public List<Topic> operation2() {
        serverCustom.getTopics();
        return serverFirebase.getTopics();
    }
}