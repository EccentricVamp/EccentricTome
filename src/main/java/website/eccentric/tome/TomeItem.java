package website.eccentric.tome;

import java.util.List;

import javax.annotation.Nullable;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.MinecraftForge;
import website.eccentric.tome.events.OpenTomeEvent;

public class TomeItem extends Item {
    public TomeItem() {
        super(new Properties().stacksTo(1));
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        var player = context.getPlayer();
        if (player == null)
            return InteractionResult.PASS;

        var hand = context.getHand();
        var position = context.getClickedPos();
        var tome = context.getItemInHand();
        var mod = ModName.from(context.getLevel().getBlockState(position));
        var modsBooks = Tome.getModsBooks(tome);

        if (!player.isShiftKeyDown() || !modsBooks.containsKey(mod))
            return InteractionResult.PASS;

        var books = modsBooks.get(mod);
        var book = books.get(books.size() - 1);

        player.setItemInHand(hand, Tome.convert(tome, book));

        return InteractionResult.SUCCESS;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        var tome = player.getItemInHand(hand);

        if (level.isClientSide)
            MinecraftForge.EVENT_BUS.post(new OpenTomeEvent(tome));

        return InteractionResultHolder.sidedSuccess(tome, level.isClientSide);
    }

    @Override
    public void appendHoverText(ItemStack tome, @Nullable Level level, List<Component> tooltip, TooltipFlag advanced) {
        var modsBooks = Tome.getModsBooks(tome);

        for (var mod : modsBooks.keySet()) {
            tooltip.add(Component.literal(ModName.name(mod)));
            var books = modsBooks.get(mod);

            for (var book : books) {
                if (book.is(Items.AIR))
                    continue;
                var name = book.getHoverName().getString();
                tooltip.add(Component.literal("  " + ChatFormatting.GRAY + name));
            }
        }
    }
}
