package website.eccentric.tome;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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
        var modsBooks = Tag.getModsBooks(tome);

        if (!player.isShiftKeyDown() || !modsBooks.containsKey(mod)) return InteractionResult.PASS;

        List<ItemStack> books = modsBooks.get(mod);
        ItemStack book = books.get(books.size() - 1);
        
        player.setItemInHand(hand, convert(tome, book));

        return InteractionResult.SUCCESS;
    }
    
    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        var tome = player.getItemInHand(hand);

        if (level.isClientSide) EccentricTome.PROXY.tomeScreen(tome);

        return InteractionResultHolder.sidedSuccess(tome, level.isClientSide);
    }

    @Override
    public void appendHoverText(ItemStack tome, Level level, List<Component> tooltip, TooltipFlag advanced) {
        var modsBooks = Tag.getModsBooks(tome);
        
        for (String mod : modsBooks.keySet()) {
            tooltip.add(new StringTextComponent(Mod.name(mod)));
            List<ItemStack> books = modsBooks.get(mod);
            for (ItemStack book : books) {
                if (book.getItem() == Items.AIR) continue;
                String name = book.getHoverName().getString();
            }
        }
    }

    public static boolean isTome(ItemStack stack) {
        if (stack.isEmpty()) return false;
        else if (stack.getItem() instanceof TomeItem) return true;
        else return Tag.isTome(stack);
    }    

    public static ItemStack convert(ItemStack tome, ItemStack book) {
        Map<String, List<ItemStack>> modsBooks = Tag.getModsBooks(tome);
        String mod = Mod.from(book);
        List<ItemStack> books = modsBooks.get(mod);
        ResourceLocation registry = book.getItem().getRegistryName();
        books = books.stream().filter(b -> !b.getItem().getRegistryName().equals(registry)).collect(Collectors.toList());
        modsBooks.put(mod, books);
        Tag.setModsBooks(tome, modsBooks);

        String name = book.getHoverName().getString();
        Tag.copyMods(tome, book);
        Tag.fill(book, true);

        setHoverName(book, name);
        
        return book;
    }

    public static ItemStack revert(ItemStack book) {
        ItemStack tome = createStack();
        Tag.copyMods(book, tome);
        Tag.clear(book);

        book.resetHoverName();

        return tome;
    }

    public static ItemStack attach(ItemStack tome, ItemStack book) {
        String mod = Mod.from(book);
        Map<String, List<ItemStack>> modsBooks = Tag.getModsBooks(tome);

        List<ItemStack> books = modsBooks.getOrDefault(mod, new ArrayList<ItemStack>());
        books.add(book);
        modsBooks.put(mod, books);
        
        Tag.setModsBooks(tome, modsBooks);
        return tome;
    }

    private static ItemStack createStack() {
        return Tag.initialize(new ItemStack(EccentricTome.TOME.get()));
    }

    private static void setHoverName(ItemStack book, String name) {
        var bookName = new TextComponent(name).setStyle(Style.EMPTY.applyFormats(ChatFormatting.GREEN));
        book.setHoverName(new TranslatableComponent("eccentrictome.name", bookName));
    }

}
