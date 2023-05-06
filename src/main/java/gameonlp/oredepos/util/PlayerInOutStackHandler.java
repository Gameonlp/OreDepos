package gameonlp.oredepos.util;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;

public class PlayerInOutStackHandler {
    private final BlockEntity tile;
    private final ItemStackHandler handler;
    private ItemStackHandler playerAccessible;
    private ItemStackHandler machineAccessible;

    private final int outputRange;
    private int inputRange;

    public PlayerInOutStackHandler(BlockEntity tile, ItemStackHandler handler, int outputRange, int inputRange){
        this.tile = tile;
        this.handler = handler;
        this.outputRange = outputRange;
        this.inputRange = inputRange;
        setupHandlers();
    }

    private void setupHandlers() {
        machineAccessible = new ItemStackHandler(outputRange + inputRange) {
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
                if (slot < outputRange) {
                    return handler.extractItem(slot, amount, simulate);
                } else {
                    return ItemStack.EMPTY;
                }
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
