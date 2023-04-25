package gameonlp.oredepos.blocks.smelter;

import gameonlp.oredepos.RegistryManager;
import gameonlp.oredepos.blocks.BasicContainer;
import gameonlp.oredepos.blocks.smelter.SmelterTile;
import gameonlp.oredepos.items.ModuleItem;
import gameonlp.oredepos.net.PacketEnergySync;
import gameonlp.oredepos.net.PacketManager;
import gameonlp.oredepos.net.PacketProductivitySync;
import gameonlp.oredepos.net.PacketProgressSync;
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

public class SmelterContainer extends BasicContainer {
    public IEnergyStorage getEnergy(){
        return tileEntity.getCapability(CapabilityEnergy.ENERGY).orElse(null);
    }

    @Override
    protected boolean correctTile() {
        return tileEntity instanceof SmelterTile;
    }

    @Override
    protected void sendInitialSync() {
        PacketManager.INSTANCE.send(PacketDistributor.ALL.noArg(), new PacketEnergySync(tileEntity.getBlockPos(), ((SmelterTile)tileEntity).energyCell.getEnergyStored()));
        PacketManager.INSTANCE.send(PacketDistributor.ALL.noArg(), new PacketProgressSync(tileEntity.getBlockPos(), ((SmelterTile) tileEntity).progress));
        PacketManager.INSTANCE.send(PacketDistributor.ALL.noArg(), new PacketProductivitySync(tileEntity.getBlockPos(), ((SmelterTile) tileEntity).productivity));
    }

    @Override
    protected void setUpSlots() {
        tileEntity.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).ifPresent(h -> {
            addSlot(new SlotItemHandler(h, 0, 76, 35));
            addSlot(new SlotItemHandler(h, 1, 30, 35));
            addSlot(new SlotItemHandler(h, 2, 116, 52));
            addSlot(new SlotItemHandler(h, 3, 134, 52));
            addSlot(new SlotItemHandler(h, 4, 152, 52));
        });
    }

    public SmelterContainer(int windowId, Level world, BlockPos pos,
                            Inventory playerInventory, Player player) {
        super(windowId, world, pos, playerInventory, player, 1, RegistryManager.SMELTER_CONTAINER.get());
    }
    @Override
    public boolean stillValid(Player p_75145_1_) {
        return stillValid(ContainerLevelAccess.create(tileEntity.getLevel(), tileEntity.getBlockPos()), player, RegistryManager.SMELTER.get());
    }
    public BlockEntity getTileEntity() {
        return tileEntity;
    }
}
