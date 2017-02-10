package it.tesi.prochilo.notifiche;

import com.google.firebase.iid.FirebaseInstanceId;

public class CustomFirebaseInstanceIdService {

    private String mToken = null;
    private FirebaseInstanceId mFirebaseInstanceId;

    public CustomFirebaseInstanceIdService() {
        mToken = mFirebaseInstanceId.getToken();
    }

    public String getToken(){
        return mToken;
    }
}
