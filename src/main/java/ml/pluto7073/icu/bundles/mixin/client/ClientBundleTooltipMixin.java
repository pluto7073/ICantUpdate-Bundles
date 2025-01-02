package ml.pluto7073.icu.bundles.mixin.client;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.mojang.blaze3d.systems.RenderSystem;
import ml.pluto7073.icu.bundles.BundleUtil;
import ml.pluto7073.icu.bundles.BundlesOfBravery;
import ml.pluto7073.icu.bundles.tooltip.SelectionBundleTooltip;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientBundleTooltip;
import net.minecraft.core.NonNullList;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.inventory.tooltip.BundleTooltip;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(ClientBundleTooltip.class)
@Debug(export = true)
public abstract class ClientBundleTooltipMixin {

    @Shadow protected abstract int gridSizeY();

    @Shadow @Final private NonNullList<ItemStack> items;

    @Shadow protected abstract void renderSlot(int x, int y, int index, boolean shouldBlock, GuiGraphics graphics, Font textRenderer);

    @Unique private static final ResourceLocation SLOT_HIGHLIGHT_BACK =
            BundlesOfBravery.asId("textures/container/bundle/slot_highlight_back.png");
    @Unique private static final ResourceLocation FULLNESS_PROGRESS =
            BundlesOfBravery.asId("textures/container/bundle/bundle_progress.png");

    @Unique private int icu_bundles$selected = 0;
    @Unique private boolean icu_bundles$HasSelection = false;

    @Unique private int icu_bundles$ItemGridHeight() {
        return gridSizeY() * 24;
    }

    @Unique private int icu_bundles$BackgroundHeight() {
        return icu_bundles$ItemGridHeight() + 13 + 8;
    }

    @Inject(method = "<init>", at = @At("TAIL"))
    private void icu_bundles$ReadSelected(BundleTooltip data, CallbackInfo ci) {
        icu_bundles$selected = data instanceof SelectionBundleTooltip sel ? sel.selected() : 0;
        icu_bundles$HasSelection = data instanceof SelectionBundleTooltip sel && sel.hasSelection();
    }

    @ModifyVariable(
            method = "renderSlot",
            at = @At(shift = At.Shift.AFTER, value = "INVOKE", target = "Lnet/minecraft/client/gui/GuiGraphics;renderItemDecorations(Lnet/minecraft/client/gui/Font;Lnet/minecraft/world/item/ItemStack;II)V"),
            ordinal = 2,
            argsOnly = true
    )
    private int icu_bundles$SetSelected(int value) {
        if (value == icu_bundles$selected && icu_bundles$HasSelection) return 0;
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
        graphics.blit(SLOT_HIGHLIGHT_BACK, x - 1, y - 1, 0, 3, 3, 18, 18, 24, 24);
        AbstractContainerScreen.renderSlotHighlight(graphics, x, y, 0);
    }

    @Unique private int icu_bundles$GetAmountOfHiddenItems(List<ItemStack> list) {
        return this.items.stream().map(ItemStack::copy).skip(list.size()).mapToInt(ItemStack::getCount).sum();
    }

    @Unique private int icu_bundles$GetItemsToShow() {
        return BundleUtil.getItemsToShow(items);
    }

    @Unique private void icu_bundles$RenderSelectedTooltip(Font textRenderer, GuiGraphics graphics, int i, int j) {
        if (!icu_bundles$HasSelection) return;

        ItemStack stack = items.get(icu_bundles$selected);
        Component component = Component.empty().append(stack.getHoverName()).withStyle(stack.getRarity().color);
        int l = textRenderer.width(component.getVisualOrderText());
        int m = i + 96 / 2 - 12;
        graphics.renderTooltip(textRenderer, component, m - l / 2, j - 15);
    }

    @Unique private void icu_bundles$DrawProgressBar(int x, int y, Font textRenderer, GuiGraphics graphics) {
        int weight = BundleUtil.getContentWeight(items.stream());
        boolean full = weight >= 64;
        int fill = Mth.clamp((94 * weight) / 64, 0, 94);
        graphics.setColor(1, 1, 1, 1);
        RenderSystem.enableBlend();
        RenderSystem.enableDepthTest();
        if (fill > 0)
            graphics.blitNineSliced(FULLNESS_PROGRESS, x + 1, y, fill, 13, 2, 6, 6, full ? 6 : 0, 12);
        graphics.blitNineSliced(FULLNESS_PROGRESS, x, y, 96, 13, 2, 12, 12, 0, 0);
        Component text = null;
        if (items.isEmpty()) {
            text = Component.translatable("bundle.tooltip.empty");
        } else if (BundleUtil.getContentWeight(items.stream()) >= 64) {
            text = Component.translatable("bundle.tooltip.full");
        }
        if (text != null) {
            graphics.drawCenteredString(textRenderer, text, x + 48, y + 3, 16777215);
        }
    }

    @Inject(method = "renderImage", at = @At("HEAD"), cancellable = true)
    private void icu_bundles$RenderNewBundleUI(Font textRenderer, int x, int y, GuiGraphics graphics, CallbackInfo ci) {
        icu_bundles$DrawProgressBar(x, y + icu_bundles$ItemGridHeight() + 4, textRenderer, graphics);
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

        icu_bundles$RenderSelectedTooltip(textRenderer, graphics, x, y);
        ci.cancel();
    }

}
