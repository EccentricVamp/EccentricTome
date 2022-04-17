package website.eccentric.tome;

import java.util.function.Consumer;

import com.google.inject.Inject;

import net.minecraft.data.DataGenerator;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.ShapelessRecipeBuilder;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;

public class TomeRecipe extends RecipeProvider {

    @Inject
    private TomeItem tomeItem;
    
    public TomeRecipe(DataGenerator generator) {
        super(generator);
    }

    @Override
    protected void buildCraftingRecipes(Consumer<FinishedRecipe> consumer) {
        ShapelessRecipeBuilder
            .shapeless(tomeItem)
            .requires(Items.BOOK)
            .requires(Blocks.BOOKSHELF)
            .save(consumer);
    }
}
