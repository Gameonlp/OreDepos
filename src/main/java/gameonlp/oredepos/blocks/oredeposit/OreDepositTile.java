package gameonlp.oredepos.blocks.oredeposit;

import gameonlp.oredepos.config.OreDeposConfig;
import gameonlp.oredepos.RegistryManager;
import net.minecraft.core.BlockPos;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.Level;
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

import java.util.function.Supplier;

public class OreDepositTile extends BlockEntity {
    private int cooldown;
    private long amount;
    private long maxAmount;
    private String fluid;
    private Supplier<Double> factor;

    protected OreDepositTile(BlockEntityType<?> p_i48289_1_, BlockPos pos, BlockState state) {
        super(p_i48289_1_, pos, state);
    }

    public OreDepositTile(BlockPos pos, BlockState state){
        this(RegistryManager.ORE_DEPOSIT_TILE.get(), pos, state);
    }

    public OreDepositTile(BlockPos pos, BlockState state, String fluid, Supplier<Double> factor){
        this(pos, state);
        this.fluid = fluid;
        this.factor = factor;
    }

    @Override
    public void onLoad() {
        if (amount == 0) {
            long min;
            long max = 1;
            if (level != null) {
                LevelData worldInfo = level.getLevelData();
                float distance = worldPosition.distManhattan(new Vec3i(worldInfo.getXSpawn(), worldInfo.getYSpawn(), worldInfo.getZSpawn()));
                try {
                    if (distance < OreDeposConfig.Common.shortDistance.get()) {
                        min = OreDeposConfig.Common.leastShortDistance.get();
                        max = OreDeposConfig.Common.mostShortDistance.get();
                        amount = Math.addExact(min, Math.multiplyExact((long) Math.ceil(distance / OreDeposConfig.Common.shortDistance.get() * factor.get()), (randomLong(min, max))));
                        amount = Math.addExact(min, (long) ((distance / OreDeposConfig.Common.shortDistance.get() * factor.get()) * randomLong(min, max)));
                    } else if (distance < OreDeposConfig.Common.mediumDistance.get()) {
                        min = OreDeposConfig.Common.leastMediumDistance.get();
                        max = OreDeposConfig.Common.mostMediumDistance.get();
                        amount = Math.addExact(min, Math.multiplyExact((long) Math.ceil(distance / OreDeposConfig.Common.mediumDistance.get() * factor.get()), (randomLong(min, max))));
                        amount = Math.addExact(min, (long) ((distance / OreDeposConfig.Common.mediumDistance.get() * factor.get()) * (randomLong(min, max))));
                    } else {
                        min = OreDeposConfig.Common.leastLongDistance.get();
                        max = OreDeposConfig.Common.mostLongDistance.get();
                        amount = Math.addExact(min, Math.multiplyExact((long) Math.ceil(distance / OreDeposConfig.Common.longDistance.get() * factor.get()), (randomLong(min, max))));
                        amount = Math.addExact(min, (long) ((distance / OreDeposConfig.Common.longDistance.get() * factor.get()) * (randomLong(min, max))));
                        if (!OreDeposConfig.Common.longDistanceIncreasesFurther.get()) {
                            amount = Math.min(max, amount);
                        }
                    }
                } catch (ArithmeticException a){
                    if (!OreDeposConfig.Common.longDistanceIncreasesFurther.get()) {
                        amount = Math.min(max, amount);
                    } else {
                        amount = Long.MAX_VALUE;
                    }
                }
            } else {
                amount = 1;
            }
            amount = Math.max(amount, 1);
            maxAmount = amount;
        }
    }

    private long randomLong(long min, long max) {
        return ((long) level.getRandom().nextInt((int) (max - min >> 8)) << 8) + level.getRandom().nextInt((int) (max - min));
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
        return this.remove || this.cooldown != 0;
    }

    @Override
    public void saveAdditional(CompoundTag tag) {
        super.saveAdditional(tag);
        tag.putLong("amount", amount);
        tag.putLong("max_amount", maxAmount);
        tag.putString("fluid", fluid);
    }

    @Override
    public void load(CompoundTag p_230337_2_) {
        super.load(p_230337_2_);

        amount = p_230337_2_.getLong("amount");
        maxAmount = p_230337_2_.getLong("max_amount");
        fluid = p_230337_2_.getString("fluid");
    }

    public long getAmount() {
        return amount;
    }

    public long getMaxAmount() {
        return maxAmount;
    }

    public static <E extends BlockEntity> void serverTick(Level level, BlockPos blockPos, BlockState blockState, OreDepositTile e) {
        if (level == null || level.isClientSide()) {
            return;
        }
        if (e.cooldown == 0) {
            return;
        }
        e.cooldown--;
    }

    public void regenerate(BlockPos blockPos, BlockState state) {
        level.setBlock(blockPos, state, 0);
        cooldown = 2;
    }
}
