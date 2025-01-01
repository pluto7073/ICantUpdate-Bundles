package ml.pluto7073.icu.bundles.client;

import ml.pluto7073.icu.bundles.client.events.ICUClientEvents;
import net.fabricmc.api.ClientModInitializer;

public class BundlesOfClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        ICUClientEvents.register();
    }
}
