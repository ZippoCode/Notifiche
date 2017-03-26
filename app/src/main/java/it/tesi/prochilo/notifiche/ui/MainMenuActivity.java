package it.tesi.prochilo.notifiche.ui;

import android.content.Intent;
import android.os.Bundle;
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
import android.widget.Toast;

import java.util.List;

import it.tesi.prochilo.notifiche.MainActivity;
import it.tesi.prochilo.notifiche.ServerListener;
import it.tesi.prochilo.notifiche.R;
import it.tesi.prochilo.notifiche.Topic;
import it.tesi.prochilo.notifiche.util.Login;

public class MainMenuActivity extends AppCompatActivity {

    private List<Topic> topicsList;
    private ListView mListView;
    private Toast mSuccess;
    private BaseAdapter mAdapter;
    private ServerListener serverListener = new ServerListener() {
        @Override
        public void onSuccess() {
            mSuccess.show();
        }

        @Override
        public void onFailure() {
            Log.d("CO", "NON RIUSCITA");
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.get_layout);
        mSuccess = Toast.makeText(this, "Logout eseguito", Toast.LENGTH_LONG);
        topicsList = Login.getAPI().getTopics(serverListener);
        mListView = (ListView) findViewById(R.id.topic_list);
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
            case R.id.menuadd:
                intent = new Intent(this, PostFragment.class);
                startActivity(intent);
                break;
            case R.id.menudelete:
                intent = new Intent(this, DeleteActivity.class);
                startActivity(intent);
                break;
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
        final BaseAdapter adapter = new BaseAdapter() {
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
        return adapter;
    }

}