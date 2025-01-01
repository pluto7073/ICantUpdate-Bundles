package ml.pluto7073.icu.bundles.mixin.client;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.sugar.Local;
import ml.pluto7073.icu.bundles.BundleUtil;
import ml.pluto7073.icu.bundles.BundlesOfBravery;
import ml.pluto7073.icu.bundles.tooltip.BundleSelection;
import ml.pluto7073.icu.bundles.tooltip.SelectionBundleTooltip;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientBundleTooltip;
import net.minecraft.core.NonNullList;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.inventory.tooltip.BundleTooltip;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;
import java.util.Objects;

@Mixin(ClientBundleTooltip.class)
@Debug(export = true)
public abstract class ClientBundleTooltipMixin {

    @Shadow protected abstract int gridSizeY();

    @Shadow @Final private NonNullList<ItemStack> items;

    @Shadow protected abstract void renderSlot(int x, int y, int index, boolean shouldBlock, GuiGraphics graphics, Font textRenderer);

    @Unique private static final ResourceLocation icu_bundles$SLOT_HIGHLIGHT_FRONT =
            BundlesOfBravery.asId("textures/container/bundle/slot_highlight_front.png");

    @Unique private int icu_bundles$selected = 0;

    @Unique private int icu_bundles$ItemGridHeight() {
        return gridSizeY() * 24;
    }

    @Unique private int icu_bundles$BackgroundHeight() {
        return icu_bundles$ItemGridHeight() + 13 + 8;
    }

    @Inject(method = "<init>", at = @At("TAIL"))
    private void icu_bundles$ReadSelected(BundleTooltip data, CallbackInfo ci) {
        icu_bundles$selected = data instanceof SelectionBundleTooltip sel ? sel.selected() : 0;
    }

    @ModifyVariable(
            method = "renderSlot",
            at = @At(shift = At.Shift.AFTER, value = "INVOKE", target = "Lnet/minecraft/client/gui/GuiGraphics;renderItemDecorations(Lnet/minecraft/client/gui/Font;Lnet/minecraft/world/item/ItemStack;II)V"),
            ordinal = 2,
            argsOnly = true
    )
    private int icu_bundles$SetSelected(int value, @Local ItemStack stack) {
        if (value == icu_bundles$selected && BundleSelection.hasSelected(stack)) return 0;
        return -1;
    }

    @ModifyReturnValue(
            method = "gridSizeY",
            at = @At("RETURN")
    )
    private int icu_bundles$SetGridSizeY(int original) {
        return Mth.positiveCeilDiv(Math.min(12, this.items.size()), 4);
    }

    @ModifyReturnValue(
            method = "getWidth",
            at = @At("RETURN")
    )
    private int icu_bundles$SetWidth(int original) {
        return 96;
    }

    @ModifyReturnValue(
            method = "getHeight",
            at = @At("RETURN")
    )
    private int icu_bundles$SetHeight(int original) {
        return icu_bundles$BackgroundHeight();
    }

    @Redirect(
            method = "renderSlot",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screens/inventory/AbstractContainerScreen;renderSlotHighlight(Lnet/minecraft/client/gui/GuiGraphics;III)V")
    )
    private void icu_bundles$RenderSelected(GuiGraphics graphics, int x, int y, int z) {
        graphics.blit(icu_bundles$SLOT_HIGHLIGHT_FRONT, x - 1, y - 1, 0, 3, 3, 18, 18, 24, 24);
    }

    @Unique private int icu_bundles$GetAmountOfHiddenItems(List<ItemStack> list) {
        return this.items.stream().map(ItemStack::copy).skip(list.size()).mapToInt(ItemStack::getCount).sum();
    }

    @Unique private int icu_bundles$GetItemsToShow() {
        return BundleUtil.getItemsToShow(items);
    }

    @Inject(method = "renderImage", at = @At("HEAD"), cancellable = true)
    private void icu_bundles$RenderNewBundleUI(Font textRenderer, int x, int y, GuiGraphics graphics, CallbackInfo ci) {
        if (items.isEmpty()) {
            ci.cancel();
            return;
        }
        List<ItemStack> toShow = items.stream().map(ItemStack::copy).toList().subList(0, icu_bundles$GetItemsToShow());
        int i = x + 96;
        int j = y + gridSizeY() * 24;
        int k = 1;

        for (int l = 1; l <= gridSizeY(); ++l) {
            for (int m = 1; m <= 4; ++m) {
                int n = i - m * 24;
                int o = j - l * 24;
                if (items.size() > 12 && m * l == 1) {
                    graphics.drawCenteredString(textRenderer, "+" + icu_bundles$GetAmountOfHiddenItems(toShow),
                            n + 10, o + 8,16777215);
                } else if (toShow.size() >= k) {
                    renderSlot(n, o, toShow.size() - k, false, graphics, textRenderer);
                    ++k;
                }
            }
        }
        ci.cancel();
    }

}
