package gameonlp.oredepos.data;

import gameonlp.oredepos.OreDepos;
import gameonlp.oredepos.RegistryManager;
import net.minecraft.advancements.critereon.InventoryChangeTrigger;
import net.minecraft.advancements.critereon.ItemPredicate;
import net.minecraft.data.*;
import net.minecraft.data.recipes.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.block.Block;

import java.util.function.Consumer;


public class OreDepositsRecipeProvider extends RecipeProvider {
    public OreDepositsRecipeProvider(DataGenerator p_i48262_1_) {
        super(p_i48262_1_);
    }

    private void depositRecipes(Consumer<FinishedRecipe> consumer, Item item, Block storage, TagKey<Item> tag, TagKey<Item> ores, Item raw, Block rawStorage, TagKey<Item> deepslateTag){
        SimpleCookingRecipeBuilder.smelting(Ingredient.of(ores), item, 1.4f, 200)
                .unlockedBy("has_items", InventoryChangeTrigger.TriggerInstance.hasItems(ItemPredicate.Builder.item().of(ores).build()))
                .save(consumer);
        ShapelessRecipeBuilder.shapeless(item, 9)
                .requires(storage.asItem())
                .unlockedBy("has_items", InventoryChangeTrigger.TriggerInstance.hasItems(storage))
                .save(consumer, new ResourceLocation(OreDepos.MODID, item.getRegistryName().getPath() + "_from_" + storage.getRegistryName().getPath()));
        ShapedRecipeBuilder.shaped(storage)
                .pattern("AAA")
                .pattern("AAA")
                .pattern("AAA")
                .define('A', Ingredient.of(tag))
                .unlockedBy("has_items", InventoryChangeTrigger.TriggerInstance.hasItems(ItemPredicate.Builder.item().of(tag).build()))
                .save(consumer);
        SimpleCookingRecipeBuilder.smelting(Ingredient.of(raw), item, 0.7f, 200)
                .unlockedBy("has_items", InventoryChangeTrigger.TriggerInstance.hasItems(raw))
                .save(consumer, new ResourceLocation(OreDepos.MODID, item.getRegistryName().getPath() + "_from_" + raw.getRegistryName().getPath()));
        ShapelessRecipeBuilder.shapeless(raw, 9)
                .requires(rawStorage.asItem())
                .unlockedBy("has_items", InventoryChangeTrigger.TriggerInstance.hasItems(rawStorage))
                .save(consumer, new ResourceLocation(OreDepos.MODID, raw.getRegistryName().getPath() + "_from_" + rawStorage.getRegistryName().getPath()));
        ShapedRecipeBuilder.shaped(rawStorage)
                .pattern("AAA")
                .pattern("AAA")
                .pattern("AAA")
                .define('A', Ingredient.of(deepslateTag))
                .unlockedBy("has_items", InventoryChangeTrigger.TriggerInstance.hasItems(ItemPredicate.Builder.item().of(deepslateTag).build()))
                .save(consumer);
    }

    @Override
    protected void buildCraftingRecipes(Consumer<FinishedRecipe> consumer) {
        depositRecipes(consumer, RegistryManager.OSMIUM_INGOT, RegistryManager.OSMIUM_BLOCK, TagProvider.INGOTS_OSMIUM, TagProvider.ORE_OSMIUM_ITEM, RegistryManager.RAW_OSMIUM, RegistryManager.RAW_OSMIUM_BLOCK, TagProvider.RAW_OSMIUM);

        depositRecipes(consumer, RegistryManager.ARDITE_INGOT, RegistryManager.ARDITE_BLOCK, TagProvider.INGOTS_ARDITE, TagProvider.ORE_ARDITE_ITEM, RegistryManager.RAW_ARDITE, RegistryManager.RAW_ARDITE_BLOCK, TagProvider.RAW_ARDITE);

        depositRecipes(consumer, RegistryManager.COBALT_INGOT, RegistryManager.COBALT_BLOCK, TagProvider.INGOTS_COBALT, TagProvider.ORE_COBALT_ITEM, RegistryManager.RAW_COBALT, RegistryManager.RAW_COBALT_BLOCK, TagProvider.RAW_COBALT);

        depositRecipes(consumer, RegistryManager.PLATINUM_INGOT, RegistryManager.PLATINUM_BLOCK, TagProvider.INGOTS_PLATINUM, TagProvider.ORE_PLATINUM_ITEM, RegistryManager.RAW_PLATINUM, RegistryManager.RAW_PLATINUM_BLOCK, TagProvider.RAW_PLATINUM);
    }
}
