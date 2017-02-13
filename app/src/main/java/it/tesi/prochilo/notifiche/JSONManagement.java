package it.tesi.prochilo.notifiche;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class JSONManagement {

    public enum ParametersGet {
        application, authorizedEntity,
        applicationVersion, appSigner,
        attestStatus, platform, connectionType, connectDate, rel;
    }

    public enum ParametersPost {
        to, registration_tokens;
    }

    public JSONObject createPostRequest(String nameTopic, List<String> topics) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(ParametersPost.to.name(), nameTopic);
            JSONArray arrayTopics = new JSONArray();
            Iterator<String> it = topics.iterator();
            while (it.hasNext()) {
                String topic = it.next();
                arrayTopics.put(topic);
            }
            jsonObject.put(ParametersPost.registration_tokens.name(), arrayTopics);
        } catch (JSONException jsone) {

        }
        return jsonObject;
    }

    public JSONObject createPutRequest() {
        return null;
    }

    public JSONObject createGetRequest() {
        return null;
    }

    public JSONObject createDeleteRequest() {
        return null;
    }

    public Map<String, String> getTopics(JSONArray mJSONArray) {
        Map<String, String> topics = new HashMap<>();
        try {
            int i = 0;
            JSONObject oggetto = mJSONArray.getJSONObject(i);
            while (oggetto != null) {
                String nameTopic = oggetto.getString("name");
                String dateTopic = oggetto.getString("date");
                topics.put(nameTopic, dateTopic);
                i++;
                oggetto = mJSONArray.getJSONObject(i);
            }
        } catch (Exception jsone) {

        }
        return topics;
    }
}
