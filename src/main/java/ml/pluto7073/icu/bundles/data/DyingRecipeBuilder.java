package ml.pluto7073.icu.bundles.data;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import ml.pluto7073.icu.bundles.recipe.DyingRecipe;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.RequirementsStrategy;
import net.minecraft.advancements.critereon.RecipeUnlockedTrigger;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.recipes.CraftingRecipeBuilder;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.ShapelessRecipeBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.ItemLike;
import org.jetbrains.annotations.Nullable;

import java.util.Iterator;
import java.util.List;
import java.util.function.Consumer;

public class DyingRecipeBuilder extends ShapelessRecipeBuilder {

    public DyingRecipeBuilder(RecipeCategory category, ItemLike result, int resultCount) {
        super(category, result, resultCount);
    }

    public void save(Consumer<FinishedRecipe> exporter, ResourceLocation recipeId) {
        this.ensureValid(recipeId);
        this.advancement.parent(ROOT_RECIPE_ADVANCEMENT).addCriterion("has_the_recipe", RecipeUnlockedTrigger.unlocked(recipeId)).rewards(net.minecraft.advancements.AdvancementRewards.Builder.recipe(recipeId)).requirements(RequirementsStrategy.OR);
        exporter.accept(new Result(recipeId, this.result, this.count, this.group == null ? "" : this.group, determineBookCategory(this.category), this.ingredients, this.advancement, recipeId.withPrefix("recipes/" + this.category.getFolderName() + "/")));
    }

    @MethodsReturnNonnullByDefault
    public static class Result extends ShapelessRecipeBuilder.Result {

        public Result(ResourceLocation recipeId, Item result, int count, String group, CraftingBookCategory category, List<Ingredient> ingredients, Advancement.Builder builder, ResourceLocation advancementId) {
            super(recipeId, result, count, group, category, ingredients, builder, advancementId);
        }

        public RecipeSerializer<?> getType() {
            return DyingRecipe.SERIALIZER;
        }

    }

}
