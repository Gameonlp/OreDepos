package gameonlp.oredepos.crafting.smelter;

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
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.registries.ForgeRegistryEntry;

import javax.annotation.Nullable;

public class SmelterRecipe implements IBaseRecipe {
    public static final ResourceLocation TYPE = new ResourceLocation(OreDepos.MODID, "smelter_recipe");
    private final ResourceLocation id;
    private final NonNullList<Ingredient> ingredients;
    private final ItemStack outItem;
    private final int energy;
    private final float ticks;

    public SmelterRecipe(ResourceLocation id, NonNullList<Ingredient> ingredients, ItemStack outItem, int energy, float ticks) {
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
        return RegistryManager.SMELTER_RECIPE_SERIALIZER;
    }

    @Override
    public RecipeType<?> getType() {
        return RegistryManager.SMELTER_RECIPE_TYPE.get();
    }

    public int getEnergy() {
        return energy;
    }

    @Override
    public NonNullList<Ingredient> getIngredients() {
        return ingredients;
    }

    public float getTicks() {
        return ticks;
    }

    public static class SmelterRecipeType implements RecipeType<SmelterRecipe> {
        @Override
        public String toString() {
            return SmelterRecipe.TYPE.toString();
        }
    }

    public static class Serializer extends ForgeRegistryEntry<RecipeSerializer<?>>
            implements RecipeSerializer<SmelterRecipe> {

        @Override
        public SmelterRecipe fromJson(ResourceLocation p_199425_1_, JsonObject json) {
            JsonObject results = json.getAsJsonObject("result");
            ItemStack itemStack = new ItemStack(ShapedRecipe.itemFromJson(results));
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
            return new SmelterRecipe(p_199425_1_, ingredients, itemStack, json.get("energy").getAsInt(), json.get("ticks").getAsInt());
        }

        @Nullable
        @Override
        public SmelterRecipe fromNetwork(ResourceLocation p_199426_1_, FriendlyByteBuf buffer) {
            ItemStack outStack = buffer.readItem();
            NonNullList<Ingredient> ingredients = NonNullList.create();
            while (buffer.readBoolean()){
                ingredients.add(Ingredient.fromNetwork(buffer));
            }
            return new SmelterRecipe(p_199426_1_, ingredients, outStack, buffer.readInt(), buffer.readFloat());
        }

        @Override
        public void toNetwork(FriendlyByteBuf buffer, SmelterRecipe recipe) {
            buffer.writeItemStack(recipe.outItem, true);
            buffer.writeBoolean(false);
            buffer.writeInt(recipe.energy);
            buffer.writeFloat(recipe.ticks);
        }
    }
}
