package website.eccentric.tome.client;

import com.mojang.blaze3d.systems.RenderSystem;

import org.lwjgl.opengl.GL11;

import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.client.event.RenderGuiOverlayEvent;
import website.eccentric.tome.Tome;
import website.eccentric.tome.TomeItem;
import website.eccentric.tome.ModName;

public class RenderGuiOverlayHandler {
	public static void onRender(RenderGuiOverlayEvent.Post event) {
		var minecraft = Minecraft.getInstance();

		var player = minecraft.player;
		if (player == null)
			return;

		var level = minecraft.level;
		if (level == null)
			return;

		var hit = minecraft.hitResult;
		if (!(hit instanceof BlockHitResult))
			return;

		var blockHit = (BlockHitResult) hit;

		var hand = Tome.inHand(minecraft.player);
		if (hand == null)
			return;

		var state = level.getBlockState(blockHit.getBlockPos());
		if (state.isAir())
			return;

		var tome = player.getItemInHand(hand);
		if (!(tome.getItem() instanceof TomeItem))
			return;

		var mod = ModName.from(state);
		var modsBooks = Tome.getModsBooks(tome);
		if (!modsBooks.containsKey(mod))
			return;

		var books = modsBooks.get(mod);
		if (books.isEmpty())
			return;

		var book = books.get(books.size() - 1);
		var hoverName = book.getHoverName();
		var convert = I18n.get("eccentrictome.convert");

		RenderSystem.enableBlend();
		RenderSystem.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

		var window = event.getWindow();
		var x = window.getGuiScaledWidth() / 2 - 17;
		var y = window.getGuiScaledHeight() / 2 + 2;

		minecraft.getItemRenderer().renderGuiItem(book, x, y);
		minecraft.font.drawShadow(event.getPoseStack(), hoverName, x + 20, y + 4, 0xFFFFFFFF);
		minecraft.font.drawShadow(event.getPoseStack(), ChatFormatting.GRAY + convert, x + 25, y + 14, 0xFFFFFFFF);
	}
}
