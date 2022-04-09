package website.eccentric.tome;

import java.util.function.Consumer;

import net.minecraft.data.DataGenerator;
import net.minecraft.data.IFinishedRecipe;
import net.minecraft.data.RecipeProvider;
import net.minecraft.data.ShapelessRecipeBuilder;
import net.minecraft.item.Items;
import net.minecraftforge.common.Tags;

public class TomeRecipe extends RecipeProvider {

    public TomeRecipe(DataGenerator generator) {
        super(generator);
    }

    @Override
    protected void buildShapelessRecipes(Consumer<IFinishedRecipe> consumer) {
        ShapelessRecipeBuilder
            .shapeless(EccentricTome.TOME.get())
            .requires(Items.BOOK)
            .requires(Tags.Items.BOOKSHELVES)
            .unlockedBy("has_book", has(Items.BOOK))
            .save(consumer);
    }
    
}
