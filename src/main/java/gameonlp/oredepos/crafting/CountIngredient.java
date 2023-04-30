package gameonlp.oredepos.crafting;

import com.google.gson.JsonElement;
import it.unimi.dsi.fastutil.ints.IntList;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraftforge.common.crafting.IIngredientSerializer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.function.Predicate;
import java.util.stream.Stream;

import net.minecraft.world.item.crafting.Ingredient.Value;

public class CountIngredient extends Ingredient {
    private int count;
    private Ingredient wrapped;

    protected CountIngredient(Stream<? extends Value> p_43907_, Ingredient wrapped, int count) {
        super(p_43907_);
        this.wrapped = wrapped;
        this.count = count;
    }

    @Override
    public ItemStack[] getItems() {
        return wrapped.getItems();
    }

    @Override
    public IntList getStackingIds() {
        return wrapped.getStackingIds();
    }

    @Override
    public JsonElement toJson() {
        return wrapped.toJson();
    }

    @Override
    public boolean isEmpty() {
        return wrapped.isEmpty();
    }

    @Override
    public boolean isSimple() {
        return wrapped.isSimple();
    }

    @Override
    public IIngredientSerializer<? extends Ingredient> getSerializer() {
        return wrapped.getSerializer();
    }

    @NotNull
    @Override
    public Predicate<ItemStack> and(@NotNull Predicate<? super ItemStack> other) {
        return wrapped.and(other);
    }

    @NotNull
    @Override
    public Predicate<ItemStack> negate() {
        return wrapped.negate();
    }

    @NotNull
    @Override
    public Predicate<ItemStack> or(@NotNull Predicate<? super ItemStack> other) {
        return wrapped.or(other);
    }

    public static CountIngredient of(Ingredient ingredient, int count) {
        return new CountIngredient(Stream.empty(), ingredient, count);
    }

    public int getCount() {
        return count;
    }

    @Override
    public String toString() {
        return "CountIngredient{" +
                "count=" + count +
                ", wrapped=" + Arrays.toString(wrapped.getItems()) +
                '}';
    }

    @Override
    public boolean test(ItemStack itemStack) {
        return wrapped.test(itemStack);
    }
}
