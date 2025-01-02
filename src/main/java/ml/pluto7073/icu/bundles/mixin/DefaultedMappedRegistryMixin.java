package ml.pluto7073.icu.bundles.mixin;

import net.minecraft.core.DefaultedMappedRegistry;
import net.minecraft.resources.ResourceLocation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(DefaultedMappedRegistry.class)
public class DefaultedMappedRegistryMixin {

    @ModifyVariable(
            at = @At("HEAD"),
            method = "get",
            ordinal = 0,
            argsOnly = true
    )
    private ResourceLocation icu_bundles$ConvertFromBundleBackport(ResourceLocation id) {
        if (id.getNamespace().equals("bundle-backportish")) {
            return new ResourceLocation(id.getPath());
        }
        return id;
    }

}
