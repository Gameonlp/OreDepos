package gameonlp.oredepos.blocks.oredeposit;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;

public class RedstoneOreDepositBlock extends OreDepositBlock{
    public RedstoneOreDepositBlock(Properties properties, String needed, double factor) {
        super(properties, needed, factor);
    }

    @Override
    protected void createBlockStateDefinition(StateContainer.Builder<Block, BlockState> p_206840_1_) {
        super.createBlockStateDefinition(p_206840_1_);
        p_206840_1_.add(BlockStateProperties.LIT);
    }
}
