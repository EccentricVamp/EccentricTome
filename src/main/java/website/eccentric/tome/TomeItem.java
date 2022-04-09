package website.eccentric.tome;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.item.Items;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.IFormattableTextComponent;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;

public class TomeItem extends Item {

    public TomeItem() {
        super(new Properties().stacksTo(1).tab(ItemGroup.TAB_TOOLS));
    }

    @Override
    public ActionResultType useOn(ItemUseContext context) {
        PlayerEntity player = context.getPlayer();
        Hand hand = context.getHand();
        World level = context.getLevel();
        BlockPos position = context.getClickedPos();
        ItemStack tome = player.getItemInHand(hand);
        String mod = Mod.from(level.getBlockState(position));
        Map<String, List<ItemStack>> modsBooks = Tag.getModsBooks(tome);

        if (!player.isShiftKeyDown() || !modsBooks.containsKey(mod)) return ActionResultType.PASS;

        List<ItemStack> books = modsBooks.get(mod);
        ItemStack book = books.get(books.size() - 1);
        
        player.setItemInHand(hand, convert(tome, book));

        return ActionResultType.SUCCESS;
    }
    
    @Override
    public ActionResult<ItemStack> use(World level, PlayerEntity player, Hand hand) {
        ItemStack tome = player.getItemInHand(hand);

        if (level.isClientSide) EccentricTome.PROXY.tomeScreen(tome);

        return ActionResult.sidedSuccess(tome, level.isClientSide);
    }

    @Override
    public void appendHoverText(ItemStack tome, World level, List<ITextComponent> tooltip, ITooltipFlag advanced) {
        Map<String, List<ItemStack>> modsBooks = Tag.getModsBooks(tome);
        
        for (String mod : modsBooks.keySet()) {
            tooltip.add(new StringTextComponent(Mod.name(mod)));
            List<ItemStack> books = modsBooks.get(mod);
            for (ItemStack book : books) {
                if (book.getItem() == Items.AIR) continue;
                String name = book.getHoverName().getString();
                tooltip.add(new StringTextComponent("  " + TextFormatting.GRAY + name));
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
        IFormattableTextComponent bookName = new StringTextComponent(name).setStyle(Style.EMPTY.applyFormats(TextFormatting.GREEN));
        book.setHoverName(new TranslationTextComponent("eccentrictome.name", bookName));
    }

}
