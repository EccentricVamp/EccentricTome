package website.eccentric.tome;

import java.util.List;

import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.world.item.ItemStack;

public class TomeScreen extends Screen {

    private final ItemStack tome;
    private String mod;

    protected TomeScreen(ItemStack tome) {
        super(new TextComponent(""));
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

        var books = Tag.getBooks(tome);
        var window = minecraft.getWindow();
        var booksPerRow = 6;
        var rows = books.size() / booksPerRow + 1;
        var iconSize = 20;
        var startX = window.getGuiScaledWidth() / 2 - booksPerRow * iconSize / 2;
        var startY = window.getGuiScaledHeight() / 2 - rows * iconSize + 45;
        var padding = 4;
        fill(poseStack, startX - padding, startY - padding, startX + iconSize * booksPerRow + padding, startY + iconSize * rows + padding, 0x22000000);

        this.mod = null;
        String name = null;
        var index = 0;
        for (var mod : books.getAllKeys()) {
            var book = ItemStack.of(books.getCompound(mod));
            var stackX = startX + (index % booksPerRow) * iconSize;
            var stackY = startY + (index / booksPerRow) * iconSize;

            if (mouseX > stackX && mouseY > stackY && mouseX <= (stackX + 16) && mouseY <= (stackY + 16)) {
                this.mod = mod;
                name = book.getHoverName().getString();
            }

            minecraft.getItemRenderer().renderAndDecorateItem(book, stackX, stackY);
        }

        if (this.mod != null) {
            var tooltips = List.of(new TextComponent(name), new TextComponent(ChatFormatting.GRAY + Mod.name(this.mod)));
            renderComponentTooltip(poseStack, tooltips, mouseX, mouseY, font);
        }
    }

}
