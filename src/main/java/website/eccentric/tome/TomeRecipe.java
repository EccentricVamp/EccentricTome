package website.eccentric.tome;

import java.util.function.Consumer;

import net.minecraft.data.DataGenerator;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.ShapelessRecipeBuilder;
import net.minecraft.world.item.Items;
import net.minecraftforge.common.Tags;

public class TomeRecipe extends RecipeProvider {

    public TomeRecipe(DataGenerator generator) {
        super(generator);
    }

    @Override
    protected void buildCraftingRecipes(Consumer<FinishedRecipe> consumer) {
        ShapelessRecipeBuilder
            .shapeless(EccentricTome.TOME.get())
            .requires(Items.BOOK)
            .requires(Tags.Items.BOOKSHELVES)
            .unlockedBy("has_book", has(Items.BOOK))
            .save(consumer);
    }
    
}
