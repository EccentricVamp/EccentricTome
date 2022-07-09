package website.eccentric.tome.client.gui;

import com.mojang.blaze3d.systems.RenderSystem;

import java.util.List;
import java.util.Map;

import org.lwjgl.opengl.GL11;

import net.minecraft.block.BlockState;
import net.minecraft.client.MainWindow;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType;
import website.eccentric.tome.Tag;
import website.eccentric.tome.TomeItem;
import website.eccentric.tome.services.ModName;

public class RenderGameOverlayHandler {
	public static void onRender(RenderGameOverlayEvent.Post event) {
		if (event.getType() != ElementType.ALL) return;

		Minecraft minecraft = Minecraft.getInstance();
		RayTraceResult hit = minecraft.hitResult;
		if (!(hit instanceof BlockRayTraceResult)) return;

		BlockRayTraceResult blockHit = (BlockRayTraceResult)hit;

		Hand hand = TomeItem.inHand(minecraft.player);
		if (hand == null) return;

		BlockState state = minecraft.level.getBlockState(blockHit.getBlockPos());
		if (state.isAir(minecraft.level, blockHit.getBlockPos())) return;

		ItemStack tome = minecraft.player.getItemInHand(hand);
		String mod = ModName.from(state);
		Map<String, List<ItemStack>> modsBooks = Tag.getModsBooks(tome);
		if (!modsBooks.containsKey(mod)) return;

		List<ItemStack> books = modsBooks.get(mod);
		if (books.isEmpty()) return;

		ItemStack book = books.get(books.size() - 1);
		ITextComponent hoverName = book.getHoverName();
		String convert = I18n.get("eccentrictome.convert");

		RenderSystem.enableBlend();
		RenderSystem.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

		MainWindow window = event.getWindow();
		int x = window.getGuiScaledWidth() / 2 - 17;
		int y = window.getGuiScaledHeight() / 2 + 2;

		minecraft.getItemRenderer().renderGuiItem(book, x, y);
		minecraft.font.drawShadow(event.getMatrixStack(), hoverName, x + 20, y + 4, 0xFFFFFFFF);
		minecraft.font.drawShadow(event.getMatrixStack(), TextFormatting.GRAY + convert, x + 25, y + 14, 0xFFFFFFFF);
	}
}
