package website.eccentric.tome;

import java.util.function.Consumer;

import net.minecraft.block.Blocks;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.IFinishedRecipe;
import net.minecraft.data.RecipeProvider;
import net.minecraft.data.ShapelessRecipeBuilder;
import net.minecraft.item.Items;

public class TomeRecipe extends RecipeProvider {
    public TomeRecipe(DataGenerator generator) {
        super(generator);
    }

    @Override
    protected void buildShapelessRecipes(Consumer<IFinishedRecipe> consumer) {
        ShapelessRecipeBuilder
            .shapeless(EccentricTome.TOME.get())
            .requires(Items.BOOK)
            .requires(Blocks.BOOKSHELF)
            .save(consumer);
    }
}
