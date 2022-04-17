package website.eccentric.tome;

import com.google.gson.JsonObject;
import java.util.function.Function;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.RecipeSerializer;

public class AttachmentSerializer implements RecipeSerializer<AttachmentRecipe> {
   private final Function<ResourceLocation, AttachmentRecipe> _constructor;

   public AttachmentSerializer(Function<ResourceLocation, AttachmentRecipe> constructor) {
      _constructor = constructor;
   }

   public AttachmentRecipe fromJson(ResourceLocation location, JsonObject json) {
      return this._constructor.apply(location);
   }

   public AttachmentRecipe fromNetwork(ResourceLocation location, FriendlyByteBuf buffer) {
      return this._constructor.apply(location);
   }

   public void toNetwork(FriendlyByteBuf buffer, AttachmentRecipe recipe) {
   }
}