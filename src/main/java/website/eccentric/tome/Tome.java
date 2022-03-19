package website.eccentric.tome;

import com.mojang.blaze3d.systems.RenderSystem;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.opengl.GL11;

import net.minecraft.client.Minecraft;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.SimpleRecipeSerializer;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.network.simple.SimpleChannel;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

@Mod(Tome.MOD_ID)
public class Tome {

	public static final String MOD_ID = "tome";
    public static final Logger LOGGER = LogManager.getLogger(MOD_ID);

    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, MOD_ID);
    public static final DeferredRegister<RecipeSerializer<?>> RECIPES = DeferredRegister.create(ForgeRegistries.RECIPE_SERIALIZERS, MOD_ID);

    public static final RegistryObject<RecipeSerializer<?>> ATTACHMENT = RECIPES.register("attachment", () -> new SimpleRecipeSerializer<>(AttachmentRecipe::new));

    public static final RegistryObject<Item> TOME = ITEMS.register("tome", TomeItem::new);

    public static SimpleChannel CHANNEL;

	public Tome() {
        var bus = FMLJavaModLoadingContext.get().getModEventBus();

        ITEMS.register(bus);
        RECIPES.register(bus);

        bus.addListener(this::onCommonSetup);
	}

    private void onCommonSetup(final FMLCommonSetupEvent event) {
        CHANNEL = Channel.register();
    }

	@SubscribeEvent
	public void onDrawScreen(RenderGameOverlayEvent.Post event) {
		if (event.getType() != ElementType.ALL) return;

		var minecraft = Minecraft.getInstance();

        if (minecraft.hitResult == null || !(minecraft.hitResult instanceof BlockHitResult)) return;
        var blockHit = (BlockHitResult) minecraft.hitResult;

		var window = event.getWindow();
        var stack = minecraft.player.getMainHandItem();

        var hasTome = !stack.isEmpty() && stack.getItem() instanceof TomeItem;
        if (!hasTome) {
            stack = minecraft.player.getOffhandItem();
            hasTome = !stack.isEmpty() && stack.getItem() instanceof TomeItem;
        }

        if (!hasTome) return;

        stack = stack.copy();

        var state = minecraft.level.getBlockState(blockHit.getBlockPos());
        if (!state.isAir()) return;

        var drawStack = ItemStack.EMPTY;
        var line1 = "";

        var mod = GetMod.fromState(state);
        var shiftStack = GetMod.shiftStack(stack, mod);
        
        if (!shiftStack.isEmpty() && !ItemStack.isSame(shiftStack, stack)) {
            drawStack = shiftStack;
            line1 = shiftStack.getTag().getCompound(TomeItem.TAG_NAME).getString("text");
        }

        if (!drawStack.isEmpty()) {
            RenderSystem.enableBlend();
            RenderSystem.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
            int sx = window.getGuiScaledWidth() / 2 - 17;
            int sy = window.getGuiScaledHeight() / 2 + 2;

            minecraft.getItemRenderer().renderGuiItem(drawStack, sx, sy);
            minecraft.font.drawShadow(event.getMatrixStack(), line1, sx + 20, sy + 4, 0xFFFFFFFF);
        }
	}

}
