package it.tesi.prochilo.notifiche;

public class Login {

    private final String mMail, mPassword, token;

    public Login(final String mail, final String password) {
        this.mMail = mail;
        this.mPassword = password;
        token = "token_admin";
    }

    public String getToken() {
        return token;
    }
}
