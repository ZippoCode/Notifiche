package it.tesi.prochilo.notifiche;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class ServerManagement {

    public enum HttpMethod {
        GET, POST, PUT, DELETE;
    }

    private static final String TAG = ServerManagement.class.getSimpleName();
    private static final String AUTHORIZATION = "Authorization";

    public ServerManagement() {
    }


    public void addUser(String idName, String token) {
        URL url;
        HttpURLConnection httpURLConnection;
        try {
            url = new URL("http://192.168.1.7:8080/user");
            httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod(HttpMethod.PUT.name());
            httpURLConnection.addRequestProperty(AUTHORIZATION, "Bearer " + token);
            httpURLConnection.addRequestProperty("Content-type", "application/json");
            httpURLConnection.setDoOutput(true);
            JSONArray array = new JSONArray();
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("userId", idName);
            jsonObject.put("token", token);
            array.put(jsonObject);
            OutputStream outputStream = httpURLConnection.getOutputStream();
            outputStream.write(array.toString().getBytes("UTF-8"));
            System.out.println(httpURLConnection.getResponseCode());
        } catch (IOException ioe) {
            ioe.printStackTrace();
        } catch (JSONException jsone) {
            jsone.printStackTrace();
        }
    }

    public void getToken(String idUser) {

    }

    public Map<String, List<String>> getTopics(String token) {
        HttpURLConnection httpURLConnection = null;
        Map<String, List<String>> ritorno = new HashMap<>();
        URL url;
        try {
            url = new URL("http://192.168.1.7:8080/topic");
            httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod(HttpMethod.GET.name());
            httpURLConnection.addRequestProperty("Content-Type", "application/json");
            httpURLConnection.addRequestProperty(AUTHORIZATION, "Bearer " + token);
            final int httpResponseCode = httpURLConnection.getResponseCode();
            InputStream inputStream = null;
            if (httpResponseCode >= HttpURLConnection.HTTP_OK
                    && httpResponseCode < HttpURLConnection.HTTP_MULT_CHOICE) {
                inputStream = httpURLConnection.getInputStream();
            } else {
                inputStream = httpURLConnection.getErrorStream();
            }
            final String httpResponseMessage = httpURLConnection.getResponseMessage();
            JSONArray response = new JSONArray(getString(inputStream));
            for (int i = 0; i < response.length(); i++) {
                JSONObject object = response.getJSONObject(i);
                List<String> list = new LinkedList<>();
                list.add(object.getString("id"));
                list.add(object.getString("userId"));
                list.add(object.getString("topic"));
                list.add(object.getString("timestamp"));
                ritorno.put("id" + i, list);
            }
        } catch (IOException ioe) {
            Log.d(TAG, "Errore apertura connessione");
            ioe.printStackTrace();
        } catch (JSONException jsone) {
            Log.d(TAG, "Error JSON");
            jsone.printStackTrace();
        }
        return ritorno;
    }

    public boolean postAndDeleteRequest(JSONArray jsonArray, String token, HttpMethod method) {
        HttpURLConnection httpURLConnection = null;
        URL url;
        OutputStream outputStream = null;
        try {
            url = new URL("http://192.168.1.7:8080/topic");
            httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod(method.name());
            httpURLConnection.addRequestProperty("Content-Type", "application/json");
            httpURLConnection.addRequestProperty(AUTHORIZATION, "Bearer " + token);
            httpURLConnection.setDoOutput(true);
            outputStream = httpURLConnection.getOutputStream();
            outputStream.write(jsonArray.toString().getBytes("UTF-8"));
            outputStream.flush();
            System.out.println(httpURLConnection.getResponseCode());
        } catch (IOException ioe) {
            Log.d(TAG, "Error open connection");
            ioe.printStackTrace();
        } finally {
            if (httpURLConnection != null) {
                httpURLConnection.disconnect();
            }
        }
        return true;
    }

    private String getString(InputStream inputStream) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = br.readLine()) != null) {
            sb.append(line).append('\n');
        }
        return sb.toString();
    }

}
