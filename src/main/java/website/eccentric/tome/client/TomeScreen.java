package website.eccentric.tome.client;

import java.util.Collection;
import java.util.stream.Collectors;

import com.mojang.blaze3d.platform.InputConstants;
import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import website.eccentric.tome.EccentricTome;
import website.eccentric.tome.Tome;
import website.eccentric.tome.network.ConvertMessage;

public class TomeScreen extends Screen {
    private static final int LEFT_CLICK = 0;

    private final ItemStack tome;

    private ItemStack book;

    public TomeScreen(ItemStack tome) {
        super(Component.empty());
        this.tome = tome;
    }

    @Override
    public boolean mouseClicked(double x, double y, int button) {
        if (button != LEFT_CLICK || book == null)
            return super.mouseClicked(x, y, button);

        EccentricTome.CHANNEL.sendToServer(new ConvertMessage(book));

        this.onClose();
        return true;
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        var minecraft = this.minecraft;
        var key = InputConstants.getKey(keyCode, scanCode);
        if (super.keyPressed(keyCode, scanCode, modifiers)) {
            return true;
        } else if (minecraft != null && minecraft.options.keyInventory.isActiveAndMatches(key)) {
            this.onClose();
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }

    @Override
    public void render(PoseStack poseStack, int mouseX, int mouseY, float ticks) {
        var minecraft = this.minecraft;
        if (minecraft == null)
            return;

        super.render(poseStack, mouseX, mouseY, ticks);

        var books = Tome.getModsBooks(tome).values().stream()
                .flatMap(Collection::stream)
                .collect(Collectors.toList());

        var window = minecraft.getWindow();
        var booksPerRow = 6;
        var rows = books.size() / booksPerRow + 1;
        var iconSize = 20;
        var startX = window.getGuiScaledWidth() / 2 - booksPerRow * iconSize / 2;
        var startY = window.getGuiScaledHeight() / 2 - rows * iconSize + 45;
        var padding = 4;
        fill(poseStack, startX - padding, startY - padding, startX + iconSize * booksPerRow + padding,
                startY + iconSize * rows + padding, 0x22000000);

        this.book = null;
        var index = 0;
        for (var book : books) {
            if (book.is(Items.AIR))
                continue;

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
