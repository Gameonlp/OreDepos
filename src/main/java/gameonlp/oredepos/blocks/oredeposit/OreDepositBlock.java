package gameonlp.oredepos.blocks.oredeposit;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.fluid.Fluid;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.Random;

public class OreDepositBlock extends Block {
    private Fluid fluid;
    private Double factor;

    public OreDepositBlock(Properties properties, double factor) {
        this(properties, null, factor);
    }

    public OreDepositBlock(Properties properties, Fluid fluid, double factor) {
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
