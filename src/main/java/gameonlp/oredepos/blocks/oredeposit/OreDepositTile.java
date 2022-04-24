package gameonlp.oredepos.blocks.oredeposit;

import gameonlp.oredepos.config.OreDeposConfig;
import gameonlp.oredepos.RegistryManager;
import net.minecraft.block.BlockState;
import net.minecraft.fluid.Fluid;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.math.vector.Vector3i;
import net.minecraft.world.storage.IWorldInfo;

public class OreDepositTile extends TileEntity {
    private int amount;
    private int maxAmount;
    private Fluid fluid;
    private Double factor;

    protected OreDepositTile(TileEntityType<?> p_i48289_1_) {
        super(p_i48289_1_);
    }

    public OreDepositTile(){
        this(RegistryManager.ORE_DEPOSIT_TILE.get());
    }

    public OreDepositTile(Fluid fluid, double factor){
        this();
        this.fluid = fluid;
        this.factor = factor;
    }

    @Override
    public void onLoad() {
        if (amount == 0) {
            int min = 0;
            int max = 0;
            if (level != null) {
                IWorldInfo worldInfo = level.getLevelData();
                float distance = worldPosition.distManhattan(new Vector3i(worldInfo.getXSpawn(), worldInfo.getYSpawn(), worldInfo.getZSpawn()));
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

    public Fluid fluidNeeded(){
        return fluid;
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
    public CompoundNBT save(CompoundNBT p_189515_1_) {
        CompoundNBT tag = super.save(p_189515_1_);
        tag.putInt("amount", amount);
        tag.putInt("max_amount", maxAmount);
        return tag;
    }

    @Override
    public void load(BlockState p_230337_1_, CompoundNBT p_230337_2_) {
        super.load(p_230337_1_, p_230337_2_);

        amount = p_230337_2_.getInt("amount");
        maxAmount = p_230337_2_.getInt("max_amount");
    }

    public int getAmount() {
        return amount;
    }

    public int getMaxAmount() {
        return maxAmount;
    }
}
