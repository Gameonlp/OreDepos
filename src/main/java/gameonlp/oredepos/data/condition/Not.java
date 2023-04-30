package gameonlp.oredepos.data.condition;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public class Not extends Condition {

    private Condition condition;

    public Not(Condition condition) {
        this.condition = condition;
    }

    @Override
    public JsonElement get() {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("type", "forge:not");
        jsonObject.add("value", condition.get());
        return jsonObject;
    }
}
