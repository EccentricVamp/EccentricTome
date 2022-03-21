package website.eccentric.tome;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.annotation.Nullable;

import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.CompoundTag;
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

    public static final String TAG_DATA = "eccentrictome:data";
    public static final String TAG_MOD = "eccentrictome:mod";
    public static final String TAG_NAME = "eccentrictome:name";
    public static final String TAG_CONVERTED = "eccentrictome:is_converted";

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

        if (!player.isShiftKeyDown()) return InteractionResult.PASS;
        
        var mod = GetMod.from(level.getBlockState(position));
        var data = stack.getTag().getCompound(TAG_DATA);

        if (!data.contains(mod)) return InteractionResult.PASS;

        var newStack = convert(stack, mod);

        if (ItemStack.isSame(newStack, stack)) return InteractionResult.PASS;
        
        player.setItemInHand(hand, newStack);
        return InteractionResult.SUCCESS;
    }
    
    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
		var stack = player.getItemInHand(hand);
        if (level.isClientSide) Minecraft.getInstance().setScreen(new TomeScreen(stack));
		return InteractionResultHolder.sidedSuccess(stack, level.isClientSide);
    }

	@Override
	public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltip, TooltipFlag advanced) {
		if (!stack.hasTag() || !stack.getTag().contains(TAG_DATA)) return;

		var data = stack.getTag().getCompound(TAG_DATA);
		if (data.getAllKeys().isEmpty()) return;

        var keys = new ArrayList<String>(data.getAllKeys());
        Collections.sort(keys);
        var currentMod = "";

        for (var key : keys) {
            var tag = data.getCompound(key);
            if (tag == null) continue;
            
            var modStack = ItemStack.of(tag);
            if (modStack.isEmpty()) continue;
            
            var name = modStack.getHoverName().getString();
            if (modStack.hasTag() && modStack.getTag().contains(TAG_NAME)) {
                name = modStack.getTag().getCompound(TAG_NAME).getString("text");
            }

            var mod = GetMod.from(modStack);
            if (!currentMod.equals(mod)) {
                tooltip.add(new TextComponent(GetMod.name(mod)).setStyle(Style.EMPTY.applyFormats(ChatFormatting.AQUA)));
            }
            
            tooltip.add(new TextComponent(" \u2520 " + name));

            currentMod = mod;
        }
	}

	public static boolean isTome(ItemStack stack) {
		if (stack.isEmpty()) return false;

		if (stack.getItem() instanceof TomeItem) return true;

		return stack.hasTag() && stack.getTag().getBoolean(TAG_CONVERTED);
	}    

    public static ItemStack convert(ItemStack stack, String mod) {
		var data = stack.getTag().getCompound(TAG_DATA);

        var converted = ItemStack.of(data.getCompound(mod));
        var convertedName = converted.getHoverName().getString();
		if (!converted.hasTag()) converted.setTag(new CompoundTag());
        var tag = converted.getTag();

        data.remove(mod);
		tag.put(TAG_DATA, data);
        tag.putString(TAG_MOD, mod);
        tag.putString(TAG_NAME, convertedName);
		tag.putBoolean(TAG_CONVERTED, true);

        var hoverName = new TextComponent(convertedName).setStyle(Style.EMPTY.applyFormats(ChatFormatting.GREEN));
        converted.setHoverName(new TranslatableComponent("eccentrictome.name", hoverName));
        
		return converted;
	}

    public static ItemStack revert(ItemStack stack) {
        var tag = stack.getTag();
        var data = tag.getCompound(TAG_DATA);
        
        var tomeTag = new CompoundTag();
        tomeTag.put(TAG_DATA, data);
        var tome = new ItemStack(EccentricTome.TOME.get());
        tome.setTag(tomeTag);

        tag.remove(TAG_DATA);
        tag.remove(TAG_MOD);
        tag.remove(TAG_NAME);
        tag.remove(TAG_CONVERTED);

        stack.resetHoverName();

        return attach(tome, stack);
    }

    public static ItemStack detatch(ItemStack stack) {
        var mod = stack.getTag().getString(TAG_MOD);

        var reverted = revert(stack);
        var data = reverted.getTag().getCompound(TomeItem.TAG_DATA);
        data.remove(mod);
        
        return reverted;
    }

    public static ItemStack attach(ItemStack tome, ItemStack attachment) {
        if (!tome.hasTag()) tome.setTag(new CompoundTag());
		var tag = tome.getTag();

		if (!tag.contains(TAG_DATA)) tag.put(TAG_DATA, new CompoundTag());
		var data = tag.getCompound(TomeItem.TAG_DATA);

		var attachmentTag = new CompoundTag();
		attachment.save(attachmentTag);
		data.put(GetMod.from(attachment), attachmentTag);

        return tome;
    }

}
