package website.eccentric.tome.client.gui;

import com.mojang.blaze3d.systems.RenderSystem;

import org.lwjgl.opengl.GL11;

import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType;
import website.eccentric.tome.Tag;
import website.eccentric.tome.TomeItem;
import website.eccentric.tome.services.ModName;

public class RenderGameOverlayHandler {
	public static void onRender(RenderGameOverlayEvent.Post event) {
		if (event.getType() != ElementType.ALL) return;

		var minecraft = Minecraft.getInstance();
		var hit = minecraft.hitResult;
		if (!(hit instanceof BlockHitResult)) return;

		var blockHit = (BlockHitResult)hit;

		var hand = TomeItem.inHand(minecraft.player);
		if (hand == null) return;

		var state = minecraft.level.getBlockState(blockHit.getBlockPos());
		if (state.isAir()) return;

		var tome = minecraft.player.getItemInHand(hand);
		var mod = ModName.from(state);
		var modsBooks = Tag.getModsBooks(tome);
		if (!modsBooks.containsKey(mod)) return;

		var books = modsBooks.get(mod);
		if (books.isEmpty()) return;

		var book = books.get(books.size() - 1);
		var hoverName = book.getHoverName();
		var convert = I18n.get("eccentrictome.convert");

		RenderSystem.enableBlend();
		RenderSystem.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

		var window = event.getWindow();
		var x = window.getGuiScaledWidth() / 2 - 17;
		var y = window.getGuiScaledHeight() / 2 + 2;

		minecraft.getItemRenderer().renderGuiItem(book, x, y);
		minecraft.font.drawShadow(event.getMatrixStack(), hoverName, x + 20, y + 4, 0xFFFFFFFF);
		minecraft.font.drawShadow(event.getMatrixStack(), ChatFormatting.GRAY + convert, x + 25, y + 14, 0xFFFFFFFF);
	}
}
