package gameonlp.oredepos.data.condition;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public class And extends Condition {


    private final JsonArray conditions;

    public And(Condition... conditions) {
        this.conditions = new JsonArray();
        for (Condition condition : conditions) {
            this.conditions.add(condition.get());
        }
    }

    @Override
    public JsonElement get() {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("type", "forge:and");
        jsonObject.add("values", conditions);
        return jsonObject;
    }
}
