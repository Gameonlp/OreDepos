package gameonlp.oredepos.blocks.chemicalplant;

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

public class ChemicalPlantContainer extends BasicContainer {
    public IEnergyStorage getEnergy(){
        return tileEntity.getCapability(ForgeCapabilities.ENERGY).orElse(null);
    }

    public ChemicalPlantContainer(int windowId, Level world, BlockPos pos,
                          Inventory playerInventory, Player player) {
        super(windowId, world, pos, playerInventory, player, 2, RegistryManager.CHEMICAL_PLANT_CONTAINER.get());
    }

    @Override
    protected boolean correctTile() {
        return tileEntity instanceof ChemicalPlantTile;
    }

    @Override
    protected void sendInitialSync() {
        PacketManager.INSTANCE.send(PacketDistributor.ALL.noArg(), new PacketFluidSync(tileEntity.getBlockPos(), ((ChemicalPlantTile)tileEntity).fluidTank.getFluid(), 0));
        PacketManager.INSTANCE.send(PacketDistributor.ALL.noArg(), new PacketFluidSync(tileEntity.getBlockPos(), ((ChemicalPlantTile)tileEntity).primaryInputTank.getFluid(), 1));
        PacketManager.INSTANCE.send(PacketDistributor.ALL.noArg(), new PacketFluidSync(tileEntity.getBlockPos(), ((ChemicalPlantTile)tileEntity).secondaryInputTank.getFluid(), 2));
        PacketManager.INSTANCE.send(PacketDistributor.ALL.noArg(), new PacketEnergySync(tileEntity.getBlockPos(), ((ChemicalPlantTile) tileEntity).getEnergyCell().getEnergyStored()));
        PacketManager.INSTANCE.send(PacketDistributor.ALL.noArg(), new PacketProgressSync(tileEntity.getBlockPos(), ((ChemicalPlantTile) tileEntity).getProgress()));
        PacketManager.INSTANCE.send(PacketDistributor.ALL.noArg(), new PacketProductivitySync(tileEntity.getBlockPos(), ((ChemicalPlantTile) tileEntity).getProductivity()));
    }

    @Override
    protected void setUpSlots() {
        tileEntity.getCapability(ForgeCapabilities.ITEM_HANDLER).ifPresent(h -> {
            addSlot(new SlotItemHandler(h, 0, 96, 52));
            addSlot(new SlotItemHandler(h, 1, 50, 23));
            addSlot(new SlotItemHandler(h, 2, 50, 41));
            addSlot(new SlotItemHandler(h, 3, 116, 52));
            addSlot(new SlotItemHandler(h, 4, 134, 52));
            addSlot(new SlotItemHandler(h, 5, 152, 52));
        });
    }

    @Override
    public boolean stillValid(Player p_75145_1_) {
        return stillValid(ContainerLevelAccess.create(tileEntity.getLevel(), tileEntity.getBlockPos()), player, RegistryManager.CHEMICAL_PLANT.get());
    }
    public BlockEntity getTileEntity() {
        return tileEntity;
    }
}
