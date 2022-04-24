package website.eccentric.tome;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.SimpleRecipeSerializer;
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
import net.minecraftforge.forge.event.lifecycle.GatherDataEvent;
import net.minecraftforge.network.simple.SimpleChannel;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import website.eccentric.tome.client.gui.RenderGameOverlayHandler;
import website.eccentric.tome.events.OpenTomeEvent;
import website.eccentric.tome.network.RevertMessage;
import website.eccentric.tome.network.TomeChannel;
import website.eccentric.tome.services.Configuration;
import website.eccentric.tome.services.Services;
import website.eccentric.tome.services.Tome;

@Mod(EccentricTome.MODID)
public class EccentricTome {
    
    public static final String MODID = "eccentrictome";
    public static final Logger LOGGER = LogManager.getLogger(MODID);

    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, MODID);
    public static final DeferredRegister<RecipeSerializer<?>> RECIPES = DeferredRegister.create(ForgeRegistries.RECIPE_SERIALIZERS, MODID);

    public static final RegistryObject<RecipeSerializer<?>> ATTACHMENT = RECIPES.register("attachment", () -> new SimpleRecipeSerializer<>(AttachmentRecipe::new));
    public static final RegistryObject<Item> TOME = ITEMS.register("tome", TomeItem::new);

    public static SimpleChannel CHANNEL;

    public EccentricTome() {
        var modEvent = FMLJavaModLoadingContext.get().getModEventBus();

        ITEMS.register(modEvent);
        RECIPES.register(modEvent);

        modEvent.addListener(this::onClientSetup);
        modEvent.addListener(this::onCommonSetup);
        modEvent.addListener(this::onGatherData);
        modEvent.addListener(this::onModConfig);

        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, CommonConfiguration.SPEC);

        var minecraftEvent = MinecraftForge.EVENT_BUS;
        minecraftEvent.addListener(this::onPlayerLeftClick);
        minecraftEvent.addListener(EventPriority.LOW, this::onItemDropped);
    }

    private void onClientSetup(final FMLClientSetupEvent event) {
        MinecraftForge.EVENT_BUS.addListener(EventPriority.LOW, RenderGameOverlayHandler::onRender);
        MinecraftForge.EVENT_BUS.addListener((OpenTomeEvent open) -> {
            Minecraft.getInstance().setScreen(new TomeScreen(open.tome));
        });
    }

    private void onCommonSetup(final FMLCommonSetupEvent event) {
        CHANNEL = TomeChannel.register();
    }

    private void onGatherData(GatherDataEvent event) {
        var generator = event.getGenerator();
        generator.addProvider(new TomeRecipe(generator));
    }

    private void onModConfig(ModConfigEvent event) {
        Services.load(Configuration.class).refresh();
    }

    private void onPlayerLeftClick(PlayerInteractEvent.LeftClickEmpty event) {
        var stack = event.getItemStack();
        if (TomeItem.isTome(stack) && !(stack.getItem() instanceof TomeItem)) {
            CHANNEL.sendToServer(new RevertMessage());
        }
    }

    private void onItemDropped(ItemTossEvent event) {
        if (!event.getPlayer().isShiftKeyDown()) return;

        var entity = event.getEntityItem();
        var stack = entity.getItem();

        if (TomeItem.isTome(stack) && !(stack.getItem() instanceof TomeItem)) {
            var detatchment = Services.load(Tome.class).revert(stack);
            var level = entity.getCommandSenderWorld();

            if (!level.isClientSide) {
                level.addFreshEntity(new ItemEntity(level, entity.getX(), entity.getY(), entity.getZ(), detatchment));
            }

            entity.setItem(stack);
        }
    }
}
