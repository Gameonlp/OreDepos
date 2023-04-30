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
import net.minecraftforge.event.level.BlockEvent.BreakEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber
public class MiningEventHandler {
	
	@SubscribeEvent
    public static void onBlockMined(BreakEvent event) {
		BlockState state = event.getState();
		ItemStack mainHand = event.getPlayer().getMainHandItem();
		boolean destroyed = !mainHand.isCorrectToolForDrops(state) || event.getPlayer().isCreative();

		if (state.getBlock() instanceof OreDepositBlock) {
			BlockEntity tileEntity = event.getLevel().getBlockEntity(event.getPos());
			if (tileEntity instanceof OreDepositTile tileEntityOre) {

				if (!event.getLevel().isClientSide() && !destroyed) {
					Block.dropResources(state, (Level)event.getLevel(), event.getPos(), tileEntityOre, event.getPlayer(), event.getPlayer().getMainHandItem());
					mainHand.hurtAndBreak(1, (ServerPlayer) event.getPlayer(), player -> {});
				}
				
				if (destroyed || tileEntityOre.isZero()) {
					tileEntityOre.setRemovable();
					event.getLevel().removeBlock(event.getPos(), false);
				} else {
					tileEntityOre.decrement();
					event.setCanceled(true);
				}
			}
		}
    }
}
