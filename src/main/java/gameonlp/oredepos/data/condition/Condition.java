package gameonlp.oredepos.data.condition;

import com.google.gson.JsonElement;

public abstract class Condition {
    protected Condition() {
    }

    public abstract JsonElement get();
}
