package gameonlp.oredepos.data;

import gameonlp.oredepos.RegistryManager;
import net.minecraft.data.DataGenerator;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.common.data.ExistingFileHelper;

public class ModelProviders {
    public static class OreDepositsBlockStateProvider extends BlockStateProvider {
        private final ExistingFileHelper exFileHelper;

        public OreDepositsBlockStateProvider(DataGenerator gen, String modid, ExistingFileHelper exFileHelper) {
            super(gen, modid, exFileHelper);
            this.exFileHelper = exFileHelper;
        }

        private void simpleBlockAndItem(Block block) {
            simpleBlock(block);
            simpleBlockItem(block, new ModelFile.ExistingModelFile(new ResourceLocation(block.getRegistryName().toString().replace(":", ":block/")), exFileHelper));
        }

        @Override
        protected void registerStatesAndModels() {
            simpleBlockAndItem(RegistryManager.OSMIUM_ORE_DEPOSIT);
            simpleBlockAndItem(RegistryManager.OSMIUM_ORE);
            simpleBlockAndItem(RegistryManager.OSMIUM_BLOCK);
            simpleBlockAndItem(RegistryManager.RAW_OSMIUM_BLOCK);
            simpleBlockAndItem(RegistryManager.DEEPSLATE_OSMIUM_ORE);
            simpleBlockAndItem(RegistryManager.DEEPSLATE_OSMIUM_ORE_DEPOSIT);

            simpleBlockAndItem(RegistryManager.ARDITE_ORE_DEPOSIT);
            simpleBlockAndItem(RegistryManager.ARDITE_ORE);
            simpleBlockAndItem(RegistryManager.ARDITE_BLOCK);
            simpleBlockAndItem(RegistryManager.RAW_ARDITE_BLOCK);

            simpleBlockAndItem(RegistryManager.COBALT_ORE_DEPOSIT);
            simpleBlockAndItem(RegistryManager.COBALT_ORE);
            simpleBlockAndItem(RegistryManager.COBALT_BLOCK);
            simpleBlockAndItem(RegistryManager.RAW_COBALT_BLOCK);

            simpleBlockAndItem(RegistryManager.PLATINUM_ORE_DEPOSIT);
            simpleBlockAndItem(RegistryManager.PLATINUM_ORE);
            simpleBlockAndItem(RegistryManager.PLATINUM_BLOCK);
            simpleBlockAndItem(RegistryManager.RAW_PLATINUM_BLOCK);
            simpleBlockAndItem(RegistryManager.DEEPSLATE_PLATINUM_ORE);
            simpleBlockAndItem(RegistryManager.DEEPSLATE_PLATINUM_ORE_DEPOSIT);
        }
    }

    public static class OreDepositsItemModelProvider extends ItemModelProvider{

        public OreDepositsItemModelProvider(DataGenerator generator, String modid, ExistingFileHelper existingFileHelper) {
            super(generator, modid, existingFileHelper);
        }

        private void simpleItem(Item item){
            this.singleTexture(item.toString(), // For 'assets/<modid>/models/item/example_item.json'
                    new ResourceLocation("item/generated"), // Set parent to 'minecraft:item/generated'
                    "layer0", // For the texture key 'layer0'
                    modLoc("item/" + item) // Set the reference to 'assets/<modid>/textures/item/example_texture.png'
            );
        }
        @Override
        protected void registerModels() {
            simpleItem(RegistryManager.OSMIUM_INGOT);
            simpleItem(RegistryManager.RAW_OSMIUM);

            simpleItem(RegistryManager.ARDITE_INGOT);
            simpleItem(RegistryManager.RAW_ARDITE);

            simpleItem(RegistryManager.COBALT_INGOT);
            simpleItem(RegistryManager.RAW_COBALT);

            simpleItem(RegistryManager.PLATINUM_INGOT);
            simpleItem(RegistryManager.RAW_PLATINUM);
        }
    }
}