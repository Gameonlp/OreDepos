package gameonlp.oredepos.data;

import gameonlp.oredepos.OreDepos;
import gameonlp.oredepos.RegistryManager;
import net.minecraft.advancements.Criterion;
import net.minecraft.advancements.criterion.CriterionInstance;
import net.minecraft.advancements.criterion.InventoryChangeTrigger;
import net.minecraft.advancements.criterion.ItemPredicate;
import net.minecraft.block.Block;
import net.minecraft.data.*;
import net.minecraft.item.Item;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.tags.ITag;
import net.minecraft.util.ResourceLocation;

import java.util.function.Consumer;

public class OreDepositsRecipeProvider extends RecipeProvider {
    public OreDepositsRecipeProvider(DataGenerator p_i48262_1_) {
        super(p_i48262_1_);
    }

    private void depositRecipes(Consumer<IFinishedRecipe> consumer, Item item, Block storage, Block ore, ITag<Item> tag){
        CookingRecipeBuilder.smelting(Ingredient.of(ore), item, 0.7f, 200)
                .unlockedBy("has_items", InventoryChangeTrigger.Instance.hasItems(ore))
                .save(consumer);
        ShapelessRecipeBuilder.shapeless(item, 9)
                .requires(storage.asItem())
                .unlockedBy("has_items", InventoryChangeTrigger.Instance.hasItems(storage))
                .save(consumer, new ResourceLocation(OreDepos.MODID, item.getRegistryName().getPath() + "_from_" + storage.getRegistryName().getPath()));
        ShapedRecipeBuilder.shaped(storage)
                .pattern("AAA")
                .pattern("AAA")
                .pattern("AAA")
                .define('A', tag)
                .unlockedBy("has_items", InventoryChangeTrigger.Instance.hasItems(ItemPredicate.Builder.item().of(tag).build()))
                .save(consumer);
    }

    @Override
    protected void buildShapelessRecipes(Consumer<IFinishedRecipe> consumer) {
        depositRecipes(consumer, RegistryManager.OSMIUM_INGOT, RegistryManager.OSMIUM_BLOCK, RegistryManager.OSMIUM_ORE, TagProvider.INGOTS_OSMIUM);
        depositRecipes(consumer, RegistryManager.ARDITE_INGOT, RegistryManager.ARDITE_BLOCK, RegistryManager.ARDITE_ORE, TagProvider.INGOTS_ARDITE);
        depositRecipes(consumer, RegistryManager.COBALT_INGOT, RegistryManager.COBALT_BLOCK, RegistryManager.COBALT_ORE, TagProvider.INGOTS_COBALT);
    }
}
