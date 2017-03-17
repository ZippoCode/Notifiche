package it.tesi.prochilo.notifiche;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.util.LinkedList;

import it.tesi.prochilo.notifiche.server.CustomFMS;
import it.tesi.prochilo.notifiche.ui.MainMenuActivity;

public class MainActivity extends AppCompatActivity {

    private Button loginButton;
    private EditText eMail, password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        CustomFMS fms = new CustomFMS("token_admin");
        loginButton = (Button) findViewById(R.id.login_button);
        eMail = (EditText) findViewById(R.id.editText_email);
        password = (EditText) findViewById(R.id.editText_password);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                eseguiRichieste(eMail.getText().toString(), password.getText().toString());
            }
        });
    }

    private void eseguiRichieste(String email, String password) {
        Intent intent = new Intent(this, MainMenuActivity.class);
        intent.putExtra("email", email);
        intent.putExtra("password", password);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }
}
