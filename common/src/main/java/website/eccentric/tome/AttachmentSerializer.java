package website.eccentric.tome;

import com.google.gson.JsonObject;
import java.util.function.Function;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.RecipeSerializer;

public class AttachmentSerializer implements RecipeSerializer<AttachmentRecipe> {
   private final Function<ResourceLocation, AttachmentRecipe> constructor;

   public AttachmentSerializer(Function<ResourceLocation, AttachmentRecipe> constructor) {
      this.constructor = constructor;
   }

   public AttachmentRecipe fromJson(ResourceLocation location, JsonObject json) {
      return this.constructor.apply(location);
   }

   public AttachmentRecipe fromNetwork(ResourceLocation location, FriendlyByteBuf buffer) {
      return this.constructor.apply(location);
   }

   public void toNetwork(FriendlyByteBuf buffer, AttachmentRecipe recipe) {
   }
}