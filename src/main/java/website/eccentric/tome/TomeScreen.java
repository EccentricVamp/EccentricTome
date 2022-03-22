package website.eccentric.tome;

import java.util.ArrayList;
import java.util.Arrays;

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
		if (button != 0 || mod == null) return super.mouseClicked(x, y, button);

		EccentricTome.CHANNEL.sendToServer(new ConvertMessage(mod));
		this.minecraft.setScreen(null);
		return true;
	}

	@Override
	public boolean isPauseScreen() {
		return false;
	}

	@Override
	public void render(PoseStack poseStack, int mouseX, int mouseY, float ticks) { 
		super.render(poseStack, mouseX, mouseY, ticks);

		var data = Tag.getData(tome);

		var stacks = new ArrayList<ItemStack>();
		for (var key : data.getAllKeys()) {
			stacks.add(ItemStack.of(data.getCompound(key)));
		}

		var window = minecraft.getWindow();
		var booksPerRow = 6;
		var rows = stacks.size() / booksPerRow + 1;
		var iconSize = 20;
		var startX = window.getGuiScaledWidth() / 2 - booksPerRow * iconSize / 2;
		var startY = window.getGuiScaledHeight() / 2 - rows * iconSize + 45;
		var padding = 4;
		fill(poseStack, startX - padding, startY - padding, startX + iconSize * booksPerRow + padding, startY + iconSize * rows + padding, 0x22000000);

		ItemStack tooltipStack = null;
        var index = 0;
        for (var stack : stacks) {
            var stackX = startX + (index % booksPerRow) * iconSize;
            var stackY = startY + (index / booksPerRow) * iconSize;

            if (mouseX > stackX && mouseY > stackY && mouseX <= (stackX + 16) && mouseY <= (stackY + 16)) {
                tooltipStack = stack;
            }

			this.minecraft.getItemRenderer().renderAndDecorateItem(stack, stackX, stackY);
        }

		if (tooltipStack != null) {
			mod = GetMod.from(tooltipStack);
			var hoverName = tooltipStack.getHoverName().getString();
			var tooltipList = Arrays.asList(new TextComponent(hoverName), new TextComponent(ChatFormatting.GRAY + GetMod.name(mod)));

			renderComponentTooltip(poseStack, tooltipList, mouseX, mouseY, this.font);
		}
		else mod = null;
	}

}
