package website.eccentric.tome.client;

import java.util.Collection;
import java.util.stream.Collectors;

import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import website.eccentric.tome.EccentricTome;
import website.eccentric.tome.Tag;
import website.eccentric.tome.network.ConvertMessage;

public class TomeScreen extends Screen {
    
    private static final int LEFT_CLICK = 0;

    private final ItemStack tome;
    
    private ItemStack book;

    public TomeScreen(ItemStack tome) {
        super(new TextComponent(""));
        this.tome = tome;
    }

    @Override
    public boolean mouseClicked(double x, double y, int button) {
        if (button != LEFT_CLICK || book == null) return super.mouseClicked(x, y, button);

        EccentricTome.CHANNEL.sendToServer(new ConvertMessage(book));
        
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

        var books = Tag.getModsBooks(tome).values().stream().flatMap(Collection::stream).collect(Collectors.toList());

        var window = minecraft.getWindow();
        var booksPerRow = 6;
        var rows = books.size() / booksPerRow + 1;
        var iconSize = 20;
        var startX = window.getGuiScaledWidth() / 2 - booksPerRow * iconSize / 2;
        var startY = window.getGuiScaledHeight() / 2 - rows * iconSize + 45;
        var padding = 4;
        fill(poseStack, startX - padding, startY - padding, startX + iconSize * booksPerRow + padding, startY + iconSize * rows + padding, 0x22000000);

        this.book = null;
        var index = 0;
        for (var book : books) {
            if (book.is(Items.AIR)) continue;

            var stackX = startX + (index % booksPerRow) * iconSize;
            var stackY = startY + (index / booksPerRow) * iconSize;

            if (mouseX > stackX && mouseY > stackY && mouseX <= (stackX + 16) && mouseY <= (stackY + 16)) {
                this.book = book;
            }

            minecraft.getItemRenderer().renderAndDecorateItem(book, stackX, stackY);
            index++;
        }

        if (this.book != null) {
            renderComponentTooltip(poseStack, getTooltipFromItem(this.book), mouseX, mouseY);
        }
    }
}
