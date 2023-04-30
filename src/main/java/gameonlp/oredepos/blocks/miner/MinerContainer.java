package gameonlp.oredepos.blocks.miner;

import gameonlp.oredepos.RegistryManager;
import gameonlp.oredepos.blocks.BasicContainer;
import gameonlp.oredepos.items.DrillHeadItem;
import gameonlp.oredepos.items.ModuleItem;
import gameonlp.oredepos.net.*;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.items.SlotItemHandler;
import net.minecraftforge.network.PacketDistributor;

public class MinerContainer extends BasicContainer {
    public IEnergyStorage getEnergy(){
        return tileEntity.getCapability(ForgeCapabilities.ENERGY).orElse(null);
    }

    @Override
    protected boolean correctTile() {
        return tileEntity instanceof MinerTile;
    }

    @Override
    protected void sendInitialSync() {
        PacketManager.INSTANCE.send(PacketDistributor.ALL.noArg(), new PacketFluidSync(tileEntity.getBlockPos(), ((MinerTile)tileEntity).fluidTank.getFluid(), 0));
        PacketManager.INSTANCE.send(PacketDistributor.ALL.noArg(), new PacketEnergySync(tileEntity.getBlockPos(), ((MinerTile)tileEntity).energyCell.getEnergyStored()));
        PacketManager.INSTANCE.send(PacketDistributor.ALL.noArg(), new PacketProgressSync(tileEntity.getBlockPos(), ((MinerTile) tileEntity).progress));
        PacketManager.INSTANCE.send(PacketDistributor.ALL.noArg(), new PacketProductivitySync(tileEntity.getBlockPos(), ((MinerTile) tileEntity).productivity));
    }

    @Override
    protected void setUpSlots() {
        tileEntity.getCapability(ForgeCapabilities.ITEM_HANDLER).ifPresent(h -> {
            addSlot(new SlotItemHandler(h, 0, 8, 16));
            addSlot(new SlotItemHandler(h, 1, 26, 16));
            addSlot(new SlotItemHandler(h, 2, 8, 34));
            addSlot(new SlotItemHandler(h, 3, 26, 34));
            addSlot(new SlotItemHandler(h, 4, 8, 52));
            addSlot(new SlotItemHandler(h, 5, 26, 52));
            addSlot(new SlotItemHandler(h, 6, 80, 34));
            addSlot(new SlotItemHandler(h, 7, 116, 52));
            addSlot(new SlotItemHandler(h, 8, 134, 52));
            addSlot(new SlotItemHandler(h, 9, 152, 52));
        });
    }

    public MinerContainer(int windowId, Level world, BlockPos pos,
                          Inventory playerInventory, Player player) {
        super(windowId, world, pos, playerInventory, player, 5, RegistryManager.MINER_CONTAINER.get());
    }
    @Override
    public boolean stillValid(Player p_75145_1_) {
        return stillValid(ContainerLevelAccess.create(tileEntity.getLevel(), tileEntity.getBlockPos()), player, RegistryManager.MINER.get());
    }

    @Override
    public ItemStack quickMoveStack(Player playerIn, int index) {
        int playerStart = 0;
        int playerEnd = 36;
        int tileStart = playerEnd + 1;
        int tileEnd = playerEnd + 11;
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = this.slots.get(index);
        if (slot != null && slot.hasItem()) {
            ItemStack stack = slot.getItem();
            itemstack = stack.copy();
            if (index < playerEnd) {
                if (slot.getItem().getItem() instanceof DrillHeadItem){
                    if (!this.moveItemStackTo(stack, tileStart + 5, tileEnd, false)) {
                        return ItemStack.EMPTY;
                    }
                } else if (slot.getItem().getItem() instanceof ModuleItem) {
                    if (!this.moveItemStackTo(stack, tileStart + 5, tileEnd, false)) {
                        return ItemStack.EMPTY;
                    }
                }
                return ItemStack.EMPTY;
            } else if (index <= tileEnd && !this.moveItemStackTo(stack, playerStart, playerEnd, false)) {
                return ItemStack.EMPTY;
            }
            if (stack.isEmpty()) {
                slot.set(ItemStack.EMPTY);
            } else {
                slot.setChanged();
            }
            slot.onTake(playerIn, stack);
        }
        return itemstack;
    }
    public BlockEntity getTileEntity() {
        return tileEntity;
    }
}
