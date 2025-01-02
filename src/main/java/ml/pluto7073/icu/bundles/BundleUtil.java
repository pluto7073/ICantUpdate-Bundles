package ml.pluto7073.icu.bundles;

import net.minecraft.world.item.BundleItem;
import net.minecraft.world.item.ItemStack;

import java.util.List;
import java.util.stream.Stream;

public class BundleUtil {

    public static int getItemsToShow(List<?> items) {
        int i = items.size();
        int j = i > 12 ? 11 : 12;
        int k = i % 4;
        int l = k == 0 ? 0 : 4 - k;
        return Math.min(i, j - l);
    }

    public static int getContentWeight(Stream<ItemStack> stacks) {
        return stacks.mapToInt(stack -> BundleItem.getWeight(stack) * stack.getCount()).sum();
    }

}
