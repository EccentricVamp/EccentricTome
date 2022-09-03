package website.eccentric.tome;

import java.util.List;
import java.util.Map;

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
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import website.eccentric.tome.events.OpenTomeEvent;

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
        String mod = ModName.from(level.getBlockState(position));
        Map<String, List<ItemStack>> modsBooks = Tome.getModsBooks(tome);

        if (!player.isShiftKeyDown() || !modsBooks.containsKey(mod)) return ActionResultType.PASS;

        List<ItemStack> books = modsBooks.get(mod);
        ItemStack book = books.get(books.size() - 1);
        
        player.setItemInHand(hand, Tome.convert(tome, book));

        return ActionResultType.SUCCESS;
    }
    
    @Override
    public ActionResult<ItemStack> use(World level, PlayerEntity player, Hand hand) {
        ItemStack tome = player.getItemInHand(hand);

        if (level.isClientSide) MinecraftForge.EVENT_BUS.post(new OpenTomeEvent(tome));

        return ActionResult.sidedSuccess(tome, level.isClientSide);
    }

    @Override
    public void appendHoverText(ItemStack tome, World level, List<ITextComponent> tooltip, ITooltipFlag advanced) {
        Map<String, List<ItemStack>> modsBooks = Tome.getModsBooks(tome);
        
        for (String mod : modsBooks.keySet()) {
            tooltip.add(new StringTextComponent(ModName.name(mod)));
            List<ItemStack> books = modsBooks.get(mod);
            
            for (ItemStack book : books) {
                if (book.getItem() == Items.AIR) continue;
                String name = book.getHoverName().getString();
                tooltip.add(new StringTextComponent("  " + TextFormatting.GRAY + name));
            }
        }
    }
}
