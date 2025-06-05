package ml.pluto7073.icu.bundles.mixin.mousetweaks;

import com.llamalad7.mixinextras.sugar.Local;
import ml.pluto7073.icu.bundles.data.ICUItemTags;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.world.inventory.Slot;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import yalter.mousetweaks.Main;

@Mixin(value = Main.class, remap = false)
public class MainMixin {

    @Inject(at = @At(value = "INVOKE", target = "Lyalter/mousetweaks/ScrollItemScaling;scale(D)D"), method = "onMouseScrolled", cancellable = true)
    private static void icu_bundles$FixOnScrolledBehavior(Screen screen, double x, double y, double scrollDelta, CallbackInfoReturnable<Boolean> cir, @Local(ordinal = 0) Slot selectedSlot) {
        if (selectedSlot.hasItem() && selectedSlot.getItem().is(ICUItemTags.BUNDLES)) {
            cir.setReturnValue(false);
        }
    }

}
