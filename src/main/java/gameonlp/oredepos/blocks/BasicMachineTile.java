package gameonlp.oredepos.blocks;

import gameonlp.oredepos.blocks.beacon.BeaconTile;
import gameonlp.oredepos.items.ModuleItem;
import gameonlp.oredepos.net.PacketManager;
import gameonlp.oredepos.net.PacketProductivitySync;
import gameonlp.oredepos.net.PacketProgressSync;
import gameonlp.oredepos.net.PacketTooltipSync;
import gameonlp.oredepos.tile.EnergyHandlerTile;
import gameonlp.oredepos.tile.ModuleAcceptorTile;
import gameonlp.oredepos.util.EnergyCell;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.templates.FluidTank;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.network.PacketDistributor;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import static gameonlp.oredepos.util.OptionUtil.option;

public abstract class BasicMachineTile extends BlockEntity implements ModuleAcceptorTile, EnergyHandlerTile {

    protected EnergyCell energyCell;
    protected ItemStackHandler slots;
    protected float progress;
    protected List<BeaconTile> beacons;
    private List<Component> reason = Collections.emptyList();
    protected boolean hadReason;
    private List<BeaconTile> toAdd;
    private List<BeaconTile> toRemove;

    public float getProductivity() {
        return productivity;
    }

    protected float productivity;

    public EnergyCell getEnergyCell() {
        return energyCell;
    }

    public float getProgress() {
        return progress;
    }

    public float getMaxProgress() {
        return maxProgress;
    }

    protected float maxProgress = 30;
    protected List<ItemStack> leftoverItemStacks;
    protected List<FluidStack> leftoverFluidStacks;

    protected BasicMachineTile(BlockEntityType<?> p_i48289_1_, BlockPos pos, BlockState state) {
        super(p_i48289_1_, pos, state);
        beacons = new LinkedList<>();
        toAdd = new LinkedList<>();
        toRemove = new LinkedList<>();
        leftoverItemStacks = new LinkedList<>();
        leftoverFluidStacks = new LinkedList<>();
    }

    public ItemStackHandler getSlots() {
        return slots;
    }

    @NotNull
    public List<ModuleItem> getModuleItems(int moduleOffset) {
        List<ModuleItem> modules = new LinkedList<>();
        for (int i = moduleOffset; i < slots.getSlots(); i++) {
            ItemStack moduleStack = slots.getStackInSlot(i);
            if (!moduleStack.isEmpty() && moduleStack.getItem() instanceof ModuleItem){
                modules.add((ModuleItem) moduleStack.getItem());
            }
        }
        return modules;
    }

    protected void getModuleBoosts(List<ModuleItem> modules, ModuleItem.ModuleBoosts moduleBoosts) {
        List<ModuleItem> beaconModules = new LinkedList<>();
        for (BeaconTile beacon : beacons) {
            beaconModules.addAll(beacon.getModuleItems(0));
        }
        for (ModuleItem beaconModule : beaconModules) {
            beaconModule.getBoosts(moduleBoosts, true);
        }
        for (ModuleItem module : modules){
            module.getBoosts(moduleBoosts);
        }
    }

    protected float getDrain(List<ModuleItem> modules, float baseDrain) {
        ModuleItem.ModuleBoosts moduleBoosts = new ModuleItem.ModuleBoosts();
        getModuleBoosts(modules, moduleBoosts);

        return moduleBoosts.energy * baseDrain;
    }

    protected void increaseProgress(List<ModuleItem> modules, float drain, float time) {
        ModuleItem.ModuleBoosts moduleBoosts = new ModuleItem.ModuleBoosts();
        getModuleBoosts(modules, moduleBoosts);

        float progressRatio = time / maxProgress;
        if (energyCell.getEnergyStored() >= drain) {
            energyCell.setEnergy((int) (energyCell.getEnergyStored() - drain));
            progress += (moduleBoosts.progress / progressRatio);
            PacketManager.INSTANCE.send(PacketDistributor.ALL.noArg(), new PacketProgressSync(worldPosition, progress));
            this.setChanged();
        }
    }

    protected void increaseProductivity(List<ModuleItem> modules) {
        ModuleItem.ModuleBoosts moduleBoosts = new ModuleItem.ModuleBoosts();
        getModuleBoosts(modules, moduleBoosts);

        productivity += moduleBoosts.productivity;
        PacketManager.INSTANCE.send(PacketDistributor.ALL.noArg(), new PacketProductivitySync(worldPosition, productivity));
        this.setChanged();
    }

    public void handleOutputs(List<ItemStack> items, int slotBegin, int slotEnd) {
        handleOutputs(items, slotBegin, slotEnd, List.of(), List.of());
    }

    public void handleOutputs(List<ItemStack> items, int slotBegin, int slotEnd, List<FluidStack> fluidStacks, List<FluidTank> tanks) {
        if (productivity >= 1){
            productivity -= 1;
            PacketManager.INSTANCE.send(PacketDistributor.ALL.noArg(), new PacketProductivitySync(worldPosition, productivity));
            items = items.stream().map(ItemStack::copy).toList();
            for (ItemStack stack : items) {
                stack.setCount(stack.getCount() * 2);
            }
            for (FluidStack fluidStack : fluidStacks) {
                fluidStack.setAmount(fluidStack.getAmount() * 2);
            }
        }
        insertToSlots(slotBegin, slotEnd, leftoverItemStacks, leftoverFluidStacks, tanks);
        insertToSlots(slotBegin, slotEnd, items, fluidStacks, tanks);
    }

    public void eject(int slotBegin, int slotEnd) {
        List<IItemHandler> neighbours = new LinkedList<>();
        option(level.getBlockEntity(getBlockPos().offset(0, 1, 0))).ifPresent(b -> b.getCapability(ForgeCapabilities.ITEM_HANDLER, Direction.DOWN).ifPresent(neighbours::add));
        option(level.getBlockEntity(getBlockPos().offset(0, -1, 0))).ifPresent(b -> b.getCapability(ForgeCapabilities.ITEM_HANDLER, Direction.UP).ifPresent(neighbours::add));
        option(level.getBlockEntity(getBlockPos().offset(1, 0, 0))).ifPresent(b -> b.getCapability(ForgeCapabilities.ITEM_HANDLER, Direction.WEST).ifPresent(neighbours::add));
        option(level.getBlockEntity(getBlockPos().offset(-1, 0, 0))).ifPresent(b -> b.getCapability(ForgeCapabilities.ITEM_HANDLER, Direction.EAST).ifPresent(neighbours::add));
        option(level.getBlockEntity(getBlockPos().offset(0, 0, 1))).ifPresent(b -> b.getCapability(ForgeCapabilities.ITEM_HANDLER, Direction.NORTH).ifPresent(neighbours::add));
        option(level.getBlockEntity(getBlockPos().offset(0, 0, -1))).ifPresent(b -> b.getCapability(ForgeCapabilities.ITEM_HANDLER, Direction.SOUTH).ifPresent(neighbours::add));
        for (int slot = slotBegin; slot <= slotEnd; slot++) {
            for (IItemHandler neighbour : neighbours) {
                for (int i = 0; i < neighbour.getSlots(); i++) {
                    int c = slots.getStackInSlot(slot).getCount();
                    slots.insertItem(slot, neighbour.insertItem(i, slots.extractItem(slot, Math.min(slots.getStackInSlot(slot).getCount(), 1), false), false), false);
                    if (c > slots.getStackInSlot(slot).getCount()) {
                        return;
                    }
                }
            }
        }
    }

    private void insertToSlots(int slotBegin, int slotEnd, List<ItemStack> stacks, List<FluidStack> fluidStacks, List<FluidTank> tanks) {
        ItemStack leftover = ItemStack.EMPTY;
        for (ItemStack stack : stacks) {
            for (int i = slotBegin; i <= slotEnd; i++) {
                if (leftover.equals(ItemStack.EMPTY)) {
                    leftover = slots.insertItem(i, stack, false);
                } else {
                    leftover = slots.insertItem(i, leftover, false);
                }
                if (leftover.equals(ItemStack.EMPTY)){
                    break;
                }
            }
            if (!leftover.isEmpty()) {
                leftoverItemStacks.add(leftover);
            }
        }
        for (FluidStack fluidStack : fluidStacks) {
            for (FluidTank tank : tanks) {
                fluidStack.setAmount(fluidStack.getAmount() - tank.fill(fluidStack, IFluidHandler.FluidAction.EXECUTE));
                if (fluidStack.isEmpty()) {
                    break;
                }
            }
            if (!fluidStack.isEmpty()) {
                leftoverFluidStacks.add(fluidStack);
            }
        }
    }

    protected boolean isInventoryFull(List<ModuleItem> modules, List<ItemStack> items, int slotBegin, int slotEnd) {
        return isInventoryFull(modules, items, slotBegin, slotEnd, List.of(), List.of());
    }

    protected boolean isInventoryFull(List<ModuleItem> modules, List<ItemStack> items, int slotBegin, int slotEnd, List<FluidStack> fluidStacks, List<FluidTank> tanks) {
        ModuleItem.ModuleBoosts moduleBoosts = new ModuleItem.ModuleBoosts();
        getModuleBoosts(modules, moduleBoosts);

        items = new LinkedList<>(items);
        items.addAll(leftoverItemStacks);

        ItemStack leftover = ItemStack.EMPTY;
        for (ItemStack stack : items) {
            for (int slot = slotBegin; slot <= slotEnd; slot++) {
                if (!moduleBoosts.stacking) {
                    if (slots.getStackInSlot(slot).getCount() != 0) {
                        if (shouldUpdateReason()) {
                            this.setReason(Collections.singletonList(Component.translatable("tooltip.oredepos.output_full")));
                            hadReason = true;
                            PacketManager.INSTANCE.send(PacketDistributor.ALL.noArg(), new PacketTooltipSync(worldPosition, getReason()));
                        }
                        return true;
                    }
                } else {
                    if (leftover.equals(ItemStack.EMPTY)) {
                        leftover = slots.insertItem(slot, stack, true);
                    } else {
                        leftover = slots.insertItem(slot, leftover, true);
                    }
                    if (leftover.equals(ItemStack.EMPTY)){
                        break;
                    }
                }
            }
            if (!leftover.isEmpty()) {
                if (shouldUpdateReason()) {
                    this.setReason(Collections.singletonList(Component.translatable("tooltip.oredepos.output_full")));
                    hadReason = true;
                    PacketManager.INSTANCE.send(PacketDistributor.ALL.noArg(), new PacketTooltipSync(worldPosition, getReason()));
                }
                return true;
            }
        }
        for (FluidStack stack : fluidStacks.stream().map(FluidStack::copy).toList()) {
            for (FluidTank tank : tanks) {
                if (!moduleBoosts.stacking) {
                    if (!tank.isEmpty()) {
                        if (shouldUpdateReason()) {
                            this.setReason(Collections.singletonList(Component.translatable("tooltip.oredepos.output_full")));
                            hadReason = true;
                            PacketManager.INSTANCE.send(PacketDistributor.ALL.noArg(), new PacketTooltipSync(worldPosition, getReason()));
                        }
                        return true;
                    }
                } else {
                    stack.setAmount(stack.getAmount() - tank.fill(stack, IFluidHandler.FluidAction.SIMULATE));
                    if (stack.isEmpty()) {
                        break;
                    }
                }
            }
            if (!stack.isEmpty()) {
                if (shouldUpdateReason()) {
                    this.setReason(Collections.singletonList(Component.translatable("tooltip.oredepos.output_full")));
                    hadReason = true;
                    PacketManager.INSTANCE.send(PacketDistributor.ALL.noArg(), new PacketTooltipSync(worldPosition, getReason()));
                }
                return true;
            }
        }
        return false;
    }

    public final boolean shouldUpdateReason() {
        return !hadReason || level.getGameTime() % 20 == 0;
    }

    protected void clearReason() {
        if (hadReason){
            this.setReason(Collections.emptyList());
            hadReason = false;
            PacketManager.INSTANCE.send(PacketDistributor.ALL.noArg(), new PacketTooltipSync(worldPosition, getReason()));
        }
    }

    public void setEnergy(int energy) {
        this.energyCell.setEnergy(energy);
    }

    public void setProgress(float progress) {
        this.progress = progress;
    }

    public void setProductivity(float productivity) {
        this.productivity = productivity;
    }

    public void update() {
        beacons.addAll(toAdd);
        beacons.removeAll(toRemove);
        toAdd.clear();
        toRemove.clear();
    }

    @Override
    public void addBeacon(BeaconTile beacon) {
        if (!beacons.contains(beacon) && !toAdd.contains(beacon)) {
            toAdd.add(beacon);
        }
    }

    @Override
    public void removeBeacon(BeaconTile beacon) {
        toRemove.add(beacon);
    }

    public void setReason(List<Component> reason) {
        this.reason = reason;
    }

    public List<Component> getReason() {
        return reason;
    }
}
