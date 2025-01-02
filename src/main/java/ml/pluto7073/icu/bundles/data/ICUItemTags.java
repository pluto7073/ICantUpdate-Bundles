package ml.pluto7073.icu.bundles.data;

import ml.pluto7073.icu.bundles.BundlesOfBravery;
import ml.pluto7073.icu.bundles.item.ICUItems;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;

import java.util.concurrent.CompletableFuture;

public class ICUItemTags extends FabricTagProvider.ItemTagProvider {

    public static final TagKey<Item> BUNDLES = TagKey.create(Registries.ITEM, BundlesOfBravery.asId("bundles"));

    public ICUItemTags(FabricDataOutput output, CompletableFuture<HolderLookup.Provider> completableFuture) {
        super(output, completableFuture);
    }

    @Override
    protected void addTags(HolderLookup.Provider arg) {
        FabricTagBuilder builder = getOrCreateTagBuilder(BUNDLES).add(Items.BUNDLE);
        ICUItems.BUNDLES.values().forEach(builder::add);
    }

}
