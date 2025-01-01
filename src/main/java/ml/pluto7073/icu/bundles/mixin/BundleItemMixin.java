package ml.pluto7073.icu.bundles.mixin;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.sugar.Local;
import ml.pluto7073.icu.bundles.tooltip.BundleSelection;
import ml.pluto7073.icu.bundles.tooltip.SelectionBundleTooltip;
import net.minecraft.ChatFormatting;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.tooltip.BundleTooltip;
import net.minecraft.world.item.BundleItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Debug;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;
import java.util.Optional;

@Mixin(BundleItem.class)
@Debug(export = true)
public abstract class BundleItemMixin {

    @Unique private static final MutableComponent icu_bundles$BUNDLE_EMPTY_DESC =
            Component.translatable("item.minecraft.bundle.empty.description");
    @Unique private static final int FULL_BAR_COLOR = Mth.color(1.0F, 0.33F, 0.33F);

    @Shadow
    private static int getContentWeight(ItemStack stack) {
        return 0;
    }

    @Unique
    private static int icu_bundles$ItemCount(ItemStack itemStack) {
        var nbt = itemStack.getTag();
        if (nbt == null) return 0;
        return nbt.getList("Items", Tag.TAG_COMPOUND).size();
    }

    @Redirect(
            method = "getTooltipImage",
            at = @At(value = "NEW", target = "(Lnet/minecraft/core/NonNullList;I)Lnet/minecraft/world/inventory/tooltip/BundleTooltip;")
    )
    private BundleTooltip icu_bundles$SetData(NonNullList<ItemStack> inventory, int bundleOccupancy, @Local(argsOnly = true) ItemStack stack) {
        return new SelectionBundleTooltip(inventory, bundleOccupancy, BundleSelection.get(stack), BundleSelection.hasSelected(stack));
    }

    @ModifyConstant(method = "removeOne", constant = @Constant(intValue = 0))
    private static int icu_bundles$RemoveSelected(int constant, @Local(argsOnly = true) ItemStack stack) {
        return Math.max(Math.min(BundleSelection.get(stack), icu_bundles$ItemCount(stack) - 1), 0);
    }

    @Inject(method = "removeOne", at = @At("TAIL"))
    private static void icu_bundles$ResetSelection(ItemStack stack, CallbackInfoReturnable<Optional<ItemStack>> cir) {
        BundleSelection.clear(stack);
    }

    @Inject(method = "dropContents", at = @At("TAIL"))
    private static void icu_bundles$ResetAfterDrop(ItemStack stack, Player player, CallbackInfoReturnable<Boolean> cir) {
        BundleSelection.clear(stack);
    }

    @Redirect(
            method = "getMatchingItem",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/ItemStack;is(Lnet/minecraft/world/item/Item;)Z")
    )
    private static boolean icu_bundles$IsAnyBundle(ItemStack instance, Item item) {
        return instance.getItem() instanceof BundleItem;
    }

    @Redirect(
            method = "getWeight",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/ItemStack;is(Lnet/minecraft/world/item/Item;)Z")
    )
    private static boolean icu_bundles$IsAnyBundleWeight(ItemStack instance, Item item) {
        return instance.getItem() instanceof BundleItem;
    }

    @Inject(method = "appendHoverText", at = @At("HEAD"), cancellable = true)
    private void icu_bundles$ReplaceBundleTooltip(ItemStack stack, Level world, List<Component> tooltip, TooltipFlag context, CallbackInfo ci) {
        if (getContentWeight(stack) == 0) {
            tooltip.add(icu_bundles$BUNDLE_EMPTY_DESC.withStyle(ChatFormatting.GRAY));
        }
        ci.cancel();
    }

    @ModifyReturnValue(
            method = "getBarColor",
            at = @At("RETURN")
    )
    private int icu_bundles$FixBarColor(int original, ItemStack stack) {
        return getContentWeight(stack) == 64 ? FULL_BAR_COLOR : original;
    }

}
