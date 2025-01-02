package ml.pluto7073.icu.bundles.item;

import ml.pluto7073.icu.bundles.BundlesOfBravery;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

public class ICUItems {

    public static final Map<ResourceLocation, Item> BUNDLES = new HashMap<>();

    private static <T extends Item> T register(String id, T item) {
        ResourceLocation itemId = new ResourceLocation(id);
        BUNDLES.put(itemId, item);
        return Registry.register(BuiltInRegistries.ITEM, itemId, item);
    }

    private static BundleItem register(String id) {
        return register(id + "_bundle",
                new BundleItem(new Item.Properties().stacksTo(1)));
    }

    public static final Item RED_BUNDLE = register("red");
    public static final Item ORANGE_BUNDLE = register("orange");
    public static final Item YELLOW_BUNDLE = register("yellow");
    public static final Item LIME_BUNDLE = register("lime");
    public static final Item GREEN_BUNDLE = register("green");
    public static final Item CYAN_BUNDLE = register("cyan");
    public static final Item LIGHT_BLUE_BUNDLE = register("light_blue");
    public static final Item BLUE_BUNDLE = register("blue");
    public static final Item PURPLE_BUNDLE = register("purple");
    public static final Item MAGENTA_BUNDLE = register("magenta");
    public static final Item PINK_BUNDLE = register("pink");
    public static final Item BROWN_BUNDLE = register("brown");
    public static final Item BLACK_BUNDLE = register("black");
    public static final Item GRAY_BUNDLE = register("gray");
    public static final Item LIGHT_GRAY_BUNDLE = register("light_gray");
    public static final Item WHITE_BUNDLE = register("white");

    public static void init() {
        ItemGroupEvents.modifyEntriesEvent(CreativeModeTabs.TOOLS_AND_UTILITIES)
                .register(entries -> entries.addAfter(Items.BUNDLE, Stream.of(
                        WHITE_BUNDLE,
                        LIGHT_GRAY_BUNDLE,
                        GRAY_BUNDLE,
                        BLACK_BUNDLE,
                        BROWN_BUNDLE,
                        RED_BUNDLE,
                        ORANGE_BUNDLE,
                        YELLOW_BUNDLE,
                        LIME_BUNDLE,
                        GREEN_BUNDLE,
                        CYAN_BUNDLE,
                        LIGHT_BLUE_BUNDLE,
                        BLUE_BUNDLE,
                        PURPLE_BUNDLE,
                        MAGENTA_BUNDLE,
                        PINK_BUNDLE
                ).map(ItemStack::new).toList(), CreativeModeTab.TabVisibility.SEARCH_TAB_ONLY));
    }

}
