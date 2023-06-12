package gameonlp.oredepos.data;

import gameonlp.oredepos.OreDepos;
import gameonlp.oredepos.RegistryManager;
import net.minecraft.advancements.critereon.InventoryChangeTrigger;
import net.minecraft.advancements.critereon.ItemPredicate;
import net.minecraft.data.*;
import net.minecraft.data.recipes.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;

import java.util.Arrays;
import java.util.function.Consumer;


public class OreDepositsRecipeProvider extends RecipeProvider {
    public OreDepositsRecipeProvider(DataGenerator p_i48262_1_) {
        super(p_i48262_1_);
    }

    private void depositRecipes(Consumer<FinishedRecipe> consumer, Item item, Block storage, TagKey<Item> tag, TagKey<Item> ores, Item raw, Block rawStorage, TagKey<Item> deepslateTag, Item dust){
        SimpleCookingRecipeBuilder.smelting(Ingredient.of(ores), item, 1.4f, 200)
                .unlockedBy("has_items", InventoryChangeTrigger.TriggerInstance.hasItems(ItemPredicate.Builder.item().of(ores).build()))
                .save(consumer);
        SimpleCookingRecipeBuilder.blasting(Ingredient.of(ores), item, 1.4f, 100)
                .unlockedBy("has_items", InventoryChangeTrigger.TriggerInstance.hasItems(ItemPredicate.Builder.item().of(ores).build()))
                .save(consumer, new ResourceLocation(OreDepos.MODID, "blasting_" + item.getRegistryName().getPath()));
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
        SimpleCookingRecipeBuilder.blasting(Ingredient.of(raw), item, 0.7f, 100)
                .unlockedBy("has_items", InventoryChangeTrigger.TriggerInstance.hasItems(raw))
                .save(consumer, new ResourceLocation(OreDepos.MODID, "blasting_" + item.getRegistryName().getPath() + "_from_" + raw.getRegistryName().getPath()));
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
        basicGrinderRecipe(consumer, GrinderRecipeBuilder.grinder(Ingredient.of(rawStorage), dust)
                .count(12), "_from_raw_block");
        basicGrinderRecipe(consumer, GrinderRecipeBuilder.grinder(Ingredient.of(raw), dust)
                .count(1), "_from_raw");
        basicGrinderRecipe(consumer, GrinderRecipeBuilder.grinder(Ingredient.of(tag), dust)
                .count(1), "_from_ingots");
        basicGrinderRecipe(consumer, GrinderRecipeBuilder.grinder(Ingredient.of(ores), dust)
                .count(1), "_from_ores");
        SimpleCookingRecipeBuilder.smelting(Ingredient.of(dust), item, 0.7f, 100)
                .unlockedBy("has_items", InventoryChangeTrigger.TriggerInstance.hasItems(dust))
                .save(consumer, new ResourceLocation(OreDepos.MODID, "smelting_" + item.getRegistryName().getPath() + "_from_" + dust.getRegistryName().getPath()));
        SimpleCookingRecipeBuilder.blasting(Ingredient.of(dust), item, 0.7f, 50)
                .unlockedBy("has_items", InventoryChangeTrigger.TriggerInstance.hasItems(dust))
                .save(consumer, new ResourceLocation(OreDepos.MODID, "blasting_" + item.getRegistryName().getPath() + "_from_" + dust.getRegistryName().getPath()));
//        basicGrinderRecipe(consumer, GrinderRecipeBuilder.grinder(Ingredient.of(deepslateTag), dust)
//                .count(1), "_from_");
    }

    protected void basicGrinderRecipe(Consumer<FinishedRecipe> consumer, GrinderRecipeBuilder recipeBuilder) {
        basicGrinderRecipe(consumer, recipeBuilder, "");
    }

    protected void basicGrinderRecipe(Consumer<FinishedRecipe> consumer, GrinderRecipeBuilder recipeBuilder, String suffix){
        ItemStack[] stacks = recipeBuilder
                .getInput()
                .getItems();
        ItemLike[] items = new ItemLike[stacks.length];
        for (int i = 0; i < stacks.length; i++) {
            items[i] = stacks[i].getItem();
        }
        recipeBuilder.unlockedBy("has_items", InventoryChangeTrigger.TriggerInstance.hasItems(ItemPredicate.Builder.item().of(items).build()))
                .save(consumer, new ResourceLocation(OreDepos.MODID, "grinding_" + recipeBuilder.getResult().getRegistryName().getPath() + suffix));
    }

    protected void basicSmelterRecipe(Consumer<FinishedRecipe> consumer, SmelterRecipeBuilder recipeBuilder){
        ItemStack[] stacks = recipeBuilder
                .getInput()
                .getItems();
        ItemLike[] items = new ItemLike[stacks.length];
        for (int i = 0; i < stacks.length; i++) {
            items[i] = stacks[i].getItem();
        }
        recipeBuilder.unlockedBy("has_items", InventoryChangeTrigger.TriggerInstance.hasItems(ItemPredicate.Builder.item().of(items).build()))
                .save(consumer, new ResourceLocation(OreDepos.MODID, "smelting_" + recipeBuilder.getResult().getRegistryName().getPath()));
    }

    @Override
    protected void buildCraftingRecipes(Consumer<FinishedRecipe> consumer) {
        depositRecipes(consumer, RegistryManager.ZINC_INGOT, RegistryManager.ZINC_BLOCK, TagProvider.INGOTS_ZINC, TagProvider.ORE_ZINC_ITEM, RegistryManager.RAW_ZINC, RegistryManager.RAW_ZINC_BLOCK, TagProvider.RAW_ZINC, RegistryManager.ZINC_DUST);
        depositRecipes(consumer, RegistryManager.SILVER_INGOT, RegistryManager.SILVER_BLOCK, TagProvider.INGOTS_SILVER, TagProvider.ORE_SILVER_ITEM, RegistryManager.RAW_SILVER, RegistryManager.RAW_SILVER_BLOCK, TagProvider.RAW_SILVER, RegistryManager.SILVER_DUST);
        depositRecipes(consumer, RegistryManager.LEAD_INGOT, RegistryManager.LEAD_BLOCK, TagProvider.INGOTS_LEAD, TagProvider.ORE_LEAD_ITEM, RegistryManager.RAW_LEAD, RegistryManager.RAW_LEAD_BLOCK, TagProvider.RAW_LEAD, RegistryManager.LEAD_DUST);
        depositRecipes(consumer, RegistryManager.URANIUM_INGOT, RegistryManager.URANIUM_BLOCK, TagProvider.INGOTS_URANIUM, TagProvider.ORE_URANIUM_ITEM, RegistryManager.RAW_URANIUM, RegistryManager.RAW_URANIUM_BLOCK, TagProvider.RAW_URANIUM, RegistryManager.URANIUM_DUST);
        depositRecipes(consumer, RegistryManager.TIN_INGOT, RegistryManager.TIN_BLOCK, TagProvider.INGOTS_TIN, TagProvider.ORE_TIN_ITEM, RegistryManager.RAW_TIN, RegistryManager.RAW_TIN_BLOCK, TagProvider.RAW_TIN, RegistryManager.TIN_DUST);
        depositRecipes(consumer, RegistryManager.NICKEL_INGOT, RegistryManager.NICKEL_BLOCK, TagProvider.INGOTS_NICKEL, TagProvider.ORE_NICKEL_ITEM, RegistryManager.RAW_NICKEL, RegistryManager.RAW_NICKEL_BLOCK, TagProvider.RAW_NICKEL, RegistryManager.NICKEL_DUST);
        depositRecipes(consumer, RegistryManager.ALUMINUM_INGOT, RegistryManager.ALUMINUM_BLOCK, TagProvider.INGOTS_ALUMINUM, TagProvider.ORE_ALUMINUM_ITEM, RegistryManager.RAW_ALUMINUM, RegistryManager.RAW_ALUMINUM_BLOCK, TagProvider.RAW_ALUMINUM, RegistryManager.ALUMINUM_DUST);
        depositRecipes(consumer, RegistryManager.OSMIUM_INGOT, RegistryManager.OSMIUM_BLOCK, TagProvider.INGOTS_OSMIUM, TagProvider.ORE_OSMIUM_ITEM, RegistryManager.RAW_OSMIUM, RegistryManager.RAW_OSMIUM_BLOCK, TagProvider.RAW_OSMIUM, RegistryManager.OSMIUM_DUST);
        depositRecipes(consumer, RegistryManager.ARDITE_INGOT, RegistryManager.ARDITE_BLOCK, TagProvider.INGOTS_ARDITE, TagProvider.ORE_ARDITE_ITEM, RegistryManager.RAW_ARDITE, RegistryManager.RAW_ARDITE_BLOCK, TagProvider.RAW_ARDITE, RegistryManager.ARDITE_DUST);
        depositRecipes(consumer, RegistryManager.COBALT_INGOT, RegistryManager.COBALT_BLOCK, TagProvider.INGOTS_COBALT, TagProvider.ORE_COBALT_ITEM, RegistryManager.RAW_COBALT, RegistryManager.RAW_COBALT_BLOCK, TagProvider.RAW_COBALT, RegistryManager.COBALT_DUST);
        depositRecipes(consumer, RegistryManager.PLATINUM_INGOT, RegistryManager.PLATINUM_BLOCK, TagProvider.INGOTS_PLATINUM, TagProvider.ORE_PLATINUM_ITEM, RegistryManager.RAW_PLATINUM, RegistryManager.RAW_PLATINUM_BLOCK, TagProvider.RAW_PLATINUM, RegistryManager.PLATINUM_DUST);

        basicGrinderRecipe(consumer,
                GrinderRecipeBuilder.grinder(Ingredient.of(Items.BONE), Items.BONE_MEAL)
                        .count(4));
        basicGrinderRecipe(consumer,
                GrinderRecipeBuilder.grinder(Ingredient.of(ItemTags.REDSTONE_ORES), Items.REDSTONE)
                        .count(5));
        basicGrinderRecipe(consumer,
                GrinderRecipeBuilder.grinder(Ingredient.of(ItemTags.LAPIS_ORES), Items.LAPIS_LAZULI)
                        .count(8));
        basicGrinderRecipe(consumer,
                GrinderRecipeBuilder.grinder(Ingredient.of(Items.BLAZE_ROD), Items.BLAZE_POWDER)
                        .count(4));
        basicGrinderRecipe(consumer,
                GrinderRecipeBuilder.grinder(Ingredient.of(ItemTags.EMERALD_ORES), Items.EMERALD));
        basicGrinderRecipe(consumer,
                GrinderRecipeBuilder.grinder(Ingredient.of(ItemTags.DIAMOND_ORES), Items.DIAMOND));
        basicGrinderRecipe(consumer,
                GrinderRecipeBuilder.grinder(Ingredient.of(ItemTags.COAL_ORES), Items.COAL)
                        .count(3));
    }
}
