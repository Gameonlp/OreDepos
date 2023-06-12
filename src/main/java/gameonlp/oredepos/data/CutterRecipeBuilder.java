package gameonlp.oredepos.data;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import gameonlp.oredepos.OreDepos;
import gameonlp.oredepos.RegistryManager;
import gameonlp.oredepos.data.condition.Condition;
import gameonlp.oredepos.data.condition.True;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementRewards;
import net.minecraft.advancements.CriterionTriggerInstance;
import net.minecraft.advancements.RequirementsStrategy;
import net.minecraft.advancements.critereon.RecipeUnlockedTrigger;
import net.minecraft.core.Registry;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.ItemLike;

import javax.annotation.Nullable;
import java.util.function.Consumer;

public class CutterRecipeBuilder implements RecipeBuilder {
    private final Ingredient ingredient;
    private final Item result;
    private int count;
    private int energy;
    private int ticks;
    private final Advancement.Builder advancement = Advancement.Builder.advancement();
    @Nullable
    private String group;
    private Condition condition;

    public CutterRecipeBuilder(Ingredient ingredient, ItemLike result) {
        this.ingredient = ingredient;
        this.result = result.asItem();
        this.count = 2;
        this.energy = 40;
        this.ticks = 20;
    }

    public static CutterRecipeBuilder cutter(Ingredient ingredient, ItemLike result) {
        return new CutterRecipeBuilder(ingredient, result);
    }

    public CutterRecipeBuilder count(int count) {
        this.count = count;
        return this;
    }

    public CutterRecipeBuilder energy(int energy) {
        this.energy = energy;
        return this;
    }

    public CutterRecipeBuilder ticks(int ticks) {
        this.ticks = ticks;
        return this;
    }

    public CutterRecipeBuilder condition(Condition condition) {
        this.condition = condition;
        return this;
    }

    public CutterRecipeBuilder unlockedBy(String p_126197_, CriterionTriggerInstance p_126198_) {
        this.advancement.addCriterion(p_126197_, p_126198_);
        return this;
    }

    public CutterRecipeBuilder group(@Nullable String p_126195_) {
        this.group = p_126195_;
        return this;
    }

    public Item getResult() {
        return this.result;
    }

    public void save(Consumer<FinishedRecipe> p_126205_, ResourceLocation p_126206_) {
        this.ensureValid(p_126206_);
        this.advancement.parent(new ResourceLocation("recipes/root")).addCriterion("has_the_recipe", RecipeUnlockedTrigger.unlocked(p_126206_)).rewards(AdvancementRewards.Builder.recipe(p_126206_)).requirements(RequirementsStrategy.OR);
        p_126205_.accept(new CutterRecipeBuilder.Result(p_126206_, this.result, this.count, this.condition, this.group == null ? "" : this.group, this.ingredient, this.energy, this.ticks, this.advancement, new ResourceLocation(p_126206_.getNamespace(), "recipes/" + this.result.getItemCategory().getRecipeFolderName() + "/" + p_126206_.getPath())));
    }

    private void ensureValid(ResourceLocation p_126208_) {
        if (this.advancement.getCriteria().isEmpty()) {
            throw new IllegalStateException("No way of obtaining recipe " + p_126208_);
        }
    }

    public Ingredient getInput() {
        return ingredient;
    }

    public static class Result implements FinishedRecipe {
        private final ResourceLocation id;
        private final Item result;
        private final int count;
        private final Condition condition;
        private final String group;
        private final Ingredient ingredient;
        private final int energy;
        private final int ticks;
        private final Advancement.Builder advancement;
        private final ResourceLocation advancementId;

        public Result(ResourceLocation resourceLocation, Item item, int count, Condition condition, String group, Ingredient ingredient, int energy, int ticks, Advancement.Builder p_126227_, ResourceLocation p_126228_) {
            this.id = resourceLocation;
            this.result = item;
            this.count = count;
            this.condition = condition;
            this.group = group;
            this.ingredient = ingredient;
            this.energy = energy;
            this.ticks = ticks;
            this.advancement = p_126227_;
            this.advancementId = p_126228_;
        }

        public void serializeRecipeData(JsonObject p_126230_) {
            if (!this.group.isEmpty()) {
                p_126230_.addProperty("group", this.group);
            }
            p_126230_.addProperty("type", "forge:conditional");
            JsonArray recipes = new JsonArray();
            p_126230_.add("recipes", recipes);
            JsonObject holder = new JsonObject();
            recipes.add(holder);

            JsonArray conditions = new JsonArray();
            conditions.add(new True().get());
            if (condition != null) {
                conditions.add(condition.get());
            }
            holder.add("conditions", conditions);

            JsonObject recipe = new JsonObject();
            recipe.addProperty("type", OreDepos.MODID + ":cutter_recipe");

            JsonArray jsonarray = new JsonArray();
            jsonarray.add(ingredient.toJson());

            recipe.add("input", jsonarray);
            JsonObject jsonobject = new JsonObject();
            jsonobject.addProperty("item", Registry.ITEM.getKey(this.result).toString());
            jsonobject.addProperty("count", count);

            recipe.add("result", jsonobject);
            recipe.add("energy", new JsonPrimitive(energy));
            recipe.add("ticks", new JsonPrimitive(ticks));
            holder.add("recipe", recipe);
        }

        public RecipeSerializer<?> getType() {
            return RegistryManager.CUTTER_RECIPE_SERIALIZER;
        }

        public ResourceLocation getId() {
            return this.id;
        }

        @Nullable
        public JsonObject serializeAdvancement() {
            return this.advancement.serializeToJson();
        }

        @Nullable
        public ResourceLocation getAdvancementId() {
            return this.advancementId;
        }
    }
}
