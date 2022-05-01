package gameonlp.oredepos.util;

import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;

public class PlayerInOutStackHandler {
    private final TileEntity tile;
    private final ItemStackHandler handler;
    private ItemStackHandler playerAccessible;
    private ItemStackHandler machineAccessible;

    private final int outputRange;

    public PlayerInOutStackHandler(TileEntity tile, ItemStackHandler handler, int outputRange){
        this.tile = tile;
        this.handler = handler;
        this.outputRange = outputRange;
        setupHandlers();
    }

    private void setupHandlers() {
        machineAccessible = new ItemStackHandler(outputRange - 1) {
            @Override
            public boolean isItemValid(int slot, @Nonnull ItemStack stack) {
                return handler.isItemValid(slot, stack);
            }

            @Override
            public int getSlotLimit(int slot) {
                return handler.getSlotLimit(slot);
            }

            @Override
            public ItemStack getStackInSlot(int slot) {
                return handler.getStackInSlot(slot);
            }

            @Override
            public ItemStack insertItem(int slot, ItemStack stack, boolean simulate) {
                if (slot < outputRange) {
                    return stack;
                }
                return handler.insertItem(slot, stack, simulate);
            }

            @Override
            public void setStackInSlot(int slot, @Nonnull ItemStack stack) {
                tile.setChanged();
                handler.setStackInSlot(slot, stack);
            }

            @Override
            public ItemStack extractItem(int slot, int amount, boolean simulate) {
                return handler.extractItem(slot, amount, simulate);
            }
        };
        playerAccessible = new ItemStackHandler(handler.getSlots()) {
            @Override
            public boolean isItemValid(int slot, @Nonnull ItemStack stack) {
                return handler.isItemValid(slot, stack);
            }

            @Override
            public int getSlotLimit(int slot) {
                return handler.getSlotLimit(slot);
            }

            @Override
            public int getSlots() {
                return handler.getSlots();
            }

            @Override
            public ItemStack getStackInSlot(int slot) {
                return handler.getStackInSlot(slot);
            }

            @Override
            public ItemStack insertItem(int slot, ItemStack stack, boolean simulate) {
                if (slot < outputRange) {
                    return stack;
                }
                return handler.insertItem(slot, stack, simulate);
            }

            @Override
            public void setStackInSlot(int slot, @Nonnull ItemStack stack) {
                tile.setChanged();
                handler.setStackInSlot(slot, stack);
            }

            @Override
            public ItemStack extractItem(int slot, int amount, boolean simulate) {
                return handler.extractItem(slot, amount, simulate);
            }
        };
    }

    public ItemStackHandler getMachineAccessible() {
        return machineAccessible;
    }

    public ItemStackHandler getPlayerAccessible() {
        return playerAccessible;
    }
}
