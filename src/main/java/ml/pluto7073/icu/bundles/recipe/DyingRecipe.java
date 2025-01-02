package ml.pluto7073.icu.bundles.recipe;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import ml.pluto7073.icu.bundles.BundlesOfBravery;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.NonNullList;
import net.minecraft.core.Registry;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.Container;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.BundleItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;

import java.util.Iterator;
import java.util.function.Predicate;

@MethodsReturnNonnullByDefault
public class DyingRecipe extends ShapelessRecipe {

    public static final RecipeSerializer<DyingRecipe> SERIALIZER = new Serializer();

    public DyingRecipe(ResourceLocation id, String group, CraftingBookCategory category, ItemStack result, NonNullList<Ingredient> ingredients) {
        super(id, group, category, result, ingredients);
    }

    @Override
    public ItemStack assemble(CraftingContainer craftingContainer, RegistryAccess registryAccess) {
        ItemStack result = super.assemble(craftingContainer, registryAccess);
        ItemStack copyFrom = find(craftingContainer, BundleItem.class::isInstance);
        result.setTag(copyFrom.getOrCreateTag().copy());
        return result;
    }

    private static ItemStack find(Container container, Predicate<Item> matcher) {
        for (int i = 0; i < container.getContainerSize(); i++) {
            ItemStack stack = container.getItem(i);
            if (matcher.test(stack.getItem())) return stack;
        }
        return ItemStack.EMPTY;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return SERIALIZER;
    }

    public static void init() {
        Registry.register(BuiltInRegistries.RECIPE_SERIALIZER, BundlesOfBravery.asId("dying_shapeless"), SERIALIZER);
    }

    public static class Serializer implements RecipeSerializer<DyingRecipe> {

        public DyingRecipe fromJson(ResourceLocation resourceLocation, JsonObject jsonObject) {
            String string = GsonHelper.getAsString(jsonObject, "group", "");
            CraftingBookCategory craftingBookCategory = CraftingBookCategory.CODEC.byName(GsonHelper.getAsString(jsonObject, "category", null), CraftingBookCategory.MISC);
            NonNullList<Ingredient> nonNullList = itemsFromJson(GsonHelper.getAsJsonArray(jsonObject, "ingredients"));
            if (nonNullList.isEmpty()) {
                throw new JsonParseException("No ingredients for shapeless recipe");
            } else if (nonNullList.size() > 9) {
                throw new JsonParseException("Too many ingredients for shapeless recipe");
            } else {
                ItemStack itemStack = ShapedRecipe.itemStackFromJson(GsonHelper.getAsJsonObject(jsonObject, "result"));
                return new DyingRecipe(resourceLocation, string, craftingBookCategory, itemStack, nonNullList);
            }
        }

        private static NonNullList<Ingredient> itemsFromJson(JsonArray json) {
            NonNullList<Ingredient> nonNullList = NonNullList.create();

            for(int i = 0; i < json.size(); ++i) {
                Ingredient ingredient = Ingredient.fromJson(json.get(i), false);
                if (!ingredient.isEmpty()) {
                    nonNullList.add(ingredient);
                }
            }

            return nonNullList;
        }

        public DyingRecipe fromNetwork(ResourceLocation resourceLocation, FriendlyByteBuf friendlyByteBuf) {
            String string = friendlyByteBuf.readUtf();
            CraftingBookCategory craftingBookCategory = friendlyByteBuf.readEnum(CraftingBookCategory.class);
            int i = friendlyByteBuf.readVarInt();
            NonNullList<Ingredient> nonNullList = NonNullList.withSize(i, Ingredient.EMPTY);

            nonNullList.replaceAll(ignored -> Ingredient.fromNetwork(friendlyByteBuf));

            ItemStack itemStack = friendlyByteBuf.readItem();
            return new DyingRecipe(resourceLocation, string, craftingBookCategory, itemStack, nonNullList);
        }

        public void toNetwork(FriendlyByteBuf friendlyByteBuf, DyingRecipe shapelessRecipe) {
            RecipeSerializer.SHAPELESS_RECIPE.toNetwork(friendlyByteBuf, shapelessRecipe);
        }
    }

}
