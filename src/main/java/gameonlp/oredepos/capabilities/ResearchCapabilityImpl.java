package gameonlp.oredepos.capabilities;

import net.minecraft.client.Minecraft;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.entity.player.Player;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class ResearchCapabilityImpl implements ResearchCapability {
    Map<String, ResearchProgress> progressMap;
    public ResearchCapabilityImpl() {
        progressMap = new HashMap<>();
    }

    @Override
    public void addResearchProgress(Player player, int red, int blue, int green, int yellow, int white) {

    }

    @Override
    public void mergeResearch(Player first, Player second) {
        ResearchProgress researchProgress = progressMap.get(first.getName().getContents());
        progressMap.put(second.getName().getContents(), researchProgress.merge(progressMap.get(second.getName().getContents())));
    }

    @Override
    public CompoundTag serializeNBT() {
        CompoundTag result = new CompoundTag();
        ListTag listTag = new ListTag();
        progressMap.forEach((P, R) -> {
            CompoundTag tag = new CompoundTag();
            tag.putString("player", P);
            tag.put("research", R.serializeNBT());
            listTag.add(tag);
        });
        result.put("researchProgress", listTag);
        result.putInt("length", listTag.size());
        return result;
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        nbt.getList("researchProgress", nbt.getInt("length")).forEach(
            tag -> {
                if (tag instanceof CompoundTag compoundTag) {
                    ResearchProgress researchProgress = new ResearchProgress();
                    researchProgress.deserializeNBT((CompoundTag) compoundTag.get("research"));
                    progressMap.put(compoundTag.getString("player"), researchProgress);
                }
            }
        );
    }
}
