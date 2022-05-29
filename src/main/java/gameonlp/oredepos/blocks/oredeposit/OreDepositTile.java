package gameonlp.oredepos.blocks.oredeposit;

import gameonlp.oredepos.config.OreDeposConfig;
import gameonlp.oredepos.RegistryManager;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.tags.FluidTags;
import net.minecraft.tags.ITag;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.core.Vec3i;
import net.minecraft.world.level.storage.LevelData;
import net.minecraftforge.registries.ForgeRegistries;

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
                if (distance < OreDeposConfig.Server.shortDistance.get()) {
                    min = OreDeposConfig.Server.leastShortDistance.get();
                    max = OreDeposConfig.Server.mostShortDistance.get();
                    amount = (int) (min + distance / OreDeposConfig.Server.shortDistance.get() * (level.getRandom().nextInt(max - min)));
                } else if (distance < OreDeposConfig.Server.mediumDistance.get()) {
                    min = OreDeposConfig.Server.leastMediumDistance.get();
                    max = OreDeposConfig.Server.mostMediumDistance.get();
                    amount = (int) (min + distance / OreDeposConfig.Server.mediumDistance.get() * (level.getRandom().nextInt(max - min)));
                } else if (distance < OreDeposConfig.Server.longDistance.get()) {
                    min = OreDeposConfig.Server.leastLongDistance.get();
                    max = OreDeposConfig.Server.mostLongDistance.get();
                    amount = (int) (min + distance / OreDeposConfig.Server.longDistance.get() * (level.getRandom().nextInt(max - min)));
                    if(!OreDeposConfig.Server.longDistanceIncreasesFurther.get()) {
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

    public ITag<Fluid> fluidNeeded(){
        return FluidTags.getAllTags().getTagOrEmpty(new ResourceLocation(fluid));
    }

    public void decrement() {
        --amount;
        this.setChanged();
        if (amount <= 0 && level != null){
            level.removeBlock(worldPosition, false);
        }
    }

    public boolean isZero() {
        return amount == 0;
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
