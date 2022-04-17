package website.eccentric.tome;

import java.util.function.Function;

import com.google.inject.Guice;
import com.google.inject.Injector;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.item.ItemTossEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.fml.DistExecutor;
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
import website.eccentric.tome.network.TomeChannel;
import website.eccentric.tome.proxy.ClientProxy;
import website.eccentric.tome.proxy.Proxy;

@Mod(EccentricTome.MODID)
public class EccentricTome {
    private Injector _injector;

    public static final String MODID = "eccentrictome";
    public static final Logger LOGGER = LogManager.getLogger(MODID);

    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, MODID);
    public static final DeferredRegister<RecipeSerializer<?>> RECIPES = DeferredRegister.create(ForgeRegistries.RECIPE_SERIALIZERS, MODID);


    public static Proxy PROXY;
    public static SimpleChannel CHANNEL;
    public static RegistryObject<RecipeSerializer<?>> ATTACHMENT;
    public static RegistryObject<Item> TOME;

    public EccentricTome() {
        _injector = Guice.createInjector(
            new CommonModule(),
            new ForgeModule()
        );

        ATTACHMENT = RECIPES.register("attachment", () -> new AttachmentSerializer(superJank(_injector)));
        TOME =  ITEMS.register("tome", () -> _injector.getInstance(TomeItem.class));

        var modEvent = FMLJavaModLoadingContext.get().getModEventBus();

        ITEMS.register(modEvent);
        RECIPES.register(modEvent);

        modEvent.addListener(this::onClientSetup);
        modEvent.addListener(this::onCommonSetup);
        modEvent.addListener(this::onGatherData);
        modEvent.addListener(this::onModConfig);

        PROXY = DistExecutor.unsafeRunForDist(() -> ClientProxy::new, () -> Proxy::new);

        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, CommonConfiguration.SPEC);

        var minecraftEvent = MinecraftForge.EVENT_BUS;
        minecraftEvent.addListener(this::onPlayerLeftClick);
        minecraftEvent.addListener(EventPriority.LOW, this::onItemDropped);
    }

    private void onClientSetup(final FMLClientSetupEvent event) {
        MinecraftForge.EVENT_BUS.addListener(EventPriority.LOW, _injector.getInstance(RenderGameOverlayHandler.class)::onRender);
    }

    private void onCommonSetup(final FMLCommonSetupEvent event) {
        CHANNEL = TomeChannel.register();
    }

    private void onGatherData(GatherDataEvent event) {
        var generator = event.getGenerator();
        var recipe = new TomeRecipe(generator);
        _injector.injectMembers(generator);
        generator.addProvider(recipe);
    }

    private void onModConfig(ModConfigEvent event) {
        // CONFIGURATION.Refresh();
    }

    private void onPlayerLeftClick(PlayerInteractEvent.LeftClickEmpty event) {
        var stack = event.getItemStack();
        if (TomeItem.isTome(stack) && !(stack.getItem() instanceof TomeItem)) {
            //CHANNEL.sendToServer(new RevertMessage());
        }
    }

    private void onItemDropped(ItemTossEvent event) {
        if (!event.getPlayer().isShiftKeyDown()) return;

        var entity = event.getEntityItem();
        var stack = entity.getItem();
        var level = entity.getCommandSenderWorld();

        if (TomeItem.isTome(stack) && !(stack.getItem() instanceof TomeItem)) {
            var detatchment = _injector.getInstance(TomeItem.class).revert(stack);

            if (!level.isClientSide) {
                level.addFreshEntity(new ItemEntity(level, entity.getX(), entity.getY(), entity.getZ(), detatchment));
            }

            entity.setItem(stack);
        }
    }

    private Function<ResourceLocation, AttachmentRecipe> superJank(Injector injector) {
        return (ResourceLocation location) -> {
            var recipe = new AttachmentRecipe(location);
            injector.injectMembers(recipe);
            return recipe;
        };
    }
}
