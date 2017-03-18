package it.tesi.prochilo.notifiche.util;

import it.tesi.prochilo.notifiche.API;

public class Login {

    private String user, password;
    private static API api = null;

    public Login(String user, String password) {
        this.user = user;
        this.password = password;
        api = new API("http://192.168.1.7:8080/topic", "77244763443", "token_admin");
    }

    public static API getAPI() {
        return api;
    }
}
