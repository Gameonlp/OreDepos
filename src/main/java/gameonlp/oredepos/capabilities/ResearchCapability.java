package gameonlp.oredepos.capabilities;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.util.INBTSerializable;

public interface ResearchCapability extends INBTSerializable<CompoundTag> {
    void addResearchProgress(Player player, int red, int blue, int green, int yellow, int white);

    void mergeResearch(Player first, Player second);
}
