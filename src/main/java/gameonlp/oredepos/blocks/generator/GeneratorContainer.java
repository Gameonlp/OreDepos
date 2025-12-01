package gameonlp.oredepos.blocks.generator;

import gameonlp.oredepos.RegistryManager;
import gameonlp.oredepos.blocks.BasicContainer;
import gameonlp.oredepos.blocks.generator.GeneratorTile;
import gameonlp.oredepos.net.PacketEnergySync;
import gameonlp.oredepos.net.PacketManager;
import gameonlp.oredepos.net.PacketProductivitySync;
import gameonlp.oredepos.net.PacketProgressSync;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.items.SlotItemHandler;
import net.minecraftforge.network.PacketDistributor;

public class GeneratorContainer extends BasicContainer {

    @Override
    protected boolean correctTile() {
        return tileEntity instanceof GeneratorTile;
    }

    @Override
    protected void sendInitialSync() {
        PacketManager.INSTANCE.send(PacketDistributor.ALL.noArg(), new PacketEnergySync(tileEntity.getBlockPos(), ((GeneratorTile) tileEntity).getEnergyCell().getEnergyStored()));
        PacketManager.INSTANCE.send(PacketDistributor.ALL.noArg(), new PacketProgressSync(tileEntity.getBlockPos(), ((GeneratorTile) tileEntity).getProgress()));
        PacketManager.INSTANCE.send(PacketDistributor.ALL.noArg(), new PacketProductivitySync(tileEntity.getBlockPos(), ((GeneratorTile) tileEntity).getProductivity()));
    }

    @Override
    protected void setUpSlots() {
        tileEntity.getCapability(ForgeCapabilities.ITEM_HANDLER).ifPresent(h -> {
            addSlot(new SlotItemHandler(h, 0, 76, 35));
            addSlot(new SlotItemHandler(h, 1, 134, 52));
            addSlot(new SlotItemHandler(h, 2, 152, 52));
        });
    }

    public GeneratorContainer(int windowId, Level world, BlockPos pos,
                              Inventory playerInventory, Player player) {
        super(windowId, world, pos, playerInventory, player, -1, RegistryManager.GENERATOR_CONTAINER.get());
    }
    @Override
    public boolean stillValid(Player p_75145_1_) {
        return stillValid(ContainerLevelAccess.create(tileEntity.getLevel(), tileEntity.getBlockPos()), player, RegistryManager.GENERATOR.get());
    }

    public BlockEntity getTileEntity() {
        return tileEntity;
    }
}
