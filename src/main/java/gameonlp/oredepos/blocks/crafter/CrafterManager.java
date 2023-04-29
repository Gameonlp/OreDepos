package gameonlp.oredepos.blocks.crafter;

import gameonlp.oredepos.RegistryManager;
import gameonlp.oredepos.config.OreDeposConfig;
import gameonlp.oredepos.crafting.CountIngredient;
import gameonlp.oredepos.crafting.FluidInventory;
import gameonlp.oredepos.crafting.crafter.CrafterRecipe;
import net.minecraft.core.NonNullList;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingRecipe;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;

import java.util.*;

public class CrafterManager {
    private List<CraftingRecipe> craftingRecipes;
    private List<CrafterRecipe> wrappedRecipes;
    private Map<Set<ItemStack>, List<CrafterRecipe>> cache;

    public CrafterManager() {
        cache = new HashMap<>();
        wrappedRecipes = new LinkedList<>();
    }

    public void refresh(Level level) {
        List<CraftingRecipe> allRecipesFor = level.getRecipeManager().getAllRecipesFor(RecipeType.CRAFTING);
        if (!allRecipesFor.equals(craftingRecipes)) {
            cache = new HashMap<>();
            craftingRecipes = allRecipesFor;
            wrappedRecipes = new LinkedList<>(craftingRecipes.stream().map(r ->
                    new CrafterRecipe(
                            new ResourceLocation("oredepos", "wrapped_" + r.getId().getPath() + "_for_crafter"),
                            deduplicate(r.getIngredients()),
                            r.getResultItem().copy(),
                            OreDeposConfig.Common.crafterDrain.get(),
                            getVanillaTicks()
                    )
            ).toList());
        }
        wrappedRecipes.addAll(level.getRecipeManager().getAllRecipesFor(RegistryManager.CRAFTER_RECIPE_TYPE.get()));
        wrappedRecipes.removeIf(crafterRecipe -> crafterRecipe.getCountIngredients().isEmpty() || crafterRecipe.getResultItem().isEmpty());
        wrappedRecipes.removeIf(crafterRecipe -> OreDeposConfig.Common.blackListedItems.get().contains(crafterRecipe.getResultItem().getItem().getRegistryName().toString()));
    }

    public List<CrafterRecipe> possibilities(FluidInventory inventory) {
        Set<ItemStack> itemStacks = new HashSet<>();
        for (int i = 0; i < inventory.getContainerSize(); i++) {
            ItemStack itemStack = inventory.getItem(i).copy();
            if (!itemStack.isEmpty()) {
                itemStack.setCount(1);
                itemStacks.add(itemStack);
            }
        }
        if (cache.containsKey(itemStacks)) {
            return cache.get(itemStacks);
        }
        List<CrafterRecipe> possible = new LinkedList<>();
        for (CrafterRecipe wrappedRecipe : wrappedRecipes) {
            boolean invalid = false;
            for (ItemStack itemStack : itemStacks) {
                invalid = true;
                for (CountIngredient ingredient : wrappedRecipe.getCountIngredients()) {
                    if (ingredient.test(itemStack)) {
                        invalid = false;
                        break;
                    }
                }
                if (invalid) {
                    break;
                }
            }
            if (!invalid) {
                possible.add(wrappedRecipe);
            }
        }
        cache.put(itemStacks, possible);
        return possible;
    }

    private static NonNullList<CountIngredient> deduplicate(NonNullList<Ingredient> ingredients) {
        Map<Ingredient, Integer> counts = new HashMap<>();
        for (Ingredient ingredient : ingredients) {
            if (!ingredient.isEmpty()) {
                counts.put(ingredient, counts.computeIfAbsent(ingredient, k -> 0) + 1);
            }
        }
        NonNullList<CountIngredient> deduplicated = NonNullList.create();
        counts.forEach((K, V) -> deduplicated.add(CountIngredient.of(K, V)));
        return deduplicated;
    }

    public static int getVanillaTicks() {
        return 40;
    }

    public CrafterRecipe getRecipe(int index) {
        return wrappedRecipes.get(index);
    }

    public CrafterRecipe getRecipe(ResourceLocation resourceLocation) {
        for (CrafterRecipe wrappedRecipe : wrappedRecipes) {
            if (wrappedRecipe.getId().equals(resourceLocation)) {
                return wrappedRecipe;
            }
        }
        return null;
    }

    public List<CrafterRecipe> possibilities() {
        return wrappedRecipes;
    }
}
