package gameonlp.oredepos.blocks.generator;

import gameonlp.oredepos.RegistryManager;
import gameonlp.oredepos.blocks.BasicMachineTile;
import gameonlp.oredepos.blocks.beacon.BeaconTile;
import gameonlp.oredepos.config.OreDeposConfig;
import gameonlp.oredepos.items.ModuleItem;
import gameonlp.oredepos.net.PacketManager;
import gameonlp.oredepos.net.PacketProgressSync;
import gameonlp.oredepos.tile.EnergyHandlerTile;
import gameonlp.oredepos.tile.ModuleAcceptorTile;
import gameonlp.oredepos.util.EnergyCell;
import gameonlp.oredepos.util.OptionUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.Containers;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.FurnaceBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.network.PacketDistributor;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.LinkedList;
import java.util.List;

import static gameonlp.oredepos.util.OptionUtil.option;

public class GeneratorTile extends BasicMachineTile implements EnergyHandlerTile, ModuleAcceptorTile {


    LazyOptional<ItemStackHandler> itemHandler = LazyOptional.of(() -> slots);
    LazyOptional<IEnergyStorage> energyHandler = LazyOptional.of(() -> energyCell);
    private float burnTimeLeft;


    protected GeneratorTile(BlockEntityType<?> p_i48289_1_, BlockPos pos, BlockState state) {
        super(p_i48289_1_, pos, state);
        slots = createItemHandler();
        energyCell = new EnergyCell(this, true, false, 16000);
    }

    public GeneratorTile(BlockPos pos, BlockState state) {
        this(RegistryManager.GENERATOR_TILE.get(), pos, state);
    }

    private ItemStackHandler createItemHandler() {
        return new ItemStackHandler(3){
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
        if (ForgeCapabilities.ITEM_HANDLER.equals(cap)) {
            return itemHandler.cast();
        }
        if (ForgeCapabilities.ENERGY.equals(cap)){
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
        tag.putFloat("progress", progress);
        tag.putFloat("productivity", productivity);
        tag.putFloat("burnTimeLeft", burnTimeLeft);
        tag.put("slots", slots.serializeNBT());
    }

    @Override
    public void load(@NotNull CompoundTag p_230337_2_) {
        super.load(p_230337_2_);
        energyCell.setEnergy(p_230337_2_.getInt("energy"));
        progress = p_230337_2_.getFloat("progress");
        productivity = p_230337_2_.getFloat("productivity");
        burnTimeLeft = p_230337_2_.getFloat("burnTimeLeft");
        slots.deserializeNBT(p_230337_2_.getCompound("slots"));
    }

    public static void serverTick(Level level, BlockPos blockPos, BlockState blockState, GeneratorTile e) {
        e.tick();
    }
    private void tick() {
        if (level == null || level.isClientSide()){
            return;
        }
        update();
        List<IEnergyStorage> neighbours = new LinkedList<>();
        option(level.getBlockEntity(getBlockPos().offset(0, 1, 0))).ifPresent(b -> b.getCapability(ForgeCapabilities.ENERGY, Direction.DOWN).ifPresent(neighbours::add));
        option(level.getBlockEntity(getBlockPos().offset(0, -1, 0))).ifPresent(b -> b.getCapability(ForgeCapabilities.ENERGY, Direction.UP).ifPresent(neighbours::add));
        option(level.getBlockEntity(getBlockPos().offset(1, 0, 0))).ifPresent(b -> b.getCapability(ForgeCapabilities.ENERGY, Direction.WEST).ifPresent(neighbours::add));
        option(level.getBlockEntity(getBlockPos().offset(-1, 0, 0))).ifPresent(b -> b.getCapability(ForgeCapabilities.ENERGY, Direction.EAST).ifPresent(neighbours::add));
        option(level.getBlockEntity(getBlockPos().offset(0, 0, 1))).ifPresent(b -> b.getCapability(ForgeCapabilities.ENERGY, Direction.NORTH).ifPresent(neighbours::add));
        option(level.getBlockEntity(getBlockPos().offset(0, 0, -1))).ifPresent(b -> b.getCapability(ForgeCapabilities.ENERGY, Direction.SOUTH).ifPresent(neighbours::add));
        for (IEnergyStorage neighbour : neighbours) {
            if (neighbour.canReceive()) {
                int accepted = neighbour.receiveEnergy(Math.min(energyCell.getEnergyStored(), OreDeposConfig.Common.generartorTransferCap.get()), false);
                energyCell.setEnergy(energyCell.getEnergyStored() - accepted);
            }
        }
        generate();
    }

    private void generate() {
        List<ModuleItem> moduleItems = getModuleItems(0);
        float drain = getDrain(moduleItems, 1f);
        if (burnTimeLeft == 0 || maxProgress - 0.0001f <= progress) {
            progress = 0;
            burnTimeLeft = 0;
            Integer integer = getBurnTime();
            if (integer == null) return;
            int burnTime = integer;
            if (burnTime > 0) {
                boolean canConsumeFuel = canConsumeFuel();
                if (canConsumeFuel) {
                    //noinspection IntegerDivisionInFloatingPointContext
                    burnTimeLeft = burnTime / 20;
                    if (productivity >= 1) {
                        productivity -= 1;
                    } else {
                        consumeFuel();
                    }
                    increaseProductivity(moduleItems);
                }
            }
        } else if (burnTimeLeft > 0) {
            increaseProgress(moduleItems, drain, burnTimeLeft / drain);
        }
    }

    private @NotNull Integer getBurnTime() {
        return ForgeHooks.getBurnTime(slots.getStackInSlot(0), null);
    }

    private boolean canConsumeFuel() {
        ItemStack itemStack = slots.extractItem(0, 1, true);
        return !itemStack.isEmpty();
    }

    private void consumeFuel() {
        ItemStack containerItem = slots.extractItem(0, 1, false).getCraftingRemainingItem();
        ItemStack not_inserted = slots.insertItem(0, containerItem, false);
        if (!not_inserted.isEmpty()) {
            OptionUtil.option(level).ifPresent(level -> Containers.dropContents(level, getBlockPos(), NonNullList.of(not_inserted)));
        }
    }

    protected void increaseProgress(List<ModuleItem> modules, float drain, float time) {
        if (energyCell.getEnergyStored() < energyCell.getMaxEnergyStored()) {
            ModuleItem.ModuleBoosts moduleBoosts = new ModuleItem.ModuleBoosts();
            getModuleBoosts(modules, moduleBoosts);

            float progressRatio = time / maxProgress;
            energyCell.setEnergy(Math.min(energyCell.getMaxEnergyStored(), (int) (energyCell.getEnergyStored() + OreDeposConfig.Common.generartorEnergyRate.get() * moduleBoosts.progress)));
            progress += (1 / progressRatio);
            PacketManager.INSTANCE.send(PacketDistributor.ALL.noArg(), new PacketProgressSync(worldPosition, progress));
            this.setChanged();
        }
    }

    public static String getName() {
        return "generator";
    }
}
