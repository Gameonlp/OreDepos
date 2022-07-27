package gameonlp.oredepos.blocks.oredeposit;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.BlockGetter;

import javax.annotation.Nullable;
import java.util.Random;
import java.util.function.Supplier;

import net.minecraft.world.level.block.state.BlockBehaviour.Properties;

public class OreDepositBlock extends BaseEntityBlock {
    private String fluid;
    private Double factor;

    public OreDepositBlock(Properties properties, double factor) {
        this(properties, "", factor);
    }

    public OreDepositBlock(Properties properties, String fluid, double factor) {
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
                if (!oreDepositTile.isRemovable() || oreDepositTile.getAmount() > 0) {
                    level.setBlock(blockPos, state, 0);
                    level.blockEvent(blockPos, state.getBlock(), 0, 0);
                }
                return;
            }
        }
        super.onRemove(state,level, blockPos,newState, moved);
    }
}
