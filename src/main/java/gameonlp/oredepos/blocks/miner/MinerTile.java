package gameonlp.oredepos.blocks.miner;

import gameonlp.oredepos.RegistryManager;
import gameonlp.oredepos.util.CustomFluidTank;
import gameonlp.oredepos.items.DrillHeadItem;
import gameonlp.oredepos.items.ModuleItem;
import gameonlp.oredepos.blocks.oredeposit.OreDepositTile;
import gameonlp.oredepos.tile.EnergyHandlerTile;
import gameonlp.oredepos.tile.FluidHandlerTile;
import gameonlp.oredepos.util.EnergyCell;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.fluid.Fluid;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.templates.FluidTank;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.LinkedList;
import java.util.List;

public class MinerTile extends TileEntity implements ITickableTileEntity, EnergyHandlerTile, FluidHandlerTile {

    final EnergyCell energyCell = new EnergyCell(this, false, true, 16000);
    ItemStackHandler slots = createItemHandler();
    ItemStackHandler outputs = new ItemStackHandler(10){
        @Override
        public boolean isItemValid(int slot, @Nonnull ItemStack stack) {
            return slots.isItemValid(slot, stack);
        }

        @Override
        public int getSlotLimit(int slot) {
            return slots.getSlotLimit(slot);
        }

        @Override
        public int getSlots() {
            return slots.getSlots();
        }

        @Override
        public ItemStack getStackInSlot(int slot) {
            return slots.getStackInSlot(slot);
        }

        @Override
        public ItemStack insertItem(int slot, ItemStack stack, boolean simulate) {
            if (slot <= 5) {
                return stack;
            }
            return slots.insertItem(slot, stack, simulate);
        }

        @Override
        public void setStackInSlot(int slot, @Nonnull ItemStack stack) {
            setChanged();
            slots.setStackInSlot(slot, stack);
        }

        @Override
        public ItemStack extractItem(int slot, int amount, boolean simulate) {
            return slots.extractItem(slot, amount, simulate);
        }
    };

    int fluidCapacity = 4000;
    FluidTank fluidTank = new CustomFluidTank(this, fluidCapacity);

    LazyOptional<ItemStackHandler> itemHandler = LazyOptional.of(() -> outputs);
    LazyOptional<IFluidHandler> fluidHandler = LazyOptional.of(() -> fluidTank);
    LazyOptional<IEnergyStorage> energyHandler = LazyOptional.of(() -> energyCell);

    int progress;


    protected MinerTile(TileEntityType<?> p_i48289_1_) {
        super(p_i48289_1_);
    }

    public MinerTile() {
        this(RegistryManager.MINER_TILE.get());
    }

    private ItemStackHandler createItemHandler() {
        return new ItemStackHandler(10){
            @Override
            protected void onContentsChanged(int slot) {
                super.onContentsChanged(slot);
                setChanged();
            }

            @Override
            public boolean isItemValid(int slot, @Nonnull ItemStack stack) {
                if (slot <= 5) {
                    return true;
                }
                if (slot == 6){
                    return stack.getItem() instanceof DrillHeadItem;
                }
                return stack.getItem() instanceof ModuleItem;
            }
        };
    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        if (side != Direction.DOWN) {
            if (CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.equals(cap)) {
                return itemHandler.cast();
            }
            if (CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY.equals(cap)){
                return fluidHandler.cast();
            }
            if (CapabilityEnergy.ENERGY.equals(cap)){
                return energyHandler.cast();
            }
        }
        return super.getCapability(cap, side);
    }

    @Override
    protected void invalidateCaps() {
        super.invalidateCaps();
        itemHandler.invalidate();
        fluidHandler.invalidate();
        energyHandler.invalidate();
    }

    @Override
    public CompoundNBT save(CompoundNBT p_189515_1_) {
        CompoundNBT tag = super.save(p_189515_1_);
        tag.putInt("energy", energyCell.getEnergyStored());
        tag.putInt("progress", progress);
        tag = fluidTank.writeToNBT(tag);
        tag.put("slots", slots.serializeNBT());
        return tag;
    }

    @Override
    public void load(BlockState p_230337_1_, CompoundNBT p_230337_2_) {
        super.load(p_230337_1_, p_230337_2_);
        energyCell.setEnergy(p_230337_2_.getInt("energy"));
        progress = p_230337_2_.getInt("progress");
        FluidStack fluid = FluidStack.loadFluidStackFromNBT(p_230337_2_);
        if (fluid == null){
            fluid = FluidStack.EMPTY;
        }
        fluidTank.setFluid(fluid);
        slots.deserializeNBT(p_230337_2_.getCompound("slots"));
    }

    @Override
    public void tick() {
        int energyDrain = 100; // TODO add config
        int fluidDrain = 100;
        if (level == null || level.isClientSide() || slots.getStackInSlot(6).equals(ItemStack.EMPTY)){
            return;
        }
        for (int slot = 0; slot <= 5; slot++) {
            if (slots.getStackInSlot(slot).getCount() != 0){
                return;
            }
        }
        List<OreDepositTile> deposits = new LinkedList<>();
        for (int x = -1; x <= 1; x++) {
            for (int y = 0; y > -3; y--) {
                for (int z = -1; z <= 1; z++) {
                    TileEntity depo = level.getBlockEntity(worldPosition.below().offset(x, y, z));
                    if (!(depo instanceof OreDepositTile)) {
                        continue;
                    }
                    OreDepositTile oreDepo = (OreDepositTile) depo;
                    DrillHeadItem drillHead = (DrillHeadItem) slots.getStackInSlot(6).getItem();
                    boolean sufficientMiningLevel = depo.getBlockState().getHarvestLevel() <= drillHead.getMiningLevel();
                    boolean corrrectTool = depo.getBlockState().getHarvestTool() == drillHead.getToolType() && depo.getBlockState().requiresCorrectToolForDrops();
                    Fluid fluid = oreDepo.fluidNeeded();
                    boolean correctFluid = fluid == null || fluid.equals(fluidTank.getFluid().getFluid());
                    boolean enoughFluid = fluid == null || fluidTank.drain(fluidDrain, IFluidHandler.FluidAction.SIMULATE).getAmount() >= fluidDrain ;
                    if (sufficientMiningLevel && corrrectTool && correctFluid && enoughFluid) {
                        deposits.add(oreDepo);
                    }
                }
            }
        }
        if (deposits.isEmpty()){
            return;
        }
        if (energyCell.getEnergyStored() >= energyDrain) {
            energyCell.setEnergy(energyCell.getEnergyStored() - energyDrain);
            progress++;
            this.setChanged();
        }
        if (progress == 20) { // TODO make reliant on module and block mined
            progress = 0;
            OreDepositTile depo = deposits.get(level.getRandom().nextInt(deposits.size()));
            List<ItemStack> drops = Block.getDrops(depo.getBlockState(), (ServerWorld) level, worldPosition, depo, null, slots.getStackInSlot(6));
            ItemStack leftover = ItemStack.EMPTY;
            for (ItemStack drop : drops) {
                for (int i = 0; i <= 5; i++) {
                    if (leftover.equals(ItemStack.EMPTY)) {
                        leftover = slots.insertItem(i, drop, false);
                    } else {
                        leftover = slots.insertItem(i, leftover, false);
                    }
                    if (leftover.equals(ItemStack.EMPTY)){
                        break;
                    }
                }
            }
            if (depo.fluidNeeded() != null){
                fluidTank.drain(fluidDrain, IFluidHandler.FluidAction.EXECUTE);
            }
            depo.decrement();
            this.setChanged();
        }
    }
    @Override
    public void setEnergy(int energy) {
        this.energyCell.setEnergy(energy);
    }

    @Override
    public void setFluid(FluidStack fluid) {
        this.fluidTank.setFluid(fluid);
    }
}
