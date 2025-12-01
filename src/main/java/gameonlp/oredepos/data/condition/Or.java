package gameonlp.oredepos.data.condition;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public class Or extends Condition {


    private final JsonArray conditions;

    public Or(Condition... conditions) {
        this.conditions = new JsonArray();
        for (Condition condition : conditions) {
            this.conditions.add(condition.get());
        }
    }

    @Override
    public JsonElement get() {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("type", "forge:or");
        jsonObject.add("values", conditions);
        return jsonObject;
    }
}
