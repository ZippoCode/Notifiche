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

    public static List<String> getTopics(InputStream inputStream) throws JSONException {
        List<String> topics_list = new LinkedList<>();
            /*
            final String application = jsonObject.getString(Parameters.application.name());
            final String authorizedEntity = jsonObject.getString(Parameters.authorizedEntity.name());
            final String platform = jsonObject.getString(Parameters.platform.name());
            final String attestStatus = jsonObject.getString(Parameters.attestStatus.name());
            final String connectionType = jsonObject.getString(Parameters.connectionType.name());
            final String connectDate = jsonObject.getString(Parameters.platform.name());
            final JSONObject rel = jsonObject.getJSONObject(Parameters.rel.name());
            */
        //Convertive l'inputStream in una stringa
        final String contenuto = null;
        final JSONObject mJSONObject = new JSONObject(contenuto);
        JSONObject topics = mJSONObject.getJSONObject("topics");
        /*
        JSONObject topic = null;
        int i = 1;
        do {
            //Da implementare correttamente i nomi "topicname" e "nameTopic"
            topic = topics.getJSONObject("topicname" + i);
            if (topic != null) {
                topics_list.add(topic.getString("nameTopic"));
            }
        } while (topic != null);*/
        return topics_list;
    }


    public JSONObject createPostRequest(String nameTopic, List<String> topics) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(ParametersPost.to.name(), nameTopic);
            JSONArray arrayTopics = new JSONArray();
            Iterator<String> it = topics.iterator();
            while(it.hasNext()){
                String topic = it.next();
                arrayTopics.put(topic);
            }
            jsonObject.put(ParametersPost.registration_tokens.name(), arrayTopics);
        }catch (JSONException jsone){

        }
        return jsonObject;
    }
}
