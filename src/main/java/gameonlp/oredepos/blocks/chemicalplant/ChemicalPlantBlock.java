package gameonlp.oredepos.blocks.chemicalplant;

import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Containers;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.InteractionResult;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.core.NonNullList;
import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;

import javax.annotation.Nullable;

import net.minecraft.world.level.block.state.BlockBehaviour.Properties;
import net.minecraftforge.network.NetworkHooks;

public class ChemicalPlantBlock extends BaseEntityBlock {

    public ChemicalPlantBlock(Properties properties) {
        super(properties);
    }

    @SuppressWarnings("deprecation")
    @Override
    public InteractionResult use(BlockState state, Level worldIn, BlockPos pos,
                                Player player, InteractionHand handIn, BlockHitResult hit) {
        if(!worldIn.isClientSide()) {
            BlockEntity tileEntity = worldIn.getBlockEntity(pos);
            if(tileEntity instanceof ChemicalPlantTile) {
                MenuProvider containerProvider = createContainerProvider(worldIn, pos);

                NetworkHooks.openGui(((ServerPlayer)player), containerProvider, tileEntity.getBlockPos());
            } else {
                throw new IllegalStateException("Our Container provider is missing!");
            }
        }
        return InteractionResult.SUCCESS;
    }

    private MenuProvider createContainerProvider(Level worldIn, BlockPos pos) {
        return new MenuProvider() {
            @Override
            public Component getDisplayName() {
                return new TranslatableComponent("screen.oredepos.chemical_plant");
            }

            @Nullable
            @Override
            public AbstractContainerMenu createMenu(int i, Inventory playerInventory, Player playerEntity) {
                return new ChemicalPlantContainer(i, worldIn, pos, playerInventory, playerEntity);
            }
        };
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new ChemicalPlantTile(pos, state);
    }

    @Override
    public RenderShape getRenderShape(BlockState p_49232_) {
        return RenderShape.MODEL;
    }

    @SuppressWarnings("deprecation")
    @Override
    public void onRemove(BlockState p_196243_1_, Level world, BlockPos blockPos, BlockState p_196243_4_, boolean dropContents) {
        if (!dropContents){
            BlockEntity tile = world.getBlockEntity(blockPos);
            if (tile instanceof ChemicalPlantTile){
                ChemicalPlantTile chemicalPlantTile = (ChemicalPlantTile) tile;
                NonNullList<ItemStack> contents = NonNullList.create();
                for (int i = 0; i < chemicalPlantTile.slots.getSlots(); i++) {
                    contents.add(chemicalPlantTile.slots.extractItem(i, Integer.MAX_VALUE, false));
                }
                Containers.dropContents(world, blockPos, contents);
            }
        }
        super.onRemove(p_196243_1_, world, blockPos, p_196243_4_, dropContents);
    }
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext p_196258_1_) {
        Direction direction = p_196258_1_.getHorizontalDirection().getOpposite();
        return this.defaultBlockState().setValue(BlockStateProperties.FACING, direction);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> p_206840_1_) {
        super.createBlockStateDefinition(p_206840_1_);
        p_206840_1_.add(BlockStateProperties.FACING);
    }

    @org.jetbrains.annotations.Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level p_153212_, BlockState p_153213_, BlockEntityType<T> p_153214_) {
        return super.getTicker(p_153212_, p_153213_, p_153214_);
    }
}
