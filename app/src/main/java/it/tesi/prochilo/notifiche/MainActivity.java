package it.tesi.prochilo.notifiche;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import it.tesi.prochilo.notifiche.activity.ManagementServerActivity;

public class MainActivity extends AppCompatActivity {

    private Button loginButton;
    private EditText eMail, password;
    private Login login = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        loginButton = (Button) findViewById(R.id.login_button);
        eMail = (EditText) findViewById(R.id.editText_email);
        password = (EditText) findViewById(R.id.editText_password);
        eMail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                eMail.setText("");
            }
        });
        password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                password.setText("");
            }
        });
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                login = new Login(eMail.getText().toString(), password.getText().toString());
                String token = login.getToken();
                eseguiRichieste(token);
            }
        });
    }

    private void eseguiRichieste(String token) {
        Intent intent = new Intent(this, ManagementServerActivity.class);
        intent.putExtra("token", token);
        startActivity(intent);
    }
}
