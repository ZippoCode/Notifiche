package it.tesi.prochilo.notifiche;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

public class CustomFirebase extends FirebaseInstanceIdService {

    public static String token;
    public static FirebaseAuth.AuthStateListener mAuthStateListener;
    public static FirebaseAuth mAuth;

    public CustomFirebase() {
        token = FirebaseInstanceId.getInstance().getToken();
        mAuth = FirebaseAuth.getInstance();
        System.out.println(token);
    }

    public void loginUser(Activity activity) {
        mAuth.signInWithEmailAndPassword("prova_email@mail.com", "prova_password")
                .addOnCompleteListener(activity, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(!task.isSuccessful()){
                            System.out.println("LOGIN FALLITO !");
                        }
                    }
                });
    }


}
