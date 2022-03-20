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

    public static final String TAG_TRANSFORM = "eccentric:tome:is_transforming";
    public static final String TAG_DATA = "eccentric:tome:data";
    public static final String TAG_NAME = "eccentric:tome:name";
    public static final String TAG_MOD = "eccentric:tome:mod";

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

        if (player.isShiftKeyDown()) {
            var mod = GetMod.fromState(level.getBlockState(position));
            var newStack = GetMod.shiftStack(stack, mod);

            if (!ItemStack.isSame(newStack, stack)) {
                player.setItemInHand(hand, newStack);
                return InteractionResult.SUCCESS;
            }
        }

        return InteractionResult.PASS;
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

            var mod = GetMod.fromStack(modStack);
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

		return stack.hasTag() && stack.getTag().getBoolean(TAG_TRANSFORM);
	}    

    public static ItemStack makeMorphedStack(ItemStack stack, String targetMod, CompoundTag data) {
		var mod = GetMod.fromStack(stack);

		var tag = new CompoundTag();
		stack.save(tag);
		tag = tag.copy();
		if (tag.contains("tag")) {
			tag.getCompound("tag").remove(TomeItem.TAG_DATA);
        }

		if (!mod.equalsIgnoreCase(GetMod.MINECRAFT) && !mod.equalsIgnoreCase(EccentricTome.MOD_ID)){
            data.put(mod, tag);
        }

		ItemStack targetStack;
		if (targetMod.equals(GetMod.MINECRAFT)) {
			targetStack = new ItemStack(new TomeItem());
        }
		else {
			var targetTag = data.getCompound(targetMod);
			data.remove(targetMod);

			targetStack = ItemStack.of(targetTag);
			if (targetStack.isEmpty()) {
				targetStack = new ItemStack(new TomeItem());
            }
		}

		if (!targetStack.hasTag()) {
			targetStack.setTag(new CompoundTag());
        }

		var stackTag = targetStack.getTag();
		stackTag.put(TomeItem.TAG_DATA, data);
		stackTag.putBoolean(TomeItem.TAG_TRANSFORM, true);

		if (targetStack.getItem() instanceof TomeItem) {
			var displayName = new CompoundTag();
			displayName.putString("text", targetStack.getHoverName().getString());

			if (stackTag.contains(TomeItem.TAG_NAME)) {
				displayName = (CompoundTag) stackTag.get(TomeItem.TAG_NAME);
            }
			else {
				stackTag.put(TomeItem.TAG_NAME, displayName);
            }

			var stackName = new TextComponent(displayName.getString("text")).setStyle(Style.EMPTY.applyFormats(ChatFormatting.GREEN));
			var component = new TranslatableComponent("tome.name", stackName);
			targetStack.setHoverName(component);
		}

		targetStack.setCount(1);
		return targetStack;
	}

}
