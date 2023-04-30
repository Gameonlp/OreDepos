package gameonlp.oredepos.data.condition;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public class ModLoaded extends Condition {
    private String modid;

    public ModLoaded(String modid) {
        this.modid = modid;
    }

    @Override
    public JsonElement get() {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("type", "forge:mod_loaded");
        jsonObject.addProperty("modid", modid);
        return jsonObject;
    }
}
