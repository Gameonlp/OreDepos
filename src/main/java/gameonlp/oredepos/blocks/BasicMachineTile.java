package gameonlp.oredepos.blocks;

import gameonlp.oredepos.blocks.beacon.BeaconTile;
import gameonlp.oredepos.items.ModuleItem;
import gameonlp.oredepos.net.PacketManager;
import gameonlp.oredepos.net.PacketProductivitySync;
import gameonlp.oredepos.net.PacketProgressSync;
import gameonlp.oredepos.tile.EnergyHandlerTile;
import gameonlp.oredepos.tile.ModuleAcceptorTile;
import gameonlp.oredepos.util.EnergyCell;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.network.PacketDistributor;
import org.jetbrains.annotations.NotNull;

import java.util.LinkedList;
import java.util.List;

public abstract class BasicMachineTile extends BlockEntity implements ModuleAcceptorTile, EnergyHandlerTile {

    protected EnergyCell energyCell;
    protected ItemStackHandler slots;
    protected float progress;
    protected List<BeaconTile> beacons;
    private List<BeaconTile> toAdd;
    private List<BeaconTile> toRemove;

    public float getProductivity() {
        return productivity;
    }

    protected float productivity;

    public EnergyCell getEnergyCell() {
        return energyCell;
    }

    public float getProgress() {
        return progress;
    }

    public float getMaxProgress() {
        return maxProgress;
    }

    protected float maxProgress = 30;

    protected BasicMachineTile(BlockEntityType<?> p_i48289_1_, BlockPos pos, BlockState state) {
        super(p_i48289_1_, pos, state);
        beacons = new LinkedList<>();
        toAdd = new LinkedList<>();
        toRemove = new LinkedList<>();
    }

    public ItemStackHandler getSlots() {
        return slots;
    }

    @NotNull
    public List<ModuleItem> getModuleItems(int moduleOffset) {
        List<ModuleItem> modules = new LinkedList<>();
        for (int i = moduleOffset; i < slots.getSlots(); i++) {
            ItemStack moduleStack = slots.getStackInSlot(i);
            if (!moduleStack.isEmpty() && moduleStack.getItem() instanceof ModuleItem){
                modules.add((ModuleItem) moduleStack.getItem());
            }
        }
        return modules;
    }

    protected float getDrain(List<ModuleItem> modules, float baseDrain) {
        List<ModuleItem> beaconModules = new LinkedList<>();
        for (BeaconTile beacon : beacons) {
            beaconModules.addAll(beacon.getModuleItems(0));
        }
        float beaconBoost = 0f;
        for (ModuleItem beaconModule : beaconModules) {
            beaconBoost = beaconModule.getEnergyConsumption(beaconBoost);
        }
        beaconBoost /= 2;
        float drain = 1f;
        for (ModuleItem module : modules){
            drain = module.getEnergyConsumption(drain);
        }
        drain += beaconBoost;
        return drain * baseDrain;
    }

    protected void increaseProgress(List<ModuleItem> modules, float drain, float time) {
        List<ModuleItem> beaconModules = new LinkedList<>();
        for (BeaconTile beacon : beacons) {
            beaconModules.addAll(beacon.getModuleItems(0));
        }
        float beaconBoost = 0f;
        for (ModuleItem beaconModule : beaconModules) {
            beaconBoost = beaconModule.getProgress(beaconBoost);
        }
        beaconBoost /= 2;
        float progressRatio = time / maxProgress;
        if (energyCell.getEnergyStored() >= drain) {
            energyCell.setEnergy((int) (energyCell.getEnergyStored() - drain));
            float progressIncrease = 1.0f;
            for (ModuleItem module : modules){
                progressIncrease = module.getProgress(progressIncrease);
            }
            progressIncrease += beaconBoost;
            progress += (progressIncrease / progressRatio);
            PacketManager.INSTANCE.send(PacketDistributor.ALL.noArg(), new PacketProgressSync(worldPosition, progress));
            this.setChanged();
        }
    }

    protected void increaseProductivity(List<ModuleItem> modules) {
        List<ModuleItem> beaconModules = new LinkedList<>();
        for (BeaconTile beacon : beacons) {
            beaconModules.addAll(beacon.getModuleItems(0));
        }
        float beaconBoost = 0f;
        for (ModuleItem beaconModule : beaconModules) {
            beaconBoost = beaconModule.getProductivity(beaconBoost);
        }
        beaconBoost /= 2;
        float productivityIncrease = 0.0f;
        for (ModuleItem module : modules) {
            productivityIncrease = module.getProductivity(productivityIncrease);
        }
        productivityIncrease += beaconBoost;
        productivity += productivityIncrease;
        PacketManager.INSTANCE.send(PacketDistributor.ALL.noArg(), new PacketProductivitySync(worldPosition, productivity));
        this.setChanged();
    }

    public void setEnergy(int energy) {
        this.energyCell.setEnergy(energy);
    }

    public void setProgress(float progress) {
        this.progress = progress;
    }

    public void setProductivity(float productivity) {
        this.productivity = productivity;
    }

    public void update() {
        beacons.addAll(toAdd);
        beacons.removeAll(toRemove);
        toAdd.clear();
        toRemove.clear();
    }

    @Override
    public void addBeacon(BeaconTile beacon) {
        if (!beacons.contains(beacon) && !toAdd.contains(beacon)) {
            toAdd.add(beacon);
        }
    }

    @Override
    public void removeBeacon(BeaconTile beacon) {
        toRemove.add(beacon);
    }
}
