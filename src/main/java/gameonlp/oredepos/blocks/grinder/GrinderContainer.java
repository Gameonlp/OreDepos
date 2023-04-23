package gameonlp.oredepos.blocks.grinder;

import gameonlp.oredepos.RegistryManager;
import gameonlp.oredepos.items.ModuleItem;
import gameonlp.oredepos.net.*;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;
import net.minecraftforge.items.wrapper.InvWrapper;
import net.minecraftforge.network.PacketDistributor;

public class GrinderContainer extends AbstractContainerMenu {

    private final BlockEntity tileEntity;
    private final Player player;
    private final IItemHandler playerInventory;

    public IEnergyStorage getEnergy(){
        return tileEntity.getCapability(CapabilityEnergy.ENERGY).orElse(null);
    }

    public GrinderContainer(int windowId, Level world, BlockPos pos,
                            Inventory playerInventory, Player player) {
        super(RegistryManager.GRINDER_CONTAINER.get(), windowId);
        this.tileEntity = world.getBlockEntity(pos);
        if (tileEntity.getLevel() != null && !tileEntity.getLevel().isClientSide() && tileEntity instanceof GrinderTile){
            PacketManager.INSTANCE.send(PacketDistributor.ALL.noArg(), new PacketEnergySync(tileEntity.getBlockPos(), ((GrinderTile)tileEntity).energyCell.getEnergyStored()));
            PacketManager.INSTANCE.send(PacketDistributor.ALL.noArg(), new PacketProgressSync(tileEntity.getBlockPos(), ((GrinderTile) tileEntity).progress));
            PacketManager.INSTANCE.send(PacketDistributor.ALL.noArg(), new PacketProductivitySync(tileEntity.getBlockPos(), ((GrinderTile) tileEntity).productivity));
        }
        this.player = player;
        this.playerInventory = new InvWrapper(playerInventory);
        layoutPlayerInventorySlots(8, 84);

        if(tileEntity != null) {
            tileEntity.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).ifPresent(h -> {
                addSlot(new SlotItemHandler(h, 0, 76, 35));
                addSlot(new SlotItemHandler(h, 1, 30, 35));
                addSlot(new SlotItemHandler(h, 2, 116, 52));
                addSlot(new SlotItemHandler(h, 3, 134, 52));
                addSlot(new SlotItemHandler(h, 4, 152, 52));
            });
        }
    }
    @Override
    public boolean stillValid(Player p_75145_1_) {
        return stillValid(ContainerLevelAccess.create(tileEntity.getLevel(), tileEntity.getBlockPos()), player, RegistryManager.GRINDER.get());
    }

    private void layoutPlayerInventorySlots(int leftCol, int topRow) {
        addSlotBox(playerInventory, 9, leftCol, topRow, 9, 18, 3, 18);

        topRow += 58;
        addSlotRange(playerInventory, 0, leftCol, topRow, 9, 18);
    }

    private int addSlotRange(IItemHandler handler, int index, int x, int y, int amount, int dx) {
        for (int i = 0; i < amount; i++) {
            addSlot(new SlotItemHandler(handler, index, x, y));
            x += dx;
            index++;
        }

        return index;
    }

    private int addSlotBox(IItemHandler handler, int index, int x, int y, int horAmount, int dx, int verAmount, int dy) {
        for (int j = 0; j < verAmount; j++) {
            index = addSlotRange(handler, index, x, y, horAmount, dx);
            y += dy;
        }

        return index;
    }

    @Override
    public ItemStack quickMoveStack(Player playerIn, int index) {
        int playerStart = 0;
        int playerEnd = 36;
        int tileStart = playerEnd + 1;
        int tileEnd = playerEnd + 5;
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = this.slots.get(index);
        if (slot != null && slot.hasItem()) {
            ItemStack stack = slot.getItem();
            itemstack = stack.copy();
            if (index < playerEnd) {
                if (slot.getItem().getItem() instanceof ModuleItem) {
                    if (!this.moveItemStackTo(stack, tileStart + 1, tileEnd, false)) {
                        return ItemStack.EMPTY;
                    }
                } else {
                    if (!this.moveItemStackTo(stack, tileStart, tileEnd, false)) {
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
