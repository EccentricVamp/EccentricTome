package website.eccentric.tome;

import java.util.List;

import net.minecraft.ChatFormatting;
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
        var mod = Mod.from(level.getBlockState(position));

        if (!player.isShiftKeyDown() || !Tag.hasBook(tome, mod)) return InteractionResult.PASS;
        
        player.setItemInHand(hand, convert(tome, mod));

        return InteractionResult.SUCCESS;
    }
    
    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        var tome = player.getItemInHand(hand);
        if (Tag.getBooks(tome).isEmpty()) return InteractionResultHolder.fail(tome);

        if (level.isClientSide) EccentricTome.PROXY.tomeScreen(tome);

        return InteractionResultHolder.sidedSuccess(tome, level.isClientSide);
    }

    @Override
    public void appendHoverText(ItemStack tome, Level level, List<Component> tooltip, TooltipFlag advanced) {
        var books = Tag.getBooks(tome);
        for (var mod : books.getAllKeys()) {
            var book = ItemStack.of(books.getCompound(mod));
            if (book.is(Items.AIR)) continue;
               
            var name = book.getHoverName().getString();
            
            tooltip.add(new TextComponent(Mod.name(mod)));
            tooltip.add(new TextComponent("  " + ChatFormatting.GRAY + name));
        }
    }

    public static boolean isTome(ItemStack stack) {
        if (stack.isEmpty()) return false;
        else if (stack.getItem() instanceof TomeItem) return true;
        else return Tag.isTome(stack);
    }    

    public static ItemStack convert(ItemStack tome, String mod) {
        var book = ItemStack.of(Tag.popBook(tome, mod));
        var name = book.getHoverName().getString();
        Tag.copyBooks(tome, book);
        Tag.fill(book, mod, name, true);

        setHoverName(book, name);
        
        return book;
    }

    public static ItemStack revert(ItemStack book) {
        var tome = createStack();
        Tag.copyBooks(book, tome);
        Tag.clear(book);

        book.resetHoverName();

        return attach(tome, book);
    }

    public static ItemStack detatch(ItemStack book) {
        var mod = Tag.getMod(book);
        var tome = revert(book);
        Tag.popBook(tome, mod);
        
        return tome;
    }

    public static ItemStack attach(ItemStack tome, ItemStack book) {
        Tag.addBook(tome, book);
        return tome;
    }

    private static ItemStack createStack() {
        return new ItemStack(EccentricTome.TOME.get());
    }

    private static void setHoverName(ItemStack book, String name) {
        var bookName = new TextComponent(name).setStyle(Style.EMPTY.applyFormats(ChatFormatting.GREEN));
        book.setHoverName(new TranslatableComponent("eccentrictome.name", bookName));
    }

}
