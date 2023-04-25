package gameonlp.oredepos.blocks.chemicalplant;

import gameonlp.oredepos.RegistryManager;
import gameonlp.oredepos.blocks.BasicContainer;
import gameonlp.oredepos.items.DrillHeadItem;
import gameonlp.oredepos.items.ModuleItem;
import gameonlp.oredepos.net.*;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;
import net.minecraftforge.items.wrapper.InvWrapper;
import net.minecraftforge.network.PacketDistributor;

public class ChemicalPlantContainer extends BasicContainer {
    public IEnergyStorage getEnergy(){
        return tileEntity.getCapability(CapabilityEnergy.ENERGY).orElse(null);
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
        PacketManager.INSTANCE.send(PacketDistributor.ALL.noArg(), new PacketEnergySync(tileEntity.getBlockPos(), ((ChemicalPlantTile)tileEntity).energyCell.getEnergyStored()));
        PacketManager.INSTANCE.send(PacketDistributor.ALL.noArg(), new PacketProgressSync(tileEntity.getBlockPos(), ((ChemicalPlantTile) tileEntity).progress));
        PacketManager.INSTANCE.send(PacketDistributor.ALL.noArg(), new PacketProductivitySync(tileEntity.getBlockPos(), ((ChemicalPlantTile) tileEntity).productivity));
    }

    @Override
    protected void setUpSlots() {
        tileEntity.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).ifPresent(h -> {
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
