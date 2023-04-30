package gameonlp.oredepos.blocks.oredeposit;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;


import java.util.function.Supplier;

public class RedstoneOreDepositBlock extends OreDepositBlock{
    public RedstoneOreDepositBlock(Properties properties, String needed, Supplier<Double> factor) {
        super(properties, needed, factor);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> p_206840_1_) {
        super.createBlockStateDefinition(p_206840_1_);
        p_206840_1_.add(BlockStateProperties.LIT);
    }
}
