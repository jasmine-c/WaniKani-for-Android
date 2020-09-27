package tr.xip.wanikani.models.v2.subjects;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import org.apache.commons.lang3.NotImplementedException;
import org.joda.time.DateTime;

import java.io.Serializable;

import tr.xip.wanikani.models.v2.Resource;

public abstract class Subject extends Resource<SubjectData> implements Serializable {

    public Subject(int id, String object, String url, DateTime data_updated_at, SubjectData data) {
        super(id, object, url, data_updated_at, data);
    }

    public static Subject ParseSubject(JsonElement json, JsonDeserializationContext context) {
        JsonObject jsonObject = json.getAsJsonObject();
        String subjectType = jsonObject.get("object").getAsString();
        int id = jsonObject.get("id").getAsInt();
        String url = jsonObject.get("url").getAsString();
        DateTime updateTime = context.deserialize(jsonObject.get("data_updated_at"), DateTime.class);

        switch(subjectType) {
            case "radical":
                return new Radical(id, subjectType, url, updateTime, (RadicalData) context.deserialize(json, RadicalData.class));
            case "kanji":
                return new Kanji(id, subjectType, url, updateTime, (KanjiData) context.deserialize(json, KanjiData.class));
            case "vocabulary":
                return new Vocabulary(id, subjectType, url, updateTime, (VocabularyData) context.deserialize(json, VocabularyData.class));
        }

        throw new JsonParseException("Unknown subject type " + subjectType);
    }

    @Override
    public void save() {
        throw new NotImplementedException("save not implemented");
    }
}
