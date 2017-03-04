package it.tesi.prochilo.notifiche.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.LinkedList;
import java.util.List;

import it.tesi.prochilo.notifiche.R;
import it.tesi.prochilo.notifiche.Topic;

public class GetFragment extends Fragment {

    private ButtonActivity mButtonActivity;
    private List<Topic> topicsList;
    private ListView mListView;
    private BaseAdapter mAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mButtonActivity = (ButtonActivity) context;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View layout = inflater.inflate(R.layout.get_layout, container, false);
        topicsList = mButtonActivity.operation2();
        mListView = (ListView) layout.findViewById(R.id.topic_list);
        mAdapter = getCustomAdapter();
        mListView.setAdapter(mAdapter);
        return layout;
    }

    @Override
    public void onStart() {
        super.onStart();
        mAdapter.notifyDataSetChanged();
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
                    view = LayoutInflater.from(getContext())
                            .inflate(R.layout.topic_layout, null);
                }
                /** Da decommentare se si vogliono visualizzare tutti i componenti
                 ((TextView) view.findViewById(R.id.id)).setText(topic.id);
                 ((TextView) view.findViewById(R.id.userId)).setText(topic.userId);
                 ((TextView) view.findViewById(R.id.topic_name)).setText(topic.topic);
                 ((TextView) view.findViewById(R.id.timestamp)).setText(topic.timestamp);
                 **/
                ((TextView) view.findViewById(R.id.topic_name)).setText(getItem(i).topic);
                return view;
            }
        };
        return adapter;
    }
}