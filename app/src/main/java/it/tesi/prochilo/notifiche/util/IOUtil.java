package it.tesi.prochilo.notifiche.util;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.regex.Pattern;

public class IOUtil {

    private static final String REGULAREXPRESSION = "[a-zA-Z0-9-_.~%]{1,900}";

    public static String getString(InputStream inputStream) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = br.readLine()) != null) {
            sb.append(line).append('\n');
        }
        return sb.toString();
    }

    public static boolean regulatorTopic(String topic) {
        return Pattern.matches(REGULAREXPRESSION, topic);
    }
}
