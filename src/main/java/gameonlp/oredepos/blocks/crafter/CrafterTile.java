package gameonlp.oredepos.blocks.crafter;

import gameonlp.oredepos.RegistryManager;
import gameonlp.oredepos.blocks.BasicMachineTile;
import gameonlp.oredepos.crafting.CountIngredient;
import gameonlp.oredepos.crafting.FluidInventory;
import gameonlp.oredepos.crafting.crafter.CrafterRecipe;
import gameonlp.oredepos.items.ModuleItem;
import gameonlp.oredepos.net.*;
import gameonlp.oredepos.tile.EnergyHandlerTile;
import gameonlp.oredepos.tile.LockableTile;
import gameonlp.oredepos.tile.ModuleAcceptorTile;
import gameonlp.oredepos.tile.SettableRecipeTile;
import gameonlp.oredepos.util.EnergyCell;
import gameonlp.oredepos.util.PlayerInOutStackHandler;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Containers;
import net.minecraft.world.item.ItemStack;
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

public class CrafterTile extends BasicMachineTile implements EnergyHandlerTile, ModuleAcceptorTile, LockableTile, SettableRecipeTile {

    PlayerInOutStackHandler handler;

    LazyOptional<ItemStackHandler> machineItemHandler = LazyOptional.of(() -> handler.getMachineAccessible());
    LazyOptional<ItemStackHandler> itemHandler = LazyOptional.of(() -> handler.getPlayerAccessible());
    LazyOptional<IEnergyStorage> energyHandler = LazyOptional.of(() -> energyCell);

    CrafterRecipe currentRecipe = null;
    boolean locked = false;
    public final CrafterManager crafterManager = new CrafterManager();
    private int index;
    private String recipe;

    public int getGridSize() {
        return gridSize;
    }

    private int gridSize;

    protected CrafterTile(BlockEntityType<?> p_i48289_1_, BlockPos pos, BlockState state) {
        super(p_i48289_1_, pos, state);
        slots = createItemHandler();
        handler = new PlayerInOutStackHandler(this, slots, 1, 12);
        energyCell = new EnergyCell(this, false, true, 16000);
    }

    public CrafterTile(BlockPos pos, BlockState state) {
        this(pos, state, 0);
    }

    public CrafterTile(BlockPos pos, BlockState state, int gridSize) {
        this(RegistryManager.CRAFTER_TILE.get(), pos, state);
        this.gridSize = gridSize;
    }

    private ItemStackHandler createItemHandler() {
        return new ItemStackHandler(13){
            @Override
            protected void onContentsChanged(int slot) {
                super.onContentsChanged(slot);
                setChanged();
            }

            @Override
            public boolean isItemValid(int slot, @Nonnull ItemStack stack) {
                if (slot > 9) {
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
        tag.putInt("gridSize", gridSize);
        tag.putFloat("progress", progress);
        tag.putFloat("productivity", productivity);
        tag.putBoolean("locked", locked);
        tag.putString("recipe", currentRecipe != null ? currentRecipe.getId().toString() : "");
        tag.put("slots", slots.serializeNBT());
    }

    @Override
    public void load(@NotNull CompoundTag p_230337_2_) {
        super.load(p_230337_2_);
        energyCell.setEnergy(p_230337_2_.getInt("energy"));
        gridSize = p_230337_2_.getInt("gridSize");
        progress = p_230337_2_.getFloat("progress");
        productivity = p_230337_2_.getFloat("productivity");
        locked = p_230337_2_.getBoolean("locked");
        crafterManager.refresh(level);
        recipe = p_230337_2_.getString("recipe");
        slots.deserializeNBT(p_230337_2_.getCompound("slots"));
    }

    public static void clientTick(Level level, BlockPos blockPos, BlockState blockState, CrafterTile e) {
        e.crafterManager.refresh(level);
    }

    public static void serverTick(Level level, BlockPos blockPos, BlockState blockState, CrafterTile e) {
        e.tick();
    }
    private void tick() {
        if (level == null || level.isClientSide()){
            return;
        }
        update();
        List<ModuleItem> modules = getModuleItems(2);
        ModuleItem.ModuleBoosts moduleBoosts = new ModuleItem.ModuleBoosts();
        getModuleBoosts(modules, moduleBoosts);
        if (moduleBoosts.ejecting) {
            eject(0, 0);
        }
        if (recipe != null) {
            crafterManager.refresh(level);
            currentRecipe = crafterManager.getRecipe(new ResourceLocation(recipe));
            recipe = null;
        }
        FluidInventory fluidInventory = new FluidInventory(9, 0);
        for (int i = 0; i < 9; i++) {
            fluidInventory.setItem(i, slots.getStackInSlot(i + 1));
        }
        if (!locked) {
            crafterManager.refresh(level);
            List<CrafterRecipe> possibilities = crafterManager.possibilities(fluidInventory, gridSize);
            if (!possibilities.contains(currentRecipe)) {
                progress = 0;
                if (possibilities.size() > 0) {
                    currentRecipe = possibilities.get(0);
                    PacketManager.INSTANCE.send(PacketDistributor.ALL.noArg(), new PacketRecipeSync(getBlockPos(), currentRecipe.getId()));
                } else {
                    currentRecipe = null;
                }
                PacketManager.INSTANCE.send(PacketDistributor.ALL.noArg(), new PacketProgressSync(worldPosition, progress));
                return;
            }
        }
        if (currentRecipe != null && locked){
            if (!currentRecipe.matches(fluidInventory, level)){
                progress = 0;
                PacketManager.INSTANCE.send(PacketDistributor.ALL.noArg(), new PacketProgressSync(worldPosition, progress));
                return;
            }
            if (isInventoryFull(modules, List.of(currentRecipe.getResultItem().copy()), 0, 0)){
                return;
            }
            float drain = getDrain(modules, (float) currentRecipe.getEnergy());
            increaseProgress(modules, drain, currentRecipe.getTicks());
            if (progress >= maxProgress - 0.0001f) {
                progress = 0;
                PacketManager.INSTANCE.send(PacketDistributor.ALL.noArg(), new PacketProgressSync(worldPosition, progress));
                NonNullList<CountIngredient> ingredients = currentRecipe.getCountIngredients();
                for (CountIngredient ingredient : ingredients) {
                    int count = ingredient.getCount();
                    for (int i = 1; i <= 9; i++) {
                        ItemStack stackInSlot = slots.getStackInSlot(i);
                        if (ingredient.test(stackInSlot)) {
                            count -= slots.extractItem(i, count, false).getCount();
                            if (count <= 0) {
                                break;
                            }
                        }
                    }
                }
                NonNullList<ItemStack> remainingItems = currentRecipe.getRemainingItems(fluidInventory);
                NonNullList<ItemStack> remainingRemainingItems = NonNullList.create();
                for (ItemStack remainingItem : remainingItems) {
                    for (int i = 1; i < 9; i++) {
                        remainingItem = slots.insertItem(i, remainingItem, false);
                        if (remainingItem.isEmpty()) {
                            break;
                        }
                    }
                    if (!remainingItem.isEmpty()) {
                        remainingRemainingItems.add(remainingItem);
                    }
                }
                Containers.dropContents(level, getBlockPos(), remainingRemainingItems);
                ItemStack outStack = currentRecipe.getResultItem();
                handleOutputs(List.of(outStack), 0, 0);
                increaseProductivity(modules);
            }
        }
    }

    public static String getName() {
        return "crafter";
    }

    public void toggleLocked() {
        locked = !locked;
        PacketManager.INSTANCE.sendToServer(new PacketLockedSync(worldPosition, locked));
    }

    @Override
    public void setLocked(boolean locked) {
        progress = 0;
        this.locked = locked;
        PacketManager.INSTANCE.send(PacketDistributor.ALL.noArg(), new PacketProgressSync(getBlockPos(), progress));
    }

    @Override
    public void setRecipe(ResourceLocation recipe) {
        if (recipe == null) {
            currentRecipe = null;
            return;
        }
        currentRecipe = crafterManager.getRecipe(recipe);
    }

    public void changeRecipe(int change) {
        if (locked) {
            return;
        }
        FluidInventory fluidInventory = new FluidInventory(9, 0);
        for (int i = 0; i < 9; i++) {
            fluidInventory.setItem(i, slots.getStackInSlot(i + 1));
        }
        List<CrafterRecipe> possibilities = crafterManager.possibilities(fluidInventory, gridSize);
        if (currentRecipe != null && possibilities.size() > 0) {
            currentRecipe = possibilities.get(Math.floorMod(possibilities.indexOf(currentRecipe) + change, possibilities.size()));
        } else if (possibilities.size() > 0){
            currentRecipe = possibilities.get(0);
        } else {
            return;
        }
        PacketManager.INSTANCE.sendToServer(new PacketRecipeSync(getBlockPos(), currentRecipe.getId()));
    }
}
