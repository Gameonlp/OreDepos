package gameonlp.oredepos.blocks.beacon;

import gameonlp.oredepos.RegistryManager;
import gameonlp.oredepos.blocks.BasicContainer;
import gameonlp.oredepos.net.PacketEnergySync;
import gameonlp.oredepos.net.PacketManager;
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

public class BeaconContainer extends BasicContainer {
    public IEnergyStorage getEnergy(){
        return tileEntity.getCapability(ForgeCapabilities.ENERGY).orElse(null);
    }

    @Override
    protected boolean correctTile() {
        return tileEntity instanceof BeaconTile;
    }

    @Override
    protected void sendInitialSync() {
        PacketManager.INSTANCE.send(PacketDistributor.ALL.noArg(), new PacketEnergySync(tileEntity.getBlockPos(), ((BeaconTile) tileEntity).getEnergyCell().getEnergyStored()));
    }

    @Override
    protected void setUpSlots() {
        tileEntity.getCapability(ForgeCapabilities.ITEM_HANDLER).ifPresent(h -> {
            addSlot(new SlotItemHandler(h, 0, 70, 35));
            addSlot(new SlotItemHandler(h, 1, 88, 35));
        });
    }

    public BeaconContainer(int windowId, Level world, BlockPos pos,
                           Inventory playerInventory, Player player) {
        super(windowId, world, pos, playerInventory, player, -1, RegistryManager.BEACON_CONTAINER.get());
    }
    @Override
    public boolean stillValid(Player p_75145_1_) {
        return stillValid(ContainerLevelAccess.create(tileEntity.getLevel(), tileEntity.getBlockPos()), player, RegistryManager.BEACON.get());
    }

    public BlockEntity getTileEntity() {
        return tileEntity;
    }
}
