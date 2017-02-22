package it.tesi.prochilo.notifiche;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();
    TextView tv;
    String testo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tv = (TextView) findViewById(R.id.testo);
        CustomFirebase customFirebase = new CustomFirebase();
        /*
        CustomFirebase.mAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    Log.d(TAG, "onAuthStateChanged:signed_in " + user.getUid());
                } else {
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                }
            }
        };
        customFirebase.loginUser(this);
        */
    }

    @Override
    protected void onStart() {
        super.onStart();
        ServerManagement serverManagement = new ServerManagement("http://192.168.1.4:8080/topic");
        ServerAsyncTask serverAsyncTask = new ServerAsyncTask(serverManagement);
        serverAsyncTask.execute("http://192.168.1.4:8080");
        //CustomFirebase.mAuth.addAuthStateListener(CustomFirebase.mAuthStateListener);
    }
    /*
    @Override
    protected void onStop() {
        super.onStop();
        if (CustomFirebase.mAuthStateListener != null) {
            CustomFirebase.mAuth.removeAuthStateListener(CustomFirebase.mAuthStateListener);
        }
    }*/
}
