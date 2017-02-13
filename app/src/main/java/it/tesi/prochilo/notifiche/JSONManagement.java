package it.tesi.prochilo.notifiche;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

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

    public JSONObject createPutRequest(){
        return null;
    }

    public JSONObject createGetRequest(){
        return null;
    }

    public JSONObject createDeleteRequest(){
     return null;
    }

    public List<String> getTopics(String getResult) {
        List<String> topics_list = new LinkedList<>();
        try {
            final JSONObject mJSONObject = new JSONObject(getResult);
            JSONObject topics = mJSONObject.getJSONObject("topics");
            JSONObject topic = null;
            int i = 1;
            do {
                topic = topics.getJSONObject("topicname1");
                if (topic != null) {
                    topics_list.add(topic.getString("addDate"));
                }
            } while (topic != null);
        } catch (JSONException jsone) {

        }
        return topics_list;
    }
}
