package it.tesi.prochilo.notifiche;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import it.tesi.prochilo.notifiche.ui.MainMenuActivity;
import it.tesi.prochilo.notifiche.util.Login;

public class MainActivity extends AppCompatActivity {

    private Button loginButton;
    private EditText eMail, password;
    private Toast success, failure;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        loginButton = (Button) findViewById(R.id.login_button);
        eMail = (EditText) findViewById(R.id.editText_email);
        password = (EditText) findViewById(R.id.editText_password);
        success = Toast.makeText(this, "Accesso eseguito", Toast.LENGTH_SHORT);
        failure =  Toast.makeText(this, "Errore accesso", Toast.LENGTH_SHORT);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                eseguiRichieste(eMail.getText().toString(), password.getText().toString());
            }
        });
    }

    private void eseguiRichieste(String email, String password) {
        final Intent intent = new Intent(this, MainMenuActivity.class);
        Login login = new Login(email, password);
        login.getAPI().login(new ServerListener() {
            @Override
            public void onSuccess() {
                success.show();
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
            }

            @Override
            public void onFailure() {
                failure.show();
            }
        });

    }
}
