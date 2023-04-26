package gameonlp.oredepos.blocks.smelter;

import gameonlp.oredepos.RegistryManager;
import gameonlp.oredepos.blocks.BasicMachineTile;
import gameonlp.oredepos.blocks.Working;
import gameonlp.oredepos.crafting.FluidInventory;
import gameonlp.oredepos.crafting.SmelterRecipe;
import gameonlp.oredepos.items.ModuleItem;
import gameonlp.oredepos.net.PacketManager;
import gameonlp.oredepos.net.PacketProductivitySync;
import gameonlp.oredepos.net.PacketProgressSync;
import gameonlp.oredepos.tile.EnergyHandlerTile;
import gameonlp.oredepos.tile.ModuleAcceptorTile;
import gameonlp.oredepos.util.EnergyCell;
import gameonlp.oredepos.util.PlayerInOutStackHandler;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.network.PacketDistributor;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

public class SmelterTile extends BasicMachineTile implements EnergyHandlerTile, ModuleAcceptorTile {

    PlayerInOutStackHandler handler;

    LazyOptional<ItemStackHandler> machineItemHandler = LazyOptional.of(() -> handler.getMachineAccessible());
    LazyOptional<ItemStackHandler> itemHandler = LazyOptional.of(() -> handler.getPlayerAccessible());
    LazyOptional<IEnergyStorage> energyHandler = LazyOptional.of(() -> energyCell);

    SmelterRecipe currentRecipe = null;
    AbstractCookingRecipe vanillaRecipe = null;

    protected SmelterTile(BlockEntityType<?> p_i48289_1_, BlockPos pos, BlockState state) {
        super(p_i48289_1_, pos, state);
        slots = createItemHandler();
        handler = new PlayerInOutStackHandler(this, slots, 1);
        energyCell = new EnergyCell(this, false, true, 16000);
        maxProgress = 30;
    }

    public SmelterTile(BlockPos pos, BlockState state) {
        this(RegistryManager.SMELTER_TILE.get(), pos, state);
    }

    public static int getVanillaDrain() {
        return 10;
    }

    private ItemStackHandler createItemHandler() {
        return new ItemStackHandler(5){
            @Override
            protected void onContentsChanged(int slot) {
                super.onContentsChanged(slot);
                setChanged();
            }

            @Override
            public boolean isItemValid(int slot, @Nonnull ItemStack stack) {
                if (slot > 1) {
                    return stack.getItem() instanceof ModuleItem && ((ModuleItem) stack.getItem()).isAccepted(getName());
                }
                return super.isItemValid(slot, stack);
            }
        };
    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        if (side == null && CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.equals(cap)){
            return itemHandler.cast();
        }
        if (CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.equals(cap)) {
            return machineItemHandler.cast();
        }
        if (CapabilityEnergy.ENERGY.equals(cap)){
            return energyHandler.cast();
        }
        return super.getCapability(cap, side);
    }

    @Override
    public void invalidateCaps() {
        super.invalidateCaps();
        machineItemHandler.invalidate();
        itemHandler.invalidate();
        energyHandler.invalidate();
    }

    @Override
    public void saveAdditional(@NotNull CompoundTag tag) {
        super.saveAdditional(tag);
        tag.putInt("energy", energyCell.getEnergyStored());
        tag.putFloat("progress", progress);
        tag.putFloat("productivity", productivity);
        tag.put("slots", slots.serializeNBT());
    }

    @Override
    public void load(@NotNull CompoundTag p_230337_2_) {
        super.load(p_230337_2_);
        energyCell.setEnergy(p_230337_2_.getInt("energy"));
        progress = p_230337_2_.getFloat("progress");
        productivity = p_230337_2_.getFloat("productivity");
        slots.deserializeNBT(p_230337_2_.getCompound("slots"));
    }

    public static void serverTick(Level level, BlockPos blockPos, BlockState blockState, SmelterTile e) {
        e.tick();
    }

    private void tick() {
        if (level == null || level.isClientSide()){
            return;
        }
        if (currentRecipe == null && vanillaRecipe == null && !getBlockState().getValue(Working.WORKING).equals(Working.INACTIVE)) {
            level.setBlockAndUpdate(getBlockPos(), getBlockState().setValue(Working.WORKING, Working.INACTIVE));
        }
        FluidInventory fluidInventory = new FluidInventory(1, 0);
        fluidInventory.setItem(0, slots.getStackInSlot(1));
        if (currentRecipe == null && vanillaRecipe == null) {
            currentRecipe = level.getRecipeManager().getRecipeFor(RegistryManager.SMELTER_RECIPE_TYPE.get(), fluidInventory, level).orElse(null);
            if (currentRecipe == null) {
                vanillaRecipe = level.getRecipeManager().getRecipeFor(RecipeType.BLASTING, fluidInventory, level).orElse(null);
                if (vanillaRecipe == null) {
                    vanillaRecipe = level.getRecipeManager().getRecipeFor(RecipeType.SMELTING, fluidInventory, level).orElse(null);
                }
                if (vanillaRecipe == null) {
                    vanillaRecipe = level.getRecipeManager().getRecipeFor(RecipeType.SMOKING, fluidInventory, level).orElse(null);
                }
            }
        }
        if (currentRecipe != null || vanillaRecipe != null){
            if (!getBlockState().getValue(Working.WORKING).equals(Working.ACTIVE)) {
                level.setBlockAndUpdate(getBlockPos(), getBlockState().setValue(Working.WORKING, Working.ACTIVE));
            }
            ItemStack resultItem = (currentRecipe != null ? currentRecipe : vanillaRecipe).getResultItem().copy();
            if ((currentRecipe != null && !currentRecipe.matches(fluidInventory, level))
                    || (vanillaRecipe != null && !vanillaRecipe.matches(fluidInventory, level))
                    || resultItem.isEmpty()
                    || slots.insertItem(0, resultItem, true) != ItemStack.EMPTY) {
                progress = 0;
                currentRecipe = null;
                vanillaRecipe = null;
                PacketManager.INSTANCE.send(PacketDistributor.ALL.noArg(), new PacketProgressSync(worldPosition, progress));
                level.setBlockAndUpdate(getBlockPos(), getBlockState().setValue(Working.WORKING, Working.INACTIVE));
                return;
            }
            List<ModuleItem> modules = getModuleItems(2);
            float drain = getDrain(modules, currentRecipe != null ? (float) currentRecipe.getEnergy() : getVanillaDrain());
            increaseProgress(modules, drain, (currentRecipe != null ? currentRecipe.getTicks() : vanillaRecipe.getCookingTime() * getVanillaSpeedFactor()));
            if (progress >= maxProgress) {
                progress = 0;
                PacketManager.INSTANCE.send(PacketDistributor.ALL.noArg(), new PacketProgressSync(worldPosition, progress));
                NonNullList<Ingredient> ingredients = currentRecipe != null ? currentRecipe.getIngredients() : vanillaRecipe.getIngredients();
                for (Ingredient ingredient : ingredients) {
                    if (ingredient.test(slots.getStackInSlot(1))) {
                        slots.extractItem(1, 1, false);
                    }
                }
                ItemStack outStack = resultItem;
                int count = outStack.getCount();
                while (productivity >= 1){
                    productivity -= 1;
                    PacketManager.INSTANCE.send(PacketDistributor.ALL.noArg(), new PacketProductivitySync(worldPosition, productivity));
                    outStack.setCount(outStack.getCount() + count);
                }
                slots.insertItem(0, outStack, false);
                increaseProductivity(modules);
            }
        }
    }

    public static float getVanillaSpeedFactor() {
        return 0.5f;
    }

    public static String getName() {
        return "smelter";
    }
}
