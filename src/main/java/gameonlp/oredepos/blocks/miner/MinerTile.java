package gameonlp.oredepos.blocks.miner;

import gameonlp.oredepos.RegistryManager;
import gameonlp.oredepos.blocks.BasicMachineTile;
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
import gameonlp.oredepos.util.PlayerInOutStackHandler;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.server.level.ServerLevel;
import net.minecraftforge.common.ForgeTier;
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
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.tags.ITag;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.*;

public class MinerTile extends BasicMachineTile implements EnergyHandlerTile, FluidHandlerTile, ModuleAcceptorTile {

    final EnergyCell energyCell = new EnergyCell(this, false, true, 16000);

    PlayerInOutStackHandler handler;
    int fluidCapacity = 4000;
    FluidTank fluidTank = new CustomFluidTank(this, fluidCapacity, 0);

    LazyOptional<ItemStackHandler> outputHandler = LazyOptional.of(() -> handler.getMachineAccessible());
    LazyOptional<ItemStackHandler> itemHandler = LazyOptional.of(() -> handler.getPlayerAccessible());
    LazyOptional<IFluidHandler> fluidHandler = LazyOptional.of(() -> fluidTank);
    LazyOptional<IEnergyStorage> energyHandler = LazyOptional.of(() -> energyCell);

    float progress;
    float maxProgress = 30;
    float productivity;

    List<Component> reason = Collections.emptyList();
    boolean hadReason;

    protected MinerTile(BlockEntityType<?> p_i48289_1_, BlockPos pos, BlockState state) {
        super(p_i48289_1_, pos, state);
        slots = createItemHandler();
        handler = new PlayerInOutStackHandler(this, slots, 6, 3);
    }

    public MinerTile(BlockPos pos, BlockState state) {
        this(RegistryManager.MINER_TILE.get(), pos, state);
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
                return stack.getItem() instanceof ModuleItem && ((ModuleItem) stack.getItem()).isAccepted(getName());
            }
        };
    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        if (side == null){
            if (CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.equals(cap)) {
                return itemHandler.cast();
            }
        }
        if (side != Direction.DOWN) {
            if (CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.equals(cap)) {
                return outputHandler.cast();
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
    public void invalidateCaps() {
        super.invalidateCaps();
        itemHandler.invalidate();
        fluidHandler.invalidate();
        energyHandler.invalidate();
    }

    @Override
    public void saveAdditional(CompoundTag tag) {
        super.saveAdditional(tag);
        tag.putInt("energy", energyCell.getEnergyStored());
        tag.putFloat("progress", progress);
        tag.putFloat("productivity", productivity);
        tag = fluidTank.writeToNBT(tag);
        tag.put("slots", slots.serializeNBT());
    }

    @Override
    public void load(CompoundTag p_230337_2_) {
        super.load(p_230337_2_);
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

    public static void serverTick(Level level, BlockPos blockPos, BlockState blockState, MinerTile e) {
        e.tick();
    }

    private void tick(){
        int energyDrain = 100; // TODO add config
        int fluidDrain = 100;
        if (level == null){
            return;
        }
        update();
        if (slots.getStackInSlot(6).equals(ItemStack.EMPTY)){
            if (!hadReason || level.getGameTime() % 20 == 0) {
                this.reason = Collections.singletonList(new TranslatableComponent("tooltip.oredepos.missing_drill"));
                hadReason = true;
                PacketManager.INSTANCE.send(PacketDistributor.ALL.noArg(), new PacketTooltipSync(worldPosition, reason));
            }
            return;
        }
        clearReason();
        for (int slot = 0; slot <= 5; slot++) {
            if (slots.getStackInSlot(slot).getCount() != 0){
                if (!hadReason || level.getGameTime() % 20 == 0) {
                    this.reason = Collections.singletonList(new TranslatableComponent("tooltip.oredepos.output_full"));
                    hadReason = true;
                    PacketManager.INSTANCE.send(PacketDistributor.ALL.noArg(), new PacketTooltipSync(worldPosition, reason));
                }
                return;
            }
        }
        clearReason();
        List<ModuleItem> modules = getModuleItems(7);
        List<OreDepositTile> deposits = new LinkedList<>();
        int lengthPriorReason = this.reason.size();

        int length = 0;
        int width = 0;
        int depth = 0;
        boolean inversion = false;
        float drain = energyDrain;
        float progressIncrease = 1.0f;
        float productivityIncrease = 0.0f;
        for (ModuleItem module : modules){
            length = module.getLength(length);
            width = module.getWidth(width);
            depth = module.getDepth(depth);
            inversion = module.getInversion(inversion);
            drain = module.getEnergyConsumption(drain);
            progressIncrease = module.getProgress(progressIncrease);
            productivityIncrease = module.getProductivity(productivityIncrease);
        }
        this.reason = findSuitableTiles(fluidDrain, deposits, width, length, depth, inversion);
        if (deposits.isEmpty()) {
            if (hadReason && lengthPriorReason == this.reason.size() && level.getGameTime() % 20 != 0){
                return;
            }
            if (this.reason.size() == 0){
                this.reason = Collections.singletonList(new TranslatableComponent("tooltip.oredepos.no_deposits"));
            }
            PacketManager.INSTANCE.send(PacketDistributor.ALL.noArg(), new PacketTooltipSync(worldPosition, reason));
            hadReason = true;
            return;
        }
        clearReason();
        if (energyCell.getEnergyStored() >= drain) {
            energyCell.setEnergy((int) (energyCell.getEnergyStored() - drain));
            progress += progressIncrease;
            PacketManager.INSTANCE.send(PacketDistributor.ALL.noArg(), new PacketProgressSync(worldPosition, progress));
            this.setChanged();
        }
        if (progress >= maxProgress - 0.0001f) {
            progress = 0;
            PacketManager.INSTANCE.send(PacketDistributor.ALL.noArg(), new PacketProgressSync(worldPosition, progress));
            OreDepositTile depo = deposits.get(level.getRandom().nextInt(deposits.size()));
            List<ItemStack> drops = Block.getDrops(depo.getBlockState(), (ServerLevel) level, worldPosition, depo, null, slots.getStackInSlot(6));
            Map<ItemStack, Integer> counts = new HashMap<>();
            for (ItemStack drop : drops) {
                counts.put(drop, drop.getCount());
            }
            if (productivity >= 1){
                productivity -= 1;
                PacketManager.INSTANCE.send(PacketDistributor.ALL.noArg(), new PacketProductivitySync(worldPosition, productivity));
                for (ItemStack drop : drops) {
                    drop.setCount(drop.getCount() + counts.get(drop));
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
            if (!depo.fluidNeeded().isEmpty()){
                fluidTank.drain(fluidDrain, IFluidHandler.FluidAction.EXECUTE);
            }
            depo.decrement();
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

    private List<Component> findSuitableTiles(int fluidDrain, List<OreDepositTile> deposits, int width, int length, int depth, boolean inversion) {
        List<List<Component>> reasons = new LinkedList<>();
        for (int x = -1 - width; x <= 1 + width; x++) {
            for (int y = inversion ? 1 : -1; (inversion && y <= 3 + depth) || (!inversion && y >= -3 - depth); y += inversion ? 1 : -1) {
                for (int z = -1 - length; z <= 1 + length; z++) {
                    if (level == null){
                        continue;
                    }
                    BlockEntity depo = level.getBlockEntity(worldPosition.offset(x, y, z));
                    if (!(depo instanceof OreDepositTile oreDepo)) {
                        continue;
                    }
                    if (!(slots.getStackInSlot(6).getItem() instanceof DrillHeadItem drillHead)){
                        continue;
                    }
                    BlockState depoBlock = depo.getBlockState();
                    boolean correctToolForDrops = drillHead.getCorresponding().isCorrectToolForDrops(depoBlock);
                    ITag<Fluid> fluid = oreDepo.fluidNeeded();
                    boolean correctFluid = fluid.isEmpty() || fluid.contains(fluidTank.getFluid().getFluid());
                    boolean enoughFluid = fluid.isEmpty() || fluidTank.drain(fluidDrain, IFluidHandler.FluidAction.SIMULATE).getAmount() >= fluidDrain;
                    if (correctToolForDrops && correctFluid && enoughFluid) {
                        deposits.add(oreDepo);
                    } else {
                        List<Component> currentReason = new LinkedList<>();
                        if (!correctToolForDrops) {
                            String correctTool = "?";
                            if (depoBlock.is(BlockTags.create(new ResourceLocation("minecraft:needs_iron_tool")))){
                                correctTool = "Iron ";
                            } else if (depoBlock.is(BlockTags.create(new ResourceLocation("minecraft:needs_diamond_tool")))){
                                correctTool = "Diamond ";
                            } else if (depoBlock.is(BlockTags.create(new ResourceLocation("forge:needs_netherite_tool")))){
                                correctTool = "Netherite ";
                            }
                            if(depoBlock.is(BlockTags.create(new ResourceLocation("minecraft:mineable/axe")))){
                                correctTool += "Axe";
                            } else if(depoBlock.is(BlockTags.create(new ResourceLocation("minecraft:mineable/pickaxe")))){
                                correctTool += "Pickaxe";
                            } else if(depoBlock.is(BlockTags.create(new ResourceLocation("minecraft:mineable/shovel")))){
                                correctTool += "Shovel";
                            } else if(depoBlock.is(BlockTags.create(new ResourceLocation("minecraft:mineable/hoe")))){
                                correctTool += "Hoe";
                            }
                            currentReason.add(new TranslatableComponent("tooltip.oredepos.incorrect_tool").append(": ").append(correctTool));
                        }
                        if (!correctFluid) {
                            currentReason.add(new TranslatableComponent("tooltip.oredepos.incorrect_fluid").append(": ").append(new FluidStack(fluid.getRandomElement(level.random).get(), 100).getDisplayName()));
                        }
                        if (!enoughFluid) {
                            currentReason.add(new TranslatableComponent("tooltip.oredepos.insufficient_fluid").append(": ").append(String.valueOf(fluidDrain)));
                        }
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
    public void setFluid(FluidStack fluid, int tank) {
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

    public static String getName() {
        return "miner";
    }

    public void setReason(List<Component> reason) {
        this.reason = reason;
    }
}
