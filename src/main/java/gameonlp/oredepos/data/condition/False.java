package gameonlp.oredepos.data.condition;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public class False extends Condition {
    @Override
    public JsonElement get() {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("type", "forge:false");
        return jsonObject;
    }
}
