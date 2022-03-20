package website.eccentric.tome;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.world.item.ItemStack;

public class TomeScreen extends Screen {

    private static final String EMPTY = "";

    private final ItemStack tome;
    String mod;

    protected TomeScreen(ItemStack tome) {
        super(new TextComponent(EMPTY));
        this.tome = tome;
    }

	@Override
	public boolean mouseClicked(double x, double y, int button) {
		if (button == 0 && mod != null) {
			EccentricTome.CHANNEL.sendToServer(new TransformMessage(mod));
			this.minecraft.setScreen(null);
			return true;
		}

		return super.mouseClicked(x, y, button);
	}

	@Override
	public boolean isPauseScreen() {
		return false;
	}

	@Override
	public void render(PoseStack matrixStack, int mouseX, int mouseY, float ticks) {
		mod = null;
		super.render(matrixStack, mouseX, mouseY, ticks);

		var stacks = new ArrayList<ItemStack>();

		if (this.tome.hasTag()) {
			var data = this.tome.getTag().getCompound(TomeItem.TAG_DATA);
			var keys = new ArrayList<String>(data.getAllKeys());
			Collections.sort(keys);

			for (var key : keys) {
				var tag = data.getCompound(key);
                if (tag == null) continue;

                stacks.add(ItemStack.of(tag));
			}
		}

		var window = this.minecraft.getWindow();
		int centerX = window.getGuiScaledWidth() / 2;
		int centerY = window.getGuiScaledHeight() / 2;

		int booksPerRow = 6;
		int rows = stacks.size() / booksPerRow + 1;
		int iconSize = 20;

		int startX = centerX - (booksPerRow * iconSize) / 2;
		int startY = centerY - (rows * iconSize) + 45;

		int padding = 4;
		int extra = 2;
		fill(matrixStack, startX - padding, startY - padding, startX + iconSize * booksPerRow + padding, startY + iconSize * rows + padding, 0x22000000);
		fill(matrixStack, startX - padding - extra, startY - padding - extra, startX + iconSize * booksPerRow + padding + extra, startY + iconSize * rows + padding + extra, 0x22000000);

		var tooltipStack = ItemStack.EMPTY;

        var index = 0;
        for (var stack : stacks) {
            int x = startX + (index % booksPerRow) * iconSize;
            int y = startY + (index / booksPerRow) * iconSize;

            if (x > x && y > y && x <= (x + 16) && y <= (y + 16)) {
                tooltipStack = stack;
                y -= 2;
            }

            minecraft.getItemRenderer().renderAndDecorateItem(stack, x, y);
            index++;
        }

		if (!tooltipStack.isEmpty()) {
			var stackName = tooltipStack.getTag().getCompound(TomeItem.TAG_NAME).getString("text");
			var stackMod = ChatFormatting.GRAY + GetMod.name(GetMod.from(tooltipStack));
			var tooltipList = Arrays.asList(new TextComponent(stackName), new TextComponent(stackMod));

			renderComponentTooltip(matrixStack, tooltipList, mouseX, mouseY, this.font);

			mod = tooltipStack.getTag().getString(TomeItem.TAG_MOD);
		}
	}

}
