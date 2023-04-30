package gameonlp.oredepos.blocks.oredeposit;

import gameonlp.oredepos.RegistryManager;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.entity.BlockEntity;

import javax.annotation.Nullable;
import java.util.function.Supplier;

public class OreDepositBlock extends BaseEntityBlock {
    private final String fluid;
    private final Supplier<Double> factor;

    public OreDepositBlock(Properties properties, Supplier<Double> factor) {
        this(properties, "", factor);
    }

    public OreDepositBlock(Properties properties, String fluid, Supplier<Double> factor) {
        super(properties);
        this.fluid = fluid;
        this.factor = factor;
    }

    @Override
    public RenderShape getRenderShape(BlockState p_49232_) {
        return RenderShape.MODEL;
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new OreDepositTile(pos, state, fluid, factor);
    }

    @Override
    public void onRemove(BlockState state, Level level, BlockPos blockPos, BlockState newState, boolean moved) {
        if (!level.isClientSide) {
            if (level.getBlockEntity(blockPos) instanceof OreDepositTile oreDepositTile) {
                if (oreDepositTile.getAmount() > 0) {
                    oreDepositTile.decrement();
                }
                if (!oreDepositTile.isRemovable() && oreDepositTile.getAmount() > 0) {
                    oreDepositTile.regenerate(blockPos, state);
                }
                return;
            }
        }
        super.onRemove(state,level, blockPos,newState, moved);
        level.blockEvent(blockPos, state.getBlock(), 0, 0);
    }

    @Override
    public void onBlockExploded(BlockState state, Level level, BlockPos pos, Explosion explosion) {
        if (level != null && level.getBlockEntity(pos) instanceof OreDepositTile tile){
            tile.setRemovable();
        }
        super.onBlockExploded(state, level, pos, explosion);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
        return level.isClientSide() ? null : createTickerHelper(type, RegistryManager.ORE_DEPOSIT_TILE.get(), OreDepositTile::serverTick);
    }
}
