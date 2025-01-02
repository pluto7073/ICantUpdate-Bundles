package ml.pluto7073.icu.bundles.tooltip;

import ml.pluto7073.icu.bundles.BundleUtil;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;

public class BundleSelection {

    public static final String SELECTION_KEY = "Selected";

    public static int get(ItemStack stack) {
        CompoundTag tag = stack.getTag();
        if (tag == null || !tag.contains(SELECTION_KEY)) return 0;
        return tag.getInt(SELECTION_KEY);
    }

    public static void set(ItemStack stack, int selected) {
        stack.getOrCreateTag().putInt(SELECTION_KEY, selected);
    }

    public static void clear(ItemStack stack) {
        stack.removeTagKey(SELECTION_KEY);
    }

    public static void add(ItemStack stack, int amount) {
        CompoundTag tag = stack.getTag();
        int count = tag != null && tag.contains("Items") ?
                Math.max(BundleUtil.getItemsToShow(tag.getList("Items", 10).stream().toList()), 1) : 1;
        int start = (tag == null || !tag.contains(SELECTION_KEY)) && amount > 0 ? -1 : get(stack);
        set(stack, Math.floorMod(start + amount, count));
    }

    public static boolean hasSelected(ItemStack stack) {
        return stack.getOrCreateTag().contains(SELECTION_KEY);
    }

}
