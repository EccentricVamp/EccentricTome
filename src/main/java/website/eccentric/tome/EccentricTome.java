package website.eccentric.tome;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.SimpleCraftingRecipeSerializer;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.item.ItemTossEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.config.ModConfigEvent;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.network.simple.SimpleChannel;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import website.eccentric.tome.client.RenderGuiOverlayHandler;
import website.eccentric.tome.client.TomeHandler;
import website.eccentric.tome.network.RevertMessage;
import website.eccentric.tome.network.TomeChannel;

@Mod(EccentricTome.ID)
public class EccentricTome {
    public static final String ID = "eccentrictome";
    public static final Logger LOGGER = LogManager.getLogger(ID);

    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, ID);
    public static final DeferredRegister<RecipeSerializer<?>> RECIPES = DeferredRegister
            .create(ForgeRegistries.RECIPE_SERIALIZERS, ID);

    public static final RegistryObject<RecipeSerializer<?>> ATTACHMENT = RECIPES.register("attachment",
            () -> new SimpleCraftingRecipeSerializer<>(AttachmentRecipe::new));
    public static final RegistryObject<Item> TOME = ITEMS.register("tome", TomeItem::new);

    public static SimpleChannel CHANNEL;

    public EccentricTome() {
        var modEvent = FMLJavaModLoadingContext.get().getModEventBus();

        ITEMS.register(modEvent);
        RECIPES.register(modEvent);

        modEvent.addListener(this::onClientSetup);
        modEvent.addListener(this::onCommonSetup);
        modEvent.addListener(this::onModConfig);

        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, Configuration.SPEC);

        var minecraftEvent = MinecraftForge.EVENT_BUS;
        minecraftEvent.addListener(EventPriority.LOW, this::onItemDropped);
    }

    private void onClientSetup(final FMLClientSetupEvent event) {
        var minecraftEvent = MinecraftForge.EVENT_BUS;
        minecraftEvent.addListener(this::onLeftClickEmpty);
        minecraftEvent.addListener(EventPriority.LOW, RenderGuiOverlayHandler::onRender);
        minecraftEvent.addListener(TomeHandler::onOpenTome);
    }

    private void onCommonSetup(final FMLCommonSetupEvent event) {
        CHANNEL = TomeChannel.register();
    }

    private void onModConfig(ModConfigEvent event) {
        Configuration.ALIAS_MAP.clear();
        for (var alias : Configuration.ALIASES.get()) {
            var tokens = alias.split("=");
            Configuration.ALIAS_MAP.put(tokens[0], tokens[1]);
        }
    }

    private void onLeftClickEmpty(PlayerInteractEvent.LeftClickEmpty event) {
        var stack = event.getItemStack();
        if (Tome.isTome(stack) && !(stack.getItem() instanceof TomeItem)) {
            CHANNEL.sendToServer(new RevertMessage());
        }
    }

    private void onItemDropped(ItemTossEvent event) {
        if (!event.getPlayer().isShiftKeyDown())
            return;

        var entity = event.getEntity();
        var stack = entity.getItem();

        if (Tome.isTome(stack) && !(stack.getItem() instanceof TomeItem)) {
            var detatchment = Tome.revert(stack);
            var level = entity.getCommandSenderWorld();

            if (!level.isClientSide) {
                level.addFreshEntity(new ItemEntity(level, entity.getX(), entity.getY(), entity.getZ(), detatchment));
            }

            entity.setItem(stack);
        }
    }
}
