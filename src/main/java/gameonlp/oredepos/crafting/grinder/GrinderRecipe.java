package gameonlp.oredepos.crafting.grinder;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import gameonlp.oredepos.OreDepos;
import gameonlp.oredepos.RegistryManager;
import gameonlp.oredepos.crafting.FluidIngredient;
import gameonlp.oredepos.crafting.IBaseRecipe;
import gameonlp.oredepos.crafting.IFluidInventory;
import net.minecraft.core.NonNullList;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.ShapedRecipe;
import net.minecraft.world.level.Level;
import net.minecraftforge.fluids.FluidStack;

import javax.annotation.Nullable;

public class GrinderRecipe implements IBaseRecipe {
    public static final ResourceLocation TYPE = new ResourceLocation(OreDepos.MODID, "grinder_recipe");
    private final ResourceLocation id;
    private final NonNullList<Ingredient> ingredients;
    private final ItemStack outItem;
    private final int energy;
    private final int ticks;

    public GrinderRecipe(ResourceLocation id, NonNullList<Ingredient> ingredients, ItemStack outItem, int energy, int ticks) {
        this.id = id;
        this.ingredients = ingredients;
        this.outItem = outItem;
        this.energy = energy;
        this.ticks = ticks;
    }

    // Only checking one slot for one ingredient, not perfect, not too bad
    @Override
    public boolean matches(IFluidInventory fluidInventory, Level p_77569_2_) {
        boolean matches = true;
        for (Ingredient ingredient : ingredients) {
            boolean found = false;
            for (int i = 0; i < fluidInventory.getContainerSize(); i++) {
                found |= ingredient.test(fluidInventory.getItem(i));
            }
            matches &= found;
        }
        return matches;
    }

    @Override
    public ItemStack assemble(IFluidInventory p_77572_1_) {
        return null;
    }

    @Override
    public boolean canCraftInDimensions(int p_194133_1_, int p_194133_2_) {
        return true;
    }

    @Override
    public ItemStack getResultItem() {
        return outItem.copy();
    }

    @Override
    public FluidStack getResultFluid() {
        return null;
    }

    @Override
    public NonNullList<FluidIngredient> getFluidIngredients() {
        return null;
    }

    @Override
    public ResourceLocation getId() {
        return id;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return RegistryManager.GRINDER_RECIPE_SERIALIZER;
    }

    @Override
    public RecipeType<?> getType() {
        return RegistryManager.GRINDER_RECIPE_TYPE.get();
    }

    public int getEnergy() {
        return energy;
    }

    @Override
    public NonNullList<Ingredient> getIngredients() {
        return ingredients;
    }

    public int getTicks() {
        return ticks;
    }

    public static class GrinderRecipeType implements RecipeType<GrinderRecipe> {
        @Override
        public String toString() {
            return GrinderRecipe.TYPE.toString();
        }
    }

    public static class Serializer
            implements RecipeSerializer<GrinderRecipe> {

        @Override
        public GrinderRecipe fromJson(ResourceLocation p_199425_1_, JsonObject json) {
            ItemStack itemStack = ItemStack.EMPTY;
            JsonObject results = json.getAsJsonObject("result");
            itemStack = new ItemStack(ShapedRecipe.itemFromJson(results),
                    results.getAsJsonPrimitive("count").getAsInt());
            NonNullList<Ingredient> ingredients = NonNullList.create();
            JsonArray inputs = json.getAsJsonArray("input");
            for (JsonElement input : inputs) {
                JsonObject inputObject = input.getAsJsonObject();
                if (inputObject.has("item") || inputObject.has("tag")){
                    ingredients.add(Ingredient.fromJson(inputObject));
                } else {
                    throw new JsonParseException("Not an item " + inputObject);
                }
            }
            if (ingredients.size() > 1){
                throw new JsonParseException("Too many input items");
            }
            return new GrinderRecipe(p_199425_1_, ingredients, itemStack, json.get("energy").getAsInt(), json.get("ticks").getAsInt());
        }

        @Nullable
        @Override
        public GrinderRecipe fromNetwork(ResourceLocation p_199426_1_, FriendlyByteBuf buffer) {
            ItemStack outStack = buffer.readItem();
            NonNullList<Ingredient> ingredients = NonNullList.create();
            while (buffer.readBoolean()){
                ingredients.add(Ingredient.fromNetwork(buffer));
            }
            return new GrinderRecipe(p_199426_1_, ingredients, outStack, buffer.readInt(), buffer.readInt());
        }

        @Override
        public void toNetwork(FriendlyByteBuf buffer, GrinderRecipe recipe) {
            buffer.writeItemStack(recipe.outItem, true);
            buffer.writeBoolean(false);
            buffer.writeInt(recipe.energy);
            buffer.writeInt(recipe.ticks);
        }
    }
}
