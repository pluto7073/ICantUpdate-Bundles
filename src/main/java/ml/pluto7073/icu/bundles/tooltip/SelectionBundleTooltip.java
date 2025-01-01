package ml.pluto7073.icu.bundles.tooltip;

import net.minecraft.core.NonNullList;
import net.minecraft.world.inventory.tooltip.BundleTooltip;
import net.minecraft.world.item.ItemStack;

public class SelectionBundleTooltip extends BundleTooltip {

    private final int selected;
    private final boolean hasSelection;

    public SelectionBundleTooltip(NonNullList<ItemStack> inventory, int bundleOccupancy, int selected, boolean hasSelection) {
        super(inventory, bundleOccupancy);
        this.selected = selected;
        this.hasSelection = hasSelection;
    }

    public int selected() {
        return selected;
    }

    public boolean hasSelection() {
        return hasSelection;
    }
}
