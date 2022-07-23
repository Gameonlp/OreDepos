package gameonlp.oredepos.events;

import gameonlp.oredepos.blocks.oredeposit.OreDepositBlock;
import gameonlp.oredepos.blocks.oredeposit.OreDepositTile;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.ToolAction;
import net.minecraftforge.event.world.BlockEvent.BreakEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber
public class MiningEventHandler {
	
	@SubscribeEvent
    public static void onBlockMined(BreakEvent event) {
		if (event.getPlayer().isCreative()) return;

		BlockState state = event.getState();
		ItemStack mainHand = event.getPlayer().getMainHandItem();
		boolean destroyed = !mainHand.isCorrectToolForDrops(state);

		if (state.getBlock() instanceof OreDepositBlock) {
			BlockEntity tileEntity = event.getWorld().getBlockEntity(event.getPos());
			if (tileEntity instanceof OreDepositTile tileEntityOre) {

				if (!event.getWorld().isClientSide() && !destroyed) {
					Block.dropResources(state, (Level)event.getWorld(), event.getPos(), tileEntityOre, event.getPlayer(), event.getPlayer().getMainHandItem());
					mainHand.hurtAndBreak(1, (ServerPlayer) event.getPlayer(), player -> {});
				}
				
				if (destroyed || tileEntityOre.isZero()) {
					event.getWorld().removeBlock(event.getPos(), false);
				} else {
					tileEntityOre.decrement();
					event.setCanceled(true);
				}
			}
		}
    }
}
