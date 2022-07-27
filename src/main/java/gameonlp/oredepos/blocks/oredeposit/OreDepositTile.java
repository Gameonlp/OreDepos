package gameonlp.oredepos.blocks.oredeposit;

import gameonlp.oredepos.config.OreDeposConfig;
import gameonlp.oredepos.RegistryManager;
import net.minecraft.core.BlockPos;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.core.Vec3i;
import net.minecraft.world.level.storage.LevelData;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.tags.ITag;
import org.jetbrains.annotations.NotNull;

public class OreDepositTile extends BlockEntity {
    private int amount;
    private int maxAmount;
    private String fluid;
    private double factor;

    protected OreDepositTile(BlockEntityType<?> p_i48289_1_, BlockPos pos, BlockState state) {
        super(p_i48289_1_, pos, state);
    }

    public OreDepositTile(BlockPos pos, BlockState state){
        this(RegistryManager.ORE_DEPOSIT_TILE.get(), pos, state);
    }

    public OreDepositTile(BlockPos pos, BlockState state, String fluid, double factor){
        this(pos, state);
        this.fluid = fluid;
        this.factor = factor;
    }

    @Override
    public void onLoad() {
        if (amount == 0) {
            int min;
            int max;
            if (level != null) {
                LevelData worldInfo = level.getLevelData();
                float distance = worldPosition.distManhattan(new Vec3i(worldInfo.getXSpawn(), worldInfo.getYSpawn(), worldInfo.getZSpawn()));
                if (distance < OreDeposConfig.Common.shortDistance.get()) {
                    min = OreDeposConfig.Common.leastShortDistance.get();
                    max = OreDeposConfig.Common.mostShortDistance.get();
                    amount = (int) (min + distance / OreDeposConfig.Common.shortDistance.get() * (level.getRandom().nextInt(max - min)));
                } else if (distance < OreDeposConfig.Common.mediumDistance.get()) {
                    min = OreDeposConfig.Common.leastMediumDistance.get();
                    max = OreDeposConfig.Common.mostMediumDistance.get();
                    amount = (int) (min + distance / OreDeposConfig.Common.mediumDistance.get() * (level.getRandom().nextInt(max - min)));
                } else if (distance < OreDeposConfig.Common.longDistance.get()) {
                    min = OreDeposConfig.Common.leastLongDistance.get();
                    max = OreDeposConfig.Common.mostLongDistance.get();
                    amount = (int) (min + distance / OreDeposConfig.Common.longDistance.get() * (level.getRandom().nextInt(max - min)));
                    if(!OreDeposConfig.Common.longDistanceIncreasesFurther.get()) {
                        amount = Math.min(max, amount);
                    }
                }
            } else {
                amount = 1;
            }
            amount = (int) Math.max(amount * factor, 1);
            maxAmount = amount;
        }
    }

    public @NotNull ITag<Fluid> fluidNeeded(){
        TagKey<Fluid> needed = ForgeRegistries.FLUIDS.tags().createTagKey(new ResourceLocation(fluid));
        return ForgeRegistries.FLUIDS.tags().getTag(needed);
    }

    public void decrement() {
        --amount;
        this.setChanged();
        if (amount <= 0 && level != null){
            this.setRemovable();
            level.removeBlock(worldPosition, false);
        }
    }

    public boolean isZero() {
        return amount == 0;
    }

    public void setRemovable() {
        this.remove = true;
    }

    public boolean isRemovable(){
        return this.remove;
    }

    @Override
    public void saveAdditional(CompoundTag tag) {
        super.saveAdditional(tag);
        tag.putInt("amount", amount);
        tag.putInt("max_amount", maxAmount);
        tag.putString("fluid", fluid);
    }

    @Override
    public void load(CompoundTag p_230337_2_) {
        super.load(p_230337_2_);

        amount = p_230337_2_.getInt("amount");
        maxAmount = p_230337_2_.getInt("max_amount");
        fluid = p_230337_2_.getString("fluid");
    }

    public int getAmount() {
        return amount;
    }

    public int getMaxAmount() {
        return maxAmount;
    }
}
