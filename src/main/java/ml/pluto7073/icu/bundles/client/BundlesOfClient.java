package ml.pluto7073.icu.bundles.client;

import ml.pluto7073.icu.bundles.BundlesOfBravery;
import ml.pluto7073.icu.bundles.client.events.ICUClientEvents;
import net.fabricmc.api.ClientModInitializer;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BundleItem;

public class BundlesOfClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        ICUClientEvents.register();

        ItemProperties.registerGeneric(BundlesOfBravery.asId("filled"), (stack, level, entity, seed) -> {
            if (!(stack.getItem() instanceof BundleItem)) return 0;
            return BundleItem.getFullnessDisplay(stack);
        });
    }
}
