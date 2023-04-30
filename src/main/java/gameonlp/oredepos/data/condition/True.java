package gameonlp.oredepos.data.condition;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

public class True extends Condition {
    @Override
    public JsonElement get() {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("type", "forge:true");
        return jsonObject;
    }
}
