package gameonlp.oredepos.blocks.miner;

import gameonlp.oredepos.RegistryManager;
import gameonlp.oredepos.net.PacketManager;
import gameonlp.oredepos.net.PacketProductivitySync;
import gameonlp.oredepos.net.PacketProgressSync;
import gameonlp.oredepos.net.PacketTooltipSync;
import gameonlp.oredepos.tile.ModuleAcceptorTile;
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
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.templates.FluidTank;
import net.minecraftforge.fml.network.PacketDistributor;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

public class MinerTile extends TileEntity implements ITickableTileEntity, EnergyHandlerTile, FluidHandlerTile, ModuleAcceptorTile {

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

    float progress;
    float maxProgress = 30;
    float productivity;

    List<ITextComponent> reason = Collections.emptyList();
    boolean hadReason;

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
        tag.putFloat("progress", progress);
        tag.putFloat("producitivity", productivity);
        tag = fluidTank.writeToNBT(tag);
        tag.put("slots", slots.serializeNBT());
        return tag;
    }

    @Override
    public void load(BlockState p_230337_1_, CompoundNBT p_230337_2_) {
        super.load(p_230337_1_, p_230337_2_);
        energyCell.setEnergy(p_230337_2_.getInt("energy"));
        progress = p_230337_2_.getFloat("progress");
        productivity = p_230337_2_.getFloat("productivity");
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
        if (level == null || level.isClientSide()){
            return;
        }
        if (slots.getStackInSlot(6).equals(ItemStack.EMPTY)){
            if (!hadReason || level.getGameTime() % 20 == 0) {
                this.reason = Collections.singletonList(new TranslationTextComponent("tooltip.oredepos.missing_drill"));
                hadReason = true;
                PacketManager.INSTANCE.send(PacketDistributor.ALL.noArg(), new PacketTooltipSync(worldPosition, reason));
            }
            return;
        }
        clearReason();
        for (int slot = 0; slot <= 5; slot++) {
            if (slots.getStackInSlot(slot).getCount() != 0){
                if (!hadReason || level.getGameTime() % 20 == 0) {
                    this.reason = Collections.singletonList(new TranslationTextComponent("tooltip.oredepos.output_full"));
                    hadReason = true;
                    PacketManager.INSTANCE.send(PacketDistributor.ALL.noArg(), new PacketTooltipSync(worldPosition, reason));
                }
                return;
            }
        }
        clearReason();
        List<OreDepositTile> deposits = new LinkedList<>();
        int lengthPriorReason = this.reason.size();
        this.reason = findSuitableTiles(fluidDrain, deposits);
        if (deposits.isEmpty()) {
            if (hadReason && lengthPriorReason == this.reason.size() && level.getGameTime() % 20 != 0){
                return;
            }
            if (this.reason.size() == 0){
                this.reason = Collections.singletonList(new TranslationTextComponent("tooltip.oredepos.no_deposits"));
            }
            PacketManager.INSTANCE.send(PacketDistributor.ALL.noArg(), new PacketTooltipSync(worldPosition, reason));
            hadReason = true;
            return;
        }
        clearReason();
        List<ModuleItem> modules = new LinkedList<>();
        ItemStack mod1 = slots.getStackInSlot(7);
        ItemStack mod2 = slots.getStackInSlot(8);
        ItemStack mod3 =  slots.getStackInSlot(9);
        if (!mod1.isEmpty()){
            modules.add((ModuleItem) mod1.getItem());
        }
        if (!mod2.isEmpty()){
            modules.add((ModuleItem) mod2.getItem());
        }
        if (!mod3.isEmpty()){
            modules.add((ModuleItem) mod3.getItem());
        }
        float drain = energyDrain;
        for (ModuleItem module : modules){
            drain = module.getEnergyConsumption(drain);
        }
        if (energyCell.getEnergyStored() >= drain) {
            energyCell.setEnergy((int) (energyCell.getEnergyStored() - drain));
            float progressIncrease = 1.0f;
            for (ModuleItem module : modules){
                progressIncrease = module.getProgress(progressIncrease);
            }
            progress += progressIncrease;
            PacketManager.INSTANCE.send(PacketDistributor.ALL.noArg(), new PacketProgressSync(worldPosition, progress));
            this.setChanged();
        }
        if (progress >= maxProgress) {
            progress -= maxProgress;
            PacketManager.INSTANCE.send(PacketDistributor.ALL.noArg(), new PacketProgressSync(worldPosition, progress));
            OreDepositTile depo = deposits.get(level.getRandom().nextInt(deposits.size()));
            List<ItemStack> drops = Block.getDrops(depo.getBlockState(), (ServerWorld) level, worldPosition, depo, null, slots.getStackInSlot(6));
            if (productivity >= 1){
                productivity -= 1;
                PacketManager.INSTANCE.send(PacketDistributor.ALL.noArg(), new PacketProductivitySync(worldPosition, productivity));
                for (ItemStack drop : drops) {
                    drop.setCount(drop.getCount() * 2); // might not work
                }
            }
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
            float productivityIncrease = 0.0f;
            for (ModuleItem module : modules) {
                productivityIncrease = module.getProductivity(productivityIncrease);
            }
            productivity += productivityIncrease;
            PacketManager.INSTANCE.send(PacketDistributor.ALL.noArg(), new PacketProductivitySync(worldPosition, productivity));
            this.setChanged();
        }
    }

    private void clearReason() {
        if (hadReason){
            this.reason = Collections.emptyList();
            hadReason = false;
            PacketManager.INSTANCE.send(PacketDistributor.ALL.noArg(), new PacketTooltipSync(worldPosition, reason));
        }
    }

    private List<ITextComponent> findSuitableTiles(int fluidDrain, List<OreDepositTile> deposits) {
        List<List<ITextComponent>> reasons = new LinkedList<>();
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
                    boolean correctTool = depo.getBlockState().getHarvestTool() == drillHead.getToolType() && depo.getBlockState().requiresCorrectToolForDrops();
                    Fluid fluid = oreDepo.fluidNeeded();
                    boolean correctFluid = fluid == null || fluid.equals(fluidTank.getFluid().getFluid());
                    boolean enoughFluid = fluid == null || fluidTank.drain(fluidDrain, IFluidHandler.FluidAction.SIMULATE).getAmount() >= fluidDrain;
                    if (sufficientMiningLevel && correctTool && correctFluid && enoughFluid) {
                        deposits.add(oreDepo);
                    } else {
                        List<ITextComponent> currentReason = new LinkedList<>();
                        if (!sufficientMiningLevel)
                            currentReason.add(new TranslationTextComponent("tooltip.oredepos.insufficient_level").append(": ").append(String.valueOf(depo.getBlockState().getHarvestLevel())));
                        if (!correctTool)
                            currentReason.add(new TranslationTextComponent("tooltip.oredepos.incorrect_tool").append(": ").append(depo.getBlockState().getHarvestTool().getName()));
                        if (!correctFluid)
                            currentReason.add(new TranslationTextComponent("tooltip.oredepos.incorrect_fluid").append(": ").append(new FluidStack(fluid, 1000).getDisplayName()));
                        if (!enoughFluid)
                            currentReason.add(new TranslationTextComponent("tooltip.oredepos.insufficient_fluid").append(": ").append(String.valueOf(fluidDrain)));
                        reasons.add(currentReason);
                    }
                }
            }
        }
        reasons.sort(Comparator.comparingInt(List::size));
        return reasons.size() > 0 ? reasons.get(0) : Collections.emptyList();
    }

    @Override
    public void setEnergy(int energy) {
        this.energyCell.setEnergy(energy);
    }

    @Override
    public void setFluid(FluidStack fluid) {
        this.fluidTank.setFluid(fluid);
    }

    @Override
    public void setProgress(float progress) {
        this.progress = progress;
    }
    @Override
    public void setProductivity(float productivity) {
        this.productivity = productivity;
    }
    public void setReason(List<ITextComponent> reason) {
        this.reason = reason;
    }
}
