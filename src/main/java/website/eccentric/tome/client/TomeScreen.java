package website.eccentric.tome.client;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import com.mojang.blaze3d.matrix.MatrixStack;

import net.minecraft.client.MainWindow;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.text.StringTextComponent;
import website.eccentric.tome.EccentricTome;
import website.eccentric.tome.Tome;
import website.eccentric.tome.network.ConvertMessage;

public class TomeScreen extends Screen {
    
    private static final int LEFT_CLICK = 0;

    private final ItemStack tome;
    
    private ItemStack book;

    public TomeScreen(ItemStack tome) {
        super(new StringTextComponent(""));
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
    public void render(MatrixStack poseStack, int mouseX, int mouseY, float ticks) { 
        super.render(poseStack, mouseX, mouseY, ticks);

        List<ItemStack> books = Tome.getModsBooks(tome).values().stream()
            .flatMap(Collection::stream)
            .collect(Collectors.toList());

        MainWindow window = minecraft.getWindow();
        int booksPerRow = 6;
        int rows = books.size() / booksPerRow + 1;
        int iconSize = 20;
        int startX = window.getGuiScaledWidth() / 2 - booksPerRow * iconSize / 2;
        int startY = window.getGuiScaledHeight() / 2 - rows * iconSize + 45;
        int padding = 4;
        fill(poseStack, startX - padding, startY - padding, startX + iconSize * booksPerRow + padding, startY + iconSize * rows + padding, 0x22000000);

        this.book = null;
        int index = 0;
        for (ItemStack book : books) {
            if (book.getItem() == Items.AIR) continue;

            int stackX = startX + (index % booksPerRow) * iconSize;
            int stackY = startY + (index / booksPerRow) * iconSize;

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
