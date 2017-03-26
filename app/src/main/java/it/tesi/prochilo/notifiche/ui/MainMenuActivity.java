package it.tesi.prochilo.notifiche.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;

import it.tesi.prochilo.notifiche.MainActivity;
import it.tesi.prochilo.notifiche.ServerListener;
import it.tesi.prochilo.notifiche.R;
import it.tesi.prochilo.notifiche.Topic;
import it.tesi.prochilo.notifiche.util.Login;

public class MainMenuActivity extends AppCompatActivity {

    private static final String TAG = MainMenuActivity.class.getCanonicalName();
    private List<Topic> topicsList;
    private BaseAdapter mAdapter;

    private ServerListener serverListener = new ServerListener() {
        @Override
        public void onSuccess() {
        }

        @Override
        public void onFailure() {

        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.topics_list_layout);
        topicsList = Login.getAPI().getTopics(serverListener);
        NavigationView mNavigationView = (NavigationView) findViewById(R.id.navigation_view);
        mNavigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Log.d(TAG, "Selected: "+item);
                showActivity(item);
                return false;
            }
        });

        ListView mListView = (ListView) findViewById(R.id.topic_list);
        mAdapter = getCustomAdapter();
        mListView.setAdapter(mAdapter);
    }


    @Override
    public void onStart() {
        super.onStart();
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.optionmenu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent;
        switch (item.getItemId()) {
            case R.id.logout:
                Login.getAPI().logout(serverListener);
                intent = new Intent(this, MainActivity.class);
                startActivity(intent);
                break;
            default:
                throw new IllegalArgumentException("item don't fount");
        }
        return true;
    }

    private BaseAdapter getCustomAdapter() {
        return  new BaseAdapter() {
            @Override
            public int getCount() {
                return topicsList.size();
            }

            @Override
            public Topic getItem(int i) {
                return topicsList.get(i);
            }

            @Override
            public long getItemId(int i) {
                return i;
            }

            @Override
            public View getView(int i, View view, ViewGroup viewGroup) {
                if (view == null) {
                    view = LayoutInflater.from(getBaseContext())
                            .inflate(R.layout.topic_layout, null);
                }
                ((TextView) view.findViewById(R.id.topic_name)).setText(getItem(i).topic);
                return view;
            }
        };
    }

    private void showActivity(final MenuItem menuItem) {
        Intent intent;
        switch (menuItem.getItemId()) {
            case R.id.subscribe_topics:
                intent = new Intent(this, PostActivity.class);
                startActivity(intent);
                break;
            case R.id.unsubscribe_topics:
                intent = new Intent(this, DeleteActivity.class);
                startActivity(intent);
                break;
            default:
                throw new IllegalArgumentException();
        }

    }

}