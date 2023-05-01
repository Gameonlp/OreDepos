package gameonlp.oredepos.tile;


import gameonlp.oredepos.blocks.beacon.BeaconTile;

public interface ModuleAcceptorTile {
    void setProgress(float progress);
    void setProductivity(float productivity);

    void addBeacon(BeaconTile beacon);

    void removeBeacon(BeaconTile beacon);
}
