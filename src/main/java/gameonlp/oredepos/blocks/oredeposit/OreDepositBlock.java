package gameonlp.oredepos.blocks.oredeposit;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.fluid.Fluid;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockReader;

import javax.annotation.Nullable;
import java.util.function.Supplier;

public class OreDepositBlock extends Block {
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
    public boolean hasTileEntity(BlockState state) {
        return true;
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return new OreDepositTile(fluid, factor);
    }
}
