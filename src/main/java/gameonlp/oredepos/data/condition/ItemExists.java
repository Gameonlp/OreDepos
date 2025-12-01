package gameonlp.oredepos.data.condition;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public class ItemExists extends Condition {
    private String item;

    public ItemExists(String item) {
        this.item = item;
    }

    @Override
    public JsonElement get() {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("type", "forge:item_exists");
        jsonObject.addProperty("item", item);
        return jsonObject;
    }
}
