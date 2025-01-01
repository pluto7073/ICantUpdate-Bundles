package ml.pluto7073.icu.bundles;

import net.minecraft.world.item.ItemStack;

import java.util.List;

public class BundleUtil {

    public static int getItemsToShow(List<?> items) {
        int i = items.size();
        int j = i > 12 ? 11 : 12;
        int k = i % 4;
        int l = k == 0 ? 0 : 4 - k;
        return Math.min(i, j - l);
    }

}
