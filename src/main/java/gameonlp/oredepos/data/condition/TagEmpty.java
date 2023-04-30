package gameonlp.oredepos.data.condition;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public class TagEmpty extends Condition {
    private String tag;

    public TagEmpty(String tag) {
        this.tag = tag;
    }

    @Override
    public JsonElement get() {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("type", "forge:tag_empty");
        jsonObject.addProperty("tag", tag);
        return jsonObject;
    }
}
