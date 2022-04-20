package gameonlp.oredepos.events;

import gameonlp.oredepos.blocks.oredeposit.OreDepositBlock;
import gameonlp.oredepos.blocks.oredeposit.OreDepositTile;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
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
		int miningLevel = mainHand.getHarvestLevel(state.getHarvestTool(), event.getPlayer(), state);
		boolean destroyed = !mainHand.isCorrectToolForDrops(state) || miningLevel < state.getHarvestLevel();

		if (state.getBlock() instanceof OreDepositBlock) {
			TileEntity tileEntity = event.getWorld().getBlockEntity(event.getPos());
			if (tileEntity instanceof OreDepositTile) {
				OreDepositTile tileEntityOre = (OreDepositTile) tileEntity;
				
				if (!event.getWorld().isClientSide() && !destroyed) {
					Block.dropResources(state, (World)event.getWorld(), event.getPos(), tileEntityOre, event.getPlayer(), event.getPlayer().getMainHandItem());
					mainHand.hurt(1, event.getWorld().getRandom(), (ServerPlayerEntity) event.getPlayer());
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
