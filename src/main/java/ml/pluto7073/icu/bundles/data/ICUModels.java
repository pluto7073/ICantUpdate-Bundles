package ml.pluto7073.icu.bundles.data;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import ml.pluto7073.icu.bundles.BundlesOfBravery;
import ml.pluto7073.icu.bundles.item.ICUItems;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricModelProvider;
import net.minecraft.data.models.BlockModelGenerators;
import net.minecraft.data.models.ItemModelGenerators;
import net.minecraft.data.models.model.ModelLocationUtils;
import net.minecraft.data.models.model.ModelTemplate;
import net.minecraft.data.models.model.ModelTemplates;
import net.minecraft.data.models.model.TextureMapping;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;

public class ICUModels extends FabricModelProvider {

    public ICUModels(FabricDataOutput output) {
        super(output);
    }

    @Override
    public void generateBlockStateModels(BlockModelGenerators blockStateModelGenerator) {}

    @Override
    public void generateItemModels(ItemModelGenerators gens) {
        ICUItems.BUNDLES.forEach((id, bundle) -> generateBundle(bundle, gens));
    }

    private static void generateBundle(Item bundle, ItemModelGenerators gens) {
        ResourceLocation filledId = ModelLocationUtils.getModelLocation(bundle, "_filled");

        JsonObject overrides = new JsonObject();
        JsonObject predicate = new JsonObject();
        predicate.addProperty(BundlesOfBravery.asId("filled").toString(), 1E-7);
        overrides.add("predicate", predicate);
        overrides.addProperty("model", filledId.toString());
        JsonArray overridesArray = new JsonArray();
        overridesArray.add(overrides);

        ModelTemplates.FLAT_ITEM.create(ModelLocationUtils.getModelLocation(bundle), TextureMapping.layer0(bundle), gens.output, (id, textures) -> {
            JsonObject base = ModelTemplates.FLAT_ITEM.createBaseTemplate(id, textures);
            base.add("overrides", overridesArray);
            return base;
        });

        generateFlatItemModel(filledId, gens);
    }

    private static void generateFlatItemModel(ResourceLocation location, ItemModelGenerators gens) {
        ModelTemplates.FLAT_ITEM.create(location, TextureMapping.layer0(location), gens.output);
    }

}
