package ml.pluto7073.icu.bundles.tooltip;

import net.minecraft.core.NonNullList;
import net.minecraft.world.inventory.tooltip.BundleTooltip;
import net.minecraft.world.item.ItemStack;

public class SelectionBundleTooltip extends BundleTooltip {

    private final int selected;

    public SelectionBundleTooltip(NonNullList<ItemStack> inventory, int bundleOccupancy, int selected) {
        super(inventory, bundleOccupancy);
        this.selected = selected;
    }

    public int selected() {
        return selected;
    }

}
