package gameonlp.oredepos.util;

import gameonlp.oredepos.net.PacketEnergySync;
import gameonlp.oredepos.net.PacketManager;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.fml.network.PacketDistributor;

public class EnergyCell implements IEnergyStorage {
    private int energy;
    private int maxEnergy;
    private TileEntity tile;
    private final boolean extractable;
    private final boolean canReceive;

    public EnergyCell(TileEntity tile, boolean extractable, boolean canReceive, int maxEnergy) {
        this.tile = tile;
        this.extractable = extractable;
        this.canReceive = canReceive;
        this.maxEnergy = maxEnergy;
    }

    @Override
    public int receiveEnergy(int maxReceive, boolean simulate) {
        int accept = Math.min(maxEnergy - energy, maxReceive);
        if (!simulate) {
            energy += accept;
            if (!tile.getLevel().isClientSide()) {
                tile.setChanged();
                PacketManager.INSTANCE.send(PacketDistributor.ALL.noArg(), new PacketEnergySync(tile.getBlockPos(), energy));
            }
        }
        return accept;
    }

    @Override
    public int extractEnergy(int maxExtract, boolean simulate) {
        int accept = Math.min(maxEnergy - energy, maxExtract);
        if (!simulate) {
            energy -= accept;
            if (!tile.getLevel().isClientSide()) {
                tile.setChanged();
                PacketManager.INSTANCE.send(PacketDistributor.ALL.noArg(), new PacketEnergySync(tile.getBlockPos(), energy));
            }
        }
        return accept;
    }

    @Override
    public int getEnergyStored() {
        return energy;
    }

    @Override
    public int getMaxEnergyStored() {
        return maxEnergy;
    }

    @Override
    public boolean canExtract() {
        return extractable;
    }

    @Override
    public boolean canReceive() {
        return canReceive;
    }

    public void setEnergy(int energy){
        this.energy = energy;
        if (tile.getLevel() != null && !tile.getLevel().isClientSide()) {
            PacketManager.INSTANCE.send(PacketDistributor.ALL.noArg(), new PacketEnergySync(tile.getBlockPos(), energy));
        }
    }
}