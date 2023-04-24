package gameonlp.oredepos.events;

import gameonlp.oredepos.OreDepos;
import gameonlp.oredepos.blocks.oredeposit.OreDepositBlock;
import gameonlp.oredepos.blocks.oredeposit.OreDepositTile;
import gameonlp.oredepos.capabilities.ResearchCapability;
import gameonlp.oredepos.capabilities.ResearchProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.world.BlockEvent.BreakEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber
public class RegisterCapabilityEventHandler {

	@SubscribeEvent
	public void registerCaps(final RegisterCapabilitiesEvent event) {
		event.register(ResearchCapability.class);
	}

	@SubscribeEvent
	public void attachCapability(final AttachCapabilitiesEvent<Level> event) {
		event.addCapability(new ResourceLocation(OreDepos.MODID, "research"), ResearchProvider.INSTANCE);
	}
}
