package ml.pluto7073.icu.bundles.data;

import ml.pluto7073.icu.bundles.item.ICUItems;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeCategory;

import java.util.function.Consumer;

public class ICURecipes extends FabricRecipeProvider {

    public ICURecipes(FabricDataOutput output) {
        super(output);
    }

    @Override
    public void buildRecipes(Consumer<FinishedRecipe> exporter) {
        ICUItems.BUNDLES.forEach((id, bundle) -> new DyingRecipeBuilder(RecipeCategory.TOOLS, bundle, 1)
                .requires(ICUItemTags.BUNDLES)
                .requires(BuiltInRegistries.ITEM.get(id.withPath(path -> path.replace("bundle", "dye"))))
                .group("bundles")
                .unlockedBy("has_bundle", has(ICUItemTags.BUNDLES))
                .save(exporter, id));
    }

}
