package website.eccentric.tome;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import net.minecraft.ChatFormatting;
import net.minecraft.core.Registry;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import website.eccentric.tome.util.Tag;

public class TomeItem extends Item {
    
    public TomeItem() {
        super(new Properties().stacksTo(1).tab(CreativeModeTab.TAB_TOOLS));
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        var player = context.getPlayer();
        var hand = context.getHand();
        var level = context.getLevel();
        var position = context.getClickedPos();
        var tome = player.getItemInHand(hand);
        var mod = Services.MOD.from(level.getBlockState(position));
        var modsBooks = Tag.getModsBooks(tome);

        if (!player.isShiftKeyDown() || !modsBooks.containsKey(mod)) return InteractionResult.PASS;

        var books = modsBooks.get(mod);
        var book = books.get(books.size() - 1);
        
        player.setItemInHand(hand, convert(tome, book));

        return InteractionResult.SUCCESS;
    }
    
    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        var tome = player.getItemInHand(hand);

        //if (level.isClientSide) EccentricTome.PROXY.tomeScreen(tome);

        return InteractionResultHolder.sidedSuccess(tome, level.isClientSide);
    }

    @Override
    public void appendHoverText(ItemStack tome, Level level, List<Component> tooltip, TooltipFlag advanced) {
        var modsBooks = Tag.getModsBooks(tome);
        
        for (var mod : modsBooks.keySet()) {
            tooltip.add(new TextComponent(Services.MOD.name(mod)));
            var books = modsBooks.get(mod);
            for (var book : books) {
                if (book.is(Items.AIR)) continue;
                var name = book.getHoverName().getString();
                tooltip.add(new TextComponent("  " + ChatFormatting.GRAY + name));
            }
        }
    }

    public static boolean isTome(ItemStack stack) {
        if (stack.isEmpty()) return false;
        else if (stack.getItem() instanceof TomeItem) return true;
        else return Tag.isTome(stack);
    }

    public static InteractionHand inHand(Player player) {
        var hand = InteractionHand.MAIN_HAND;
        var stack = player.getItemInHand(hand);
        if (isTome(stack)) return hand;
        
        hand = InteractionHand.OFF_HAND;
        stack = player.getItemInHand(hand);
        if (isTome(stack)) return hand;

        return null;
    }

    public ItemStack convert(ItemStack tome, ItemStack book) {
        var modsBooks = Tag.getModsBooks(tome);
        var mod = Services.MOD.from(book);
        var books = modsBooks.get(mod);
        var registry = Registry.ITEM.getKey(book.getItem());
        books = books.stream().filter(b -> !Registry.ITEM.getKey(b.getItem()).equals(registry)).collect(Collectors.toList());
        modsBooks.put(mod, books);
        Tag.setModsBooks(tome, modsBooks);

        var name = book.getHoverName().getString();
        Tag.copyMods(tome, book);
        Tag.fill(book, true);

        setHoverName(book, name);
        
        return book;
    }

    public ItemStack revert(ItemStack book) {
        var tome = createStack();
        Tag.copyMods(book, tome);
        Tag.clear(book);

        book.resetHoverName();

        return tome;
    }

    public ItemStack attach(ItemStack tome, ItemStack book) {
        var mod = Services.MOD.from(book);
        var modsBooks = Tag.getModsBooks(tome);

        var books = modsBooks.getOrDefault(mod, new ArrayList<ItemStack>());
        books.add(book);
        modsBooks.put(mod, books);
        
        Tag.setModsBooks(tome, modsBooks);
        return tome;
    }

    private ItemStack createStack() {
        return Tag.initialize(new ItemStack(this));
    }

    private void setHoverName(ItemStack book, String name) {
        var bookName = new TextComponent(name).setStyle(Style.EMPTY.applyFormats(ChatFormatting.GREEN));
        book.setHoverName(new TranslatableComponent("eccentrictome.name", bookName));
    }

}
