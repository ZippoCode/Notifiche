package it.tesi.prochilo.notifiche;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

public class CustomFirebaseInstanceIdService extends FirebaseInstanceIdService {

    private String mToken = null;
    private FirebaseInstanceId mFirebaseInstanceId;

    public CustomFirebaseInstanceIdService() {
        mFirebaseInstanceId = FirebaseInstanceId.getInstance();
        //mToken = mFirebaseInstanceId.getToken();
        mToken = mFirebaseInstanceId.getId();
    }

    public String getToken() {
        return mToken;
    }
}
