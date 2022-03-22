package website.eccentric.tome;

import java.util.List;

import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
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
        var stack = player.getItemInHand(hand);
        var mod = Mod.from(level.getBlockState(position));

        if (!player.isShiftKeyDown() || !Tag.hasData(stack, mod)) return InteractionResult.PASS;
        
        player.setItemInHand(hand, convert(stack, mod));

        return InteractionResult.SUCCESS;
    }
    
    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
		var tome = player.getItemInHand(hand);
		if (Tag.getData(tome).isEmpty()) return InteractionResultHolder.fail(tome);

        if (level.isClientSide) {
            Minecraft.getInstance().setScreen(new TomeScreen(tome));
        }

		return InteractionResultHolder.sidedSuccess(tome, level.isClientSide);
    }

	@Override
	public void appendHoverText(ItemStack tome, Level level, List<Component> tooltip, TooltipFlag advanced) {
		var data = Tag.getData(tome);
        for (var key : data.getAllKeys()) {
            var stack = ItemStack.of(data.getCompound(key));            
            var name = stack.getHoverName().getString();
            var mod = Mod.from(stack);
            
            tooltip.add(new TextComponent(Mod.name(mod)));
            tooltip.add(new TextComponent("\t" + name));
        }
	}

	public static boolean isTome(ItemStack stack) {
		if (stack.isEmpty()) return false;
		else if (stack.getItem() instanceof TomeItem) return true;
        else return Tag.isConverted(stack);
	}    

    public static ItemStack convert(ItemStack tome, String mod) {
        var stack = ItemStack.of(Tag.popData(tome, mod));
        var name = stack.getHoverName().getString();
        Tag.copyData(tome, stack);
        Tag.fill(tome, mod, name, true);

        setHoverName(tome, name);
        
		return stack;
	}

    public static ItemStack revert(ItemStack stack) {
        var tome = createStack();
        Tag.copyData(stack, tome);
        Tag.clear(stack);

        stack.resetHoverName();

        return attach(tome, stack);
    }

    public static ItemStack detatch(ItemStack stack) {
        var mod = Tag.getMod(stack);
        var tome = revert(stack);
        Tag.popData(tome, mod);
        
        return tome;
    }

    public static ItemStack attach(ItemStack tome, ItemStack attachment) {
        Tag.addData(tome, attachment);
        return tome;
    }

    private static ItemStack createStack() {
        return new ItemStack(EccentricTome.TOME.get());
    }

    private static void setHoverName(ItemStack stack, String name) {
        var innerName = new TextComponent(name).setStyle(Style.EMPTY.applyFormats(ChatFormatting.GREEN));
        stack.setHoverName(new TranslatableComponent("eccentrictome.name", innerName));
    }

}
