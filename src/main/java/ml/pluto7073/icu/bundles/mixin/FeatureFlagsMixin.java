package ml.pluto7073.icu.bundles.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.world.flag.FeatureFlag;
import net.minecraft.world.flag.FeatureFlagSet;
import net.minecraft.world.flag.FeatureFlags;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

import static org.objectweb.asm.Opcodes.GETSTATIC;

@Mixin(FeatureFlags.class)
public class FeatureFlagsMixin {

    @Shadow @Final public static FeatureFlag BUNDLE;

    @ModifyExpressionValue(
            method = "<clinit>",
            at = @At(value = "FIELD", target = "Lnet/minecraft/world/flag/FeatureFlags;VANILLA_SET:Lnet/minecraft/world/flag/FeatureFlagSet;",
                    opcode = GETSTATIC)
    )
    private static FeatureFlagSet icu_bundles$CombineSets(FeatureFlagSet vanilla) {
        return vanilla.join(FeatureFlagSet.of(BUNDLE));
    }

}
