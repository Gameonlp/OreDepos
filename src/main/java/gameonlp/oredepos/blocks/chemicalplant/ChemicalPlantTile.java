package gameonlp.oredepos.blocks.chemicalplant;

import gameonlp.oredepos.RegistryManager;
import gameonlp.oredepos.blocks.oredeposit.OreDepositTile;
import gameonlp.oredepos.crafting.ChemicalPlantRecipe;
import gameonlp.oredepos.crafting.FluidIngredient;
import gameonlp.oredepos.crafting.FluidInventory;
import gameonlp.oredepos.items.DrillHeadItem;
import gameonlp.oredepos.items.ModuleItem;
import gameonlp.oredepos.net.PacketManager;
import gameonlp.oredepos.net.PacketProductivitySync;
import gameonlp.oredepos.net.PacketProgressSync;
import gameonlp.oredepos.net.PacketTooltipSync;
import gameonlp.oredepos.tile.EnergyHandlerTile;
import gameonlp.oredepos.tile.FluidHandlerTile;
import gameonlp.oredepos.tile.ModuleAcceptorTile;
import gameonlp.oredepos.util.CustomFluidTank;
import gameonlp.oredepos.util.EnergyCell;
import gameonlp.oredepos.util.PlayerInOutStackHandler;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.fluid.Fluid;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tags.FluidTags;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraft.util.NonNullList;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.templates.FluidTank;
import net.minecraftforge.fml.network.PacketDistributor;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.*;

public class ChemicalPlantTile extends TileEntity implements ITickableTileEntity, EnergyHandlerTile, FluidHandlerTile, ModuleAcceptorTile {

    final EnergyCell energyCell = new EnergyCell(this, false, true, 16000);
    ItemStackHandler slots = createItemHandler();

    PlayerInOutStackHandler handler = new PlayerInOutStackHandler(this, slots, 1);

    int fluidCapacity = 4000;
    FluidTank fluidTank = new CustomFluidTank(this, fluidCapacity, 0);
    FluidTank primaryInputTank = new CustomFluidTank(this, fluidCapacity, 1);
    FluidTank secondaryInputTank = new CustomFluidTank(this, fluidCapacity, 2);

    LazyOptional<ItemStackHandler> machineItemHandler = LazyOptional.of(() -> handler.getMachineAccessible());
    LazyOptional<ItemStackHandler> itemHandler = LazyOptional.of(() -> handler.getPlayerAccessible());
    LazyOptional<IFluidHandler> outputFluidHandler = LazyOptional.of(() -> fluidTank);
    LazyOptional<IFluidHandler> primaryFluidHandler = LazyOptional.of(() -> primaryInputTank);
    LazyOptional<IFluidHandler> secondaryFluidHandler = LazyOptional.of(() -> secondaryInputTank);
    LazyOptional<IEnergyStorage> energyHandler = LazyOptional.of(() -> energyCell);

    float progress;
    float maxProgress = 30;
    float productivity;

    ChemicalPlantRecipe currentRecipe = null;

    protected ChemicalPlantTile(TileEntityType<?> p_i48289_1_) {
        super(p_i48289_1_);
    }

    public ChemicalPlantTile() {
        this(RegistryManager.CHEMICAL_PLANT_TILE.get());
    }

    private ItemStackHandler createItemHandler() {
        return new ItemStackHandler(6){
            @Override
            protected void onContentsChanged(int slot) {
                super.onContentsChanged(slot);
                setChanged();
            }

            @Override
            public boolean isItemValid(int slot, @Nonnull ItemStack stack) {
                if (slot > 2) {
                    return stack.getItem() instanceof ModuleItem;
                }
                return super.isItemValid(slot, stack);
            }
        };
    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        if (side == null && CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.equals(cap)){
            return itemHandler.cast();
        }
        if (side == this.getBlockState().getValue(BlockStateProperties.FACING) && CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY.equals(cap)){
            return outputFluidHandler.cast();
        }
        if (side == this.getBlockState().getValue(BlockStateProperties.FACING).getClockWise() && CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY.equals(cap)){
            return secondaryFluidHandler.cast();
        }
        if (side == this.getBlockState().getValue(BlockStateProperties.FACING).getCounterClockWise() && CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY.equals(cap)){
            return primaryFluidHandler.cast();
        }
        if (CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.equals(cap)) {
            return machineItemHandler.cast();
        }
        if (CapabilityEnergy.ENERGY.equals(cap)){
            return energyHandler.cast();
        }
        return super.getCapability(cap, side);
    }

    @Override
    protected void invalidateCaps() {
        super.invalidateCaps();
        machineItemHandler.invalidate();
        itemHandler.invalidate();
        outputFluidHandler.invalidate();
        primaryFluidHandler.invalidate();
        secondaryFluidHandler.invalidate();
        energyHandler.invalidate();
    }

    @Override
    public CompoundNBT save(CompoundNBT p_189515_1_) {
        CompoundNBT tag = super.save(p_189515_1_);
        tag.putInt("energy", energyCell.getEnergyStored());
        tag.putFloat("progress", progress);
        tag.putFloat("productivity", productivity);
        CompoundNBT outputFluid = new CompoundNBT();
        tag.put("output_fluid", fluidTank.writeToNBT(outputFluid));
        CompoundNBT primaryFluid = new CompoundNBT();
        tag.put("primary_fluid", primaryInputTank.writeToNBT(primaryFluid));
        CompoundNBT secondaryFluid = new CompoundNBT();
        tag.put("secondary_fluid", secondaryInputTank.writeToNBT(secondaryFluid));
        tag.put("slots", slots.serializeNBT());
        return tag;
    }

    @Override
    public void load(BlockState p_230337_1_, CompoundNBT p_230337_2_) {
        super.load(p_230337_1_, p_230337_2_);
        energyCell.setEnergy(p_230337_2_.getInt("energy"));
        progress = p_230337_2_.getFloat("progress");
        productivity = p_230337_2_.getFloat("productivity");
        CompoundNBT outputFluid = p_230337_2_.getCompound("output_fluid");
        FluidStack fluid = FluidStack.loadFluidStackFromNBT(outputFluid);
        if (fluid == null){
            fluid = FluidStack.EMPTY;
        }
        fluidTank.setFluid(fluid);
        CompoundNBT primaryFluid = p_230337_2_.getCompound("primary_fluid");
        FluidStack primfluid = FluidStack.loadFluidStackFromNBT(primaryFluid);
        if (primfluid == null){
            primfluid = FluidStack.EMPTY;
        }
        primaryInputTank.setFluid(primfluid);
        CompoundNBT secondaryFluid = p_230337_2_.getCompound("secondary_fluid");
        FluidStack secofluid = FluidStack.loadFluidStackFromNBT(secondaryFluid);
        if (secofluid == null){
            secofluid = FluidStack.EMPTY;
        }
        secondaryInputTank.setFluid(secofluid);
        slots.deserializeNBT(p_230337_2_.getCompound("slots"));
    }

    @Override
    public void tick() {
        if (level.isClientSide()){
            return;
        }
        if (!fluidTank.isEmpty()){
            Direction facing = getBlockState().getValue(BlockStateProperties.FACING);
            LazyOptional<IFluidHandler> capability = level.getBlockEntity(worldPosition.offset(facing.getNormal())).getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, facing.getOpposite());
            capability.ifPresent(handler -> {
                if (handler.fill(fluidTank.drain(1000, IFluidHandler.FluidAction.SIMULATE), IFluidHandler.FluidAction.SIMULATE) != 0){
                    FluidStack toFill = fluidTank.drain(1000, IFluidHandler.FluidAction.EXECUTE);
                    int accepted = handler.fill(toFill, IFluidHandler.FluidAction.EXECUTE);
                    toFill.grow(-accepted);
                    fluidTank.fill(toFill, IFluidHandler.FluidAction.EXECUTE);
                }
            });
        }
        FluidInventory fluidInventory = new FluidInventory(2, 2);
        fluidInventory.setItem(0, slots.getStackInSlot(1));
        fluidInventory.setItem(1, slots.getStackInSlot(2));
        fluidInventory.setFluid(0, primaryInputTank.getFluid());
        fluidInventory.setFluid(1, secondaryInputTank.getFluid());
        if (level.getGameTime() % 20 == 0) {
            Optional<ChemicalPlantRecipe> recipe = level.getRecipeManager().getRecipeFor(RegistryManager.CHEMICAL_PLANT_RECIPE_TYPE, fluidInventory, level);
            recipe.ifPresent(chemicalPlantRecipe -> currentRecipe = chemicalPlantRecipe);
        }
        if (currentRecipe == null) {
        }
        if (currentRecipe != null){
            if (!currentRecipe.matches(fluidInventory, level)){
                progress = 0;
                currentRecipe = null;
                PacketManager.INSTANCE.send(PacketDistributor.ALL.noArg(), new PacketProgressSync(worldPosition, progress));
                return;
            }
            if (slots.insertItem(0, currentRecipe.getResultItem(), true) != ItemStack.EMPTY){
                return;
            }
            if (fluidTank.fill(currentRecipe.getResultFluid(), IFluidHandler.FluidAction.SIMULATE) != currentRecipe.getResultFluid().getAmount()) {
                return;
            }
            List<ModuleItem> modules = new LinkedList<>();
            ItemStack mod1 = slots.getStackInSlot(3);
            ItemStack mod2 = slots.getStackInSlot(4);
            ItemStack mod3 =  slots.getStackInSlot(5);
            if (!mod1.isEmpty()){
                modules.add((ModuleItem) mod1.getItem());
            }
            if (!mod2.isEmpty()){
                modules.add((ModuleItem) mod2.getItem());
            }
            if (!mod3.isEmpty()){
                modules.add((ModuleItem) mod3.getItem());
            }
            float drain = (float) currentRecipe.getEnergy() / currentRecipe.getTicks();
            for (ModuleItem module : modules){
                drain = module.getEnergyConsumption(drain);
            }
            if (energyCell.getEnergyStored() >= drain) {
                energyCell.setEnergy((int) (energyCell.getEnergyStored() - drain));
                float progressIncrease = 1.0f;
                for (ModuleItem module : modules){
                    progressIncrease = module.getProgress(progressIncrease);
                }
                progress += progressIncrease;
                PacketManager.INSTANCE.send(PacketDistributor.ALL.noArg(), new PacketProgressSync(worldPosition, progress));
                this.setChanged();
            }
            if (progress >= currentRecipe.getTicks()) {
                progress -= currentRecipe.getTicks();
                PacketManager.INSTANCE.send(PacketDistributor.ALL.noArg(), new PacketProgressSync(worldPosition, progress));
                NonNullList<Ingredient> ingredients = currentRecipe.getIngredients();
                for (Ingredient ingredient : ingredients) {
                    if (ingredient.test(slots.getStackInSlot(1))) {
                        slots.extractItem(1, 1, false);
                    } else if (ingredient.test(slots.getStackInSlot(2))) {
                        slots.extractItem(2, 1, false);
                    }
                }
                NonNullList<FluidIngredient> fluidIngredients = currentRecipe.getFluidIngredients();
                if (fluidIngredients.size() == 1){
                    if (fluidIngredients.get(0).test(primaryInputTank.drain(fluidIngredients.get(0).getAmount(), IFluidHandler.FluidAction.SIMULATE))){
                        primaryInputTank.drain(fluidIngredients.get(0).getAmount(), IFluidHandler.FluidAction.EXECUTE);
                    } else if (fluidIngredients.get(0).test(secondaryInputTank.drain(fluidIngredients.get(0).getAmount(), IFluidHandler.FluidAction.SIMULATE))){
                        secondaryInputTank.drain(fluidIngredients.get(0).getAmount(), IFluidHandler.FluidAction.EXECUTE);
                    }
                } else {
                    primaryInputTank.drain(fluidIngredients.get(0).getAmount(), IFluidHandler.FluidAction.EXECUTE);
                    secondaryInputTank.drain(fluidIngredients.get(1).getAmount(), IFluidHandler.FluidAction.EXECUTE);
                }
                ItemStack outStack = currentRecipe.getResultItem();
                FluidStack outFluid = currentRecipe.getResultFluid();
                if (productivity >= 1){
                    productivity -= 1;
                    PacketManager.INSTANCE.send(PacketDistributor.ALL.noArg(), new PacketProductivitySync(worldPosition, productivity));
                    outStack.setCount(outStack.getCount() * 2);
                    outFluid.setAmount(outFluid.getAmount() * 2);
                }
                fluidTank.fill(outFluid, IFluidHandler.FluidAction.EXECUTE);
                slots.insertItem(0, outStack, false);
                float productivityIncrease = 0.0f;
                for (ModuleItem module : modules) {
                    productivityIncrease = module.getProductivity(productivityIncrease);
                }
                productivity += productivityIncrease;
                PacketManager.INSTANCE.send(PacketDistributor.ALL.noArg(), new PacketProductivitySync(worldPosition, productivity));
                this.setChanged();
            }
        }
    }

    @Override
    public void setEnergy(int energy) {
        this.energyCell.setEnergy(energy);
    }

    @Override
    public void setFluid(FluidStack fluid, int tank) {
        switch (tank) {
            case 0:
                this.fluidTank.setFluid(fluid);
                break;
            case 1:
                this.primaryInputTank.setFluid(fluid);
                break;
            case 2:
                this.secondaryInputTank.setFluid(fluid);
        }
    }

    @Override
    public void setProgress(float progress) {
        this.progress = progress;
    }
    @Override
    public void setProductivity(float productivity) {
        this.productivity = productivity;
    }
}
