package gameonlp.oredepos.crafting;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import gameonlp.oredepos.OreDepos;
import gameonlp.oredepos.RegistryManager;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.item.crafting.ShapedRecipe;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.nbt.JsonToNBT;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tags.FluidTags;
import net.minecraft.tags.Tag;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.datafix.fixes.AbstractUUIDFix;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.ForgeRegistryEntry;

import javax.annotation.Nullable;
import java.util.LinkedList;
import java.util.List;

public class ChemicalPlantRecipe implements IBaseRecipe{
    public static final ResourceLocation TYPE = new ResourceLocation(OreDepos.MODID, "chemical_recipe");
    private final ResourceLocation id;
    private final NonNullList<Ingredient> ingredients;
    private final NonNullList<FluidIngredient> fluidIngredients;
    private final FluidStack outFluid;
    private final ItemStack outItem;
    private final int energy;
    private final int ticks;

    public ChemicalPlantRecipe(ResourceLocation id, NonNullList<Ingredient> ingredients, NonNullList<FluidIngredient> fluidIngredients, FluidStack outFluid, ItemStack outItem, int energy, int ticks) {
        this.id = id;
        this.ingredients = ingredients;
        this.fluidIngredients = fluidIngredients;
        this.outFluid = outFluid;
        this.outItem = outItem;
        this.energy = energy;
        this.ticks = ticks;
    }

    // Only checking one slot for one ingredient, not perfect, not too bad
    @Override
    public boolean matches(IFluidInventory fluidInventory, World p_77569_2_) {
        boolean matches;
        if (fluidIngredients.size() == 1){
            matches = fluidIngredients.get(0).test(fluidInventory.getFluid(0)) || fluidIngredients.get(0).test(fluidInventory.getFluid(1));
        } else {
            matches = fluidIngredients.get(0).test(fluidInventory.getFluid(0));
            matches &= fluidIngredients.get(1).test(fluidInventory.getFluid(1));
        }
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
        return outFluid.copy();
    }

    @Override
    public NonNullList<FluidIngredient> getFluidIngredients() {
        return fluidIngredients;
    }

    @Override
    public ResourceLocation getId() {
        return id;
    }

    @Override
    public IRecipeSerializer<?> getSerializer() {
        return RegistryManager.CHEMICAL_PLANT_RECIPE_SERIALIZER;
    }

    @Override
    public IRecipeType<?> getType() {
        return Registry.RECIPE_TYPE.getOptional(TYPE).get();
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

    public static class ChemicalPlantRecipeType implements IRecipeType<ChemicalPlantRecipe> {
        @Override
        public String toString() {
            return ChemicalPlantRecipe.TYPE.toString();
        }
    }

    public static class Serializer extends ForgeRegistryEntry<IRecipeSerializer<?>>
            implements IRecipeSerializer<ChemicalPlantRecipe> {

        @Override
        public ChemicalPlantRecipe fromJson(ResourceLocation p_199425_1_, JsonObject json) {
            FluidStack fluidStack = FluidStack.EMPTY;
            ItemStack itemStack = ItemStack.EMPTY;
            JsonObject results = json.getAsJsonObject("results");
            if (results.has("fluid")) {
                JsonObject fluid = results.getAsJsonObject("fluid");
                ResourceLocation location = new ResourceLocation(fluid.get("name").getAsString());
                if (!fluid.has("nbt")) {
                    fluidStack = new FluidStack(ForgeRegistries.FLUIDS.getValue(location), fluid.get("amount").getAsInt());
                } else {
                    try {
                        CompoundNBT nbt = JsonToNBT.parseTag(fluid.get("nbt").getAsString());
                        fluidStack = new FluidStack(ForgeRegistries.FLUIDS.getValue(location), fluid.get("amount").getAsInt(), nbt);
                    } catch (CommandSyntaxException e) {
                        throw new JsonParseException(e);
                    }
                }
            }
            if (results.has("result_item")) {
                itemStack = ShapedRecipe.itemFromJson(results.getAsJsonObject("result_item"));
            }
            if (itemStack == null && fluidStack == null){
                throw new JsonParseException("No outputs");
            }
            NonNullList<Ingredient> ingredients = NonNullList.create();
            JsonArray inputs = json.getAsJsonArray("inputs");
            for (JsonElement input : inputs) {
                JsonObject inputObject = input.getAsJsonObject();
                if (inputObject.has("item") || inputObject.has("tag")){
                    ingredients.add(Ingredient.fromJson(inputObject));
                } else {
                    throw new JsonParseException("Not an item " + inputObject);
                }
            }
            if (ingredients.size() > 2){
                throw new JsonParseException("Too many input items");
            }
            NonNullList<FluidIngredient> fluidIngredients = NonNullList.create();
            JsonArray fluidInputs = json.getAsJsonArray("fluid_inputs");
            for (JsonElement input : fluidInputs) {
                JsonObject inputObject = input.getAsJsonObject();
                if (results.has("fluid")) {
                    JsonObject fluid = inputObject.getAsJsonObject("fluid");
                    ResourceLocation location = new ResourceLocation(fluid.get("tag").getAsString());
                    if (!fluid.has("nbt")) {
                        fluidIngredients.add(new FluidIngredient(location, fluid.get("amount").getAsInt(), null));
                    } else {
                        try {
                            CompoundNBT nbt = JsonToNBT.parseTag(fluid.get("nbt").getAsString());
                            fluidIngredients.add(new FluidIngredient(location, fluid.get("amount").getAsInt(), nbt));
                        } catch (CommandSyntaxException e) {
                            throw new JsonParseException(e);
                        }
                    }
                }
            }
            if (ingredients.size() > 2){
                throw new JsonParseException("Too many input fluids");
            } else if (ingredients.size() < 1){
                throw new JsonParseException("Too few input fluids");
            }

            return new ChemicalPlantRecipe(p_199425_1_, ingredients, fluidIngredients, fluidStack, itemStack, json.get("energy").getAsInt(), json.get("ticks").getAsInt());
        }

        @Nullable
        @Override
        public ChemicalPlantRecipe fromNetwork(ResourceLocation p_199426_1_, PacketBuffer buffer) {
            ItemStack outStack = buffer.readItem();
            FluidStack outFluid = FluidStack.loadFluidStackFromNBT(buffer.readNbt());
            NonNullList<Ingredient> ingredients = NonNullList.create();
            while (buffer.readBoolean()){
                ingredients.add(Ingredient.fromNetwork(buffer));
            }
            NonNullList<FluidIngredient> fluidIngredients = NonNullList.create();
            while (buffer.readBoolean()){
                fluidIngredients.add(FluidIngredient.fromNetwork(buffer));
            }
            return new ChemicalPlantRecipe(p_199426_1_, ingredients, fluidIngredients, outFluid, outStack, buffer.readInt(), buffer.readInt());
        }

        @Override
        public void toNetwork(PacketBuffer buffer, ChemicalPlantRecipe recipe) {
            buffer.writeItemStack(recipe.outItem, true);
            CompoundNBT fluid = new CompoundNBT();
            fluid = recipe.outFluid.writeToNBT(fluid);
            buffer.writeNbt(fluid);
            for (Ingredient ingredient : recipe.ingredients) {
                buffer.writeBoolean(true);
                ingredient.toNetwork(buffer);
            }
            buffer.writeBoolean(false);
            for (FluidIngredient fluidIngredient : recipe.fluidIngredients) {
                buffer.writeBoolean(true);
                fluidIngredient.toNetwork(buffer);
            }
            buffer.writeBoolean(false);
            buffer.writeInt(recipe.energy);
            buffer.writeInt(recipe.ticks);
        }
    }
}
