package ml.pluto7073.icu.bundles;

import ml.pluto7073.icu.bundles.item.ICUItems;
import ml.pluto7073.icu.bundles.multiplayer.ICUServerboundPackets;
import net.fabricmc.api.ModInitializer;
import net.minecraft.resources.ResourceLocation;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class BundlesOfBravery implements ModInitializer {

    public static final String MODID = "icu_bundles";
    public static final Logger LOGGER = LogManager.getLogger("I Can't Update");

    @Override
    public void onInitialize() {
        ICUServerboundPackets.registerReceivers();
        ICUItems.init();
    }

    public static ResourceLocation asId(String id) {
        return new ResourceLocation(MODID, id);
    }

}
