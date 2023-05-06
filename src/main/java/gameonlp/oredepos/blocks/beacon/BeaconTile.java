package gameonlp.oredepos.blocks.beacon;

import gameonlp.oredepos.RegistryManager;
import gameonlp.oredepos.blocks.BasicMachineTile;
import gameonlp.oredepos.config.OreDeposConfig;
import gameonlp.oredepos.items.ModuleItem;
import gameonlp.oredepos.tile.EnergyHandlerTile;
import gameonlp.oredepos.tile.ModuleAcceptorTile;
import gameonlp.oredepos.util.EnergyCell;
import gameonlp.oredepos.util.PlayerInOutStackHandler;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.LinkedList;
import java.util.List;

public class BeaconTile extends BasicMachineTile implements EnergyHandlerTile, ModuleAcceptorTile {

    LazyOptional<ItemStackHandler> itemHandler = LazyOptional.of(() -> slots);
    LazyOptional<IEnergyStorage> energyHandler = LazyOptional.of(() -> energyCell);
    List<ModuleAcceptorTile> boosted;

    protected BeaconTile(BlockEntityType<?> p_i48289_1_, BlockPos pos, BlockState state) {
        super(p_i48289_1_, pos, state);
        slots = createItemHandler();
        energyCell = new EnergyCell(this, false, true, 16000);
        boosted = new LinkedList<>();
    }

    public BeaconTile(BlockPos pos, BlockState state) {
        this(RegistryManager.BEACON_TILE.get(), pos, state);
    }

    private ItemStackHandler createItemHandler() {
        return new ItemStackHandler(2){
            @Override
            protected void onContentsChanged(int slot) {
                super.onContentsChanged(slot);
                setChanged();
            }

            @Override
            public boolean isItemValid(int slot, @Nonnull ItemStack stack) {
                if (slot > 0) {
                    return stack.getItem() instanceof ModuleItem && ((ModuleItem) stack.getItem()).isAccepted(getName());
                }
                return super.isItemValid(slot, stack);
            }
        };
    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        if (CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.equals(cap)){
            return itemHandler.cast();
        }
        if (CapabilityEnergy.ENERGY.equals(cap)){
            return energyHandler.cast();
        }
        return super.getCapability(cap, side);
    }

    @Override
    public void invalidateCaps() {
        super.invalidateCaps();
        itemHandler.invalidate();
        energyHandler.invalidate();
    }

    @Override
    public void saveAdditional(@NotNull CompoundTag tag) {
        super.saveAdditional(tag);
        tag.putInt("energy", energyCell.getEnergyStored());
        tag.put("slots", slots.serializeNBT());
    }

    @Override
    public void load(@NotNull CompoundTag p_230337_2_) {
        super.load(p_230337_2_);
        energyCell.setEnergy(p_230337_2_.getInt("energy"));
        slots.deserializeNBT(p_230337_2_.getCompound("slots"));
    }

    public static void serverTick(Level level, BlockPos blockPos, BlockState blockState, BeaconTile e) {
        e.tick();
    }
    private void tick() {
        if (level == null || level.isClientSide()){
            return;
        }
        update();
        int width = 1, depth = 1, length = 1;
        List<ModuleItem> moduleItems = getModuleItems(0);
        float beaconDrain = OreDeposConfig.Common.beaconDrain.get();
        for (ModuleItem moduleItem : moduleItems) {
            width = moduleItem.getWidth(width);
            depth = moduleItem.getDepth(depth);
            length = moduleItem.getLength(length);
        }
        if (energyCell.getEnergyStored() > beaconDrain) {
            energyCell.extractEnergy((int) beaconDrain, false);
            for (int x = -1 - width; x <= 1 + width; x++) {
                for (int y = -1 - depth; y <= 1 + depth; y++) {
                    for (int z = -1 - length; z <= 1 + length; z++) {
                        if (level == null) {
                            continue;
                        }
                        BlockEntity acceptor = level.getBlockEntity(worldPosition.offset(x, y, z));
                        if (!(acceptor instanceof ModuleAcceptorTile moduleAcceptorTile)) {
                            continue;
                        }
                        if (!boosted.contains(moduleAcceptorTile)) {
                            boosted.add(moduleAcceptorTile);
                            moduleAcceptorTile.addBeacon(this);
                        }
                    }
                }
            }
        } else {
            for (ModuleAcceptorTile moduleAcceptorTile : boosted) {
                moduleAcceptorTile.removeBeacon(this);
            }
            boosted.clear();
        }
    }

    @Override
    public void addBeacon(BeaconTile beacon) {
    }

    public static String getName() {
        return "beacon";
    }
}
