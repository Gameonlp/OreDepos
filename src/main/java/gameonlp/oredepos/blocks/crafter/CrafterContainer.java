package gameonlp.oredepos.blocks.crafter;

import gameonlp.oredepos.RegistryManager;
import gameonlp.oredepos.blocks.BasicContainer;
import gameonlp.oredepos.net.*;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.items.SlotItemHandler;
import net.minecraftforge.network.PacketDistributor;

public class CrafterContainer extends BasicContainer {
    public IEnergyStorage getEnergy(){
        return tileEntity.getCapability(ForgeCapabilities.ENERGY).orElse(null);
    }

    @Override
    protected boolean correctTile() {
        return tileEntity instanceof CrafterTile;
    }

    @Override
    protected void sendInitialSync() {
        PacketManager.INSTANCE.send(PacketDistributor.ALL.noArg(), new PacketEnergySync(tileEntity.getBlockPos(), ((CrafterTile) tileEntity).getEnergyCell().getEnergyStored()));
        PacketManager.INSTANCE.send(PacketDistributor.ALL.noArg(), new PacketProgressSync(tileEntity.getBlockPos(), ((CrafterTile) tileEntity).getProgress()));
        PacketManager.INSTANCE.send(PacketDistributor.ALL.noArg(), new PacketProductivitySync(tileEntity.getBlockPos(), ((CrafterTile) tileEntity).getProductivity()));
        PacketManager.INSTANCE.send(PacketDistributor.ALL.noArg(), new PacketLockedSync(tileEntity.getBlockPos(), ((CrafterTile) tileEntity).locked));
        if (((CrafterTile) tileEntity).currentRecipe != null) {
            PacketManager.INSTANCE.send(PacketDistributor.ALL.noArg(), new PacketRecipeSync(tileEntity.getBlockPos(), ((CrafterTile) tileEntity).currentRecipe.getId()));
        }
    }

    @Override
    protected void setUpSlots() {
        tileEntity.getCapability(ForgeCapabilities.ITEM_HANDLER).ifPresent(h -> {
            addSlot(new SlotItemHandler(h, 0, 95, 34));
            addSlot(new SlotItemHandler(h, 1, 8, 73));
            addSlot(new SlotItemHandler(h, 2, 26, 73));
            addSlot(new SlotItemHandler(h, 3, 44, 73));
            addSlot(new SlotItemHandler(h, 4, 62, 73));
            addSlot(new SlotItemHandler(h, 5, 80, 73));
            addSlot(new SlotItemHandler(h, 6, 98, 73));
            addSlot(new SlotItemHandler(h, 7, 116, 73));
            addSlot(new SlotItemHandler(h, 8, 134, 73));
            addSlot(new SlotItemHandler(h, 9, 152, 73));
            addSlot(new SlotItemHandler(h, 10, 116, 52));
            addSlot(new SlotItemHandler(h, 11, 134, 52));
            addSlot(new SlotItemHandler(h, 12, 152, 52));
        });
    }

    public CrafterContainer(int windowId, Level world, BlockPos pos,
                            Inventory playerInventory, Player player) {
        super(windowId, world, pos, playerInventory, player, 9, RegistryManager.CRAFTER_CONTAINER.get(), 8, 108);
    }
    @Override
    public boolean stillValid(Player p_75145_1_) {
        return stillValid(ContainerLevelAccess.create(tileEntity.getLevel(), tileEntity.getBlockPos()), player, tileEntity.getBlockState().getBlock()) &&
                tileEntity.getLevel().getBlockState(tileEntity.getBlockPos()).is(RegistryManager.RUDIMENTARY_CRAFTER.get()) ||
                tileEntity.getLevel().getBlockState(tileEntity.getBlockPos()).is(RegistryManager.TINY_CRAFTER.get()) ||
                tileEntity.getLevel().getBlockState(tileEntity.getBlockPos()).is(RegistryManager.SIMPLE_CRAFTER.get()) ||
                tileEntity.getLevel().getBlockState(tileEntity.getBlockPos()).is(RegistryManager.CRAFTER.get()) ||
                tileEntity.getLevel().getBlockState(tileEntity.getBlockPos()).is(RegistryManager.FULL_CRAFTER.get());
    }

    public BlockEntity getTileEntity() {
        return tileEntity;
    }
}
