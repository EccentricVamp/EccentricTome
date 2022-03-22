package website.eccentric.tome;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;

public class Tag {

    public static final String TAG_DATA = "eccentrictome:data";
    public static final String TAG_MOD = "eccentrictome:mod";
    public static final String TAG_NAME = "eccentrictome:name";
    public static final String TAG_CONVERTED = "eccentrictome:is_converted";

    public static boolean hasData(ItemStack stack) {
        return stack.hasTag() && stack.getTag().contains(TAG_DATA);
    }

    public static boolean hasData(ItemStack stack, String mod) {
        return hasData(stack) && getData(stack).contains(mod);
    }

    public static CompoundTag getData(ItemStack stack) {
        if (!stack.hasTag()) stack.setTag(new CompoundTag());
        var tag = stack.getTag();

        if (!tag.contains(TAG_DATA)) tag.put(TAG_DATA, new CompoundTag());
        return tag.getCompound(TAG_DATA);
    }

    public static CompoundTag getData(ItemStack stack, String mod) {
        return getData(stack).getCompound(mod);
    }

    public static void setData(ItemStack stack, CompoundTag data) {
        if (!stack.hasTag()) stack.setTag(new CompoundTag());
        stack.getTag().put(TAG_DATA, data);
    }

    public static void addData(ItemStack stack, ItemStack attachment) {
        if (!hasData(stack)) setData(stack, new CompoundTag());
        
        var data = getData(stack);
		data.put(GetMod.from(attachment), attachment.save(new CompoundTag()));
    }

    public static CompoundTag popData(ItemStack stack, String mod) {
        var tag = getData(stack, mod);
        var data = getData(stack);
        data.remove(mod);
        return tag;
    }

    public static void copyData(ItemStack source, ItemStack target) {
        var data = getData(source).copy();
        setData(target, data);
    }

    public static String getMod(ItemStack stack) {
        return stack.getTag().getString(TAG_MOD);
    }

    public static boolean isConverted(ItemStack stack) {
        return stack.hasTag() && stack.getTag().getBoolean(TAG_CONVERTED);
    }

    public static boolean hasName(ItemStack stack) {
        return stack.hasTag() && stack.getTag().contains(TAG_NAME);
    }

    public static void clear(ItemStack stack) {
        var tag = stack.getTag();

        tag.remove(TAG_DATA);
        tag.remove(TAG_MOD);
        tag.remove(TAG_NAME);
        tag.remove(TAG_CONVERTED);
        if (tag.isEmpty()) stack.setTag(null);
    }

    public static void fill(ItemStack stack, String mod, String name, boolean converted) {
        var tag = stack.getTag();

        tag.putString(Tag.TAG_MOD, mod);
        tag.putString(Tag.TAG_NAME, name);
		tag.putBoolean(Tag.TAG_CONVERTED, true);
    }

}
