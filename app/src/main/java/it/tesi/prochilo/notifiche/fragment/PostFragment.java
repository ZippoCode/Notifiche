package it.tesi.prochilo.notifiche.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import java.util.LinkedList;
import java.util.List;

import it.tesi.prochilo.notifiche.server.CustomServerManagement;
import it.tesi.prochilo.notifiche.R;

public class PostFragment extends Fragment {

    private Button button;
    private EditText topic1, topic2, topic3;
    private ButtonActivity mActivity;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mActivity = (ButtonActivity) context;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View layout = inflater.inflate(R.layout.post_delete_layout, container, false);
        topic1 = (EditText)layout.findViewById(R.id.et_topic1);
        topic2 = (EditText)layout.findViewById(R.id.et_topic2);
        Button button = (Button) layout.findViewById(R.id.button_post_delete);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                List<String> list = new LinkedList<>();
                list.add(topic1.getText().toString());
                list.add(topic2.getText().toString());
                mActivity.operation1(list, CustomServerManagement.HttpMethod.POST);
            }
        });
        return layout;
    }
}
