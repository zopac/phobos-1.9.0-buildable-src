package me.earth.phobos;

import net.minecraftforge.fml.common.*;
import me.earth.phobos.features.gui.custom.*;
import me.earth.phobos.manager.*;
import net.minecraftforge.fml.common.event.*;
import org.apache.logging.log4j.Logger;
import org.lwjgl.opengl.*;
import me.earth.phobos.features.modules.misc.*;
import me.earth.phobos.features.modules.client.*;
import java.io.*;
import org.apache.logging.log4j.*;

@Mod(modid = "phobos", name = "Phobos", version = "1.9.0")
public class Phobos
{
    public static final String MODID = "phobos";
    public static final String MODNAME = "Phobos";
    public static final String MODVER = "1.9.0";
    public static final String NAME_UNICODE = "\u1d00\u0280\u1d1b\u029c\u029c\u1d04\u1d0b";
    public static final String PHOBOS_UNICODE = "\u1d18\u029c\u1d0f\u0299\u1d0f\ua731";
    public static final String CHAT_SUFFIX = " \u23d0 \u1d00\u0280\u1d1b\u029c\u029c\u1d04\u1d0b";
    public static final String PHOBOS_SUFFIX = " \u23d0 \u1d18\u029c\u1d0f\u0299\u1d0f\ua731";
    public static final Logger LOGGER;
    public static ModuleManager moduleManager;
    public static SpeedManager speedManager;
    public static PositionManager positionManager;
    public static RotationManager rotationManager;
    public static CommandManager commandManager;
    public static EventManager eventManager;
    public static ConfigManager configManager;
    public static FileManager fileManager;
    public static FriendManager friendManager;
    public static TextManager textManager;
    public static ColorManager colorManager;
    public static ServerManager serverManager;
    public static PotionManager potionManager;
    public static InventoryManager inventoryManager;
    public static TimerManager timerManager;
    public static PacketManager packetManager;
    public static ReloadManager reloadManager;
    public static TotemPopManager totemPopManager;
    public static HoleManager holeManager;
    public static NotificationManager notificationManager;
    public static SafetyManager safetyManager;
    public static GuiCustomMainScreen customMainScreen;
    public static CosmeticsManager cosmeticsManager;
    public static NoStopManager baritoneManager;
    public static WaypointManager waypointManager;
    private static boolean unloaded;
    @Mod.Instance
    public static Phobos INSTANCE;
    
    @Mod.EventHandler
    public void preInit(final FMLPreInitializationEvent event) {
        Phobos.LOGGER.info("ohare is cute!!!");
        Phobos.LOGGER.info("faggot above - 3vt");
        Phobos.LOGGER.info("megyn wins again");
        Phobos.LOGGER.info("gtfo my logs - 3arth");
    }
    
    @Mod.EventHandler
    public void init(final FMLInitializationEvent event) {
        Phobos.customMainScreen = new GuiCustomMainScreen();
        Display.setTitle("3arthh4ck - v.1.9.0");
        load();
    }
    
    public static void load() {
        Phobos.LOGGER.info("\n\nLoading 3arthh4ck 1.9.0");
        Phobos.unloaded = false;
        if (Phobos.reloadManager != null) {
            Phobos.reloadManager.unload();
            Phobos.reloadManager = null;
        }
        Phobos.baritoneManager = new NoStopManager();
        Phobos.totemPopManager = new TotemPopManager();
        Phobos.timerManager = new TimerManager();
        Phobos.packetManager = new PacketManager();
        Phobos.serverManager = new ServerManager();
        Phobos.colorManager = new ColorManager();
        Phobos.textManager = new TextManager();
        Phobos.moduleManager = new ModuleManager();
        Phobos.speedManager = new SpeedManager();
        Phobos.rotationManager = new RotationManager();
        Phobos.positionManager = new PositionManager();
        Phobos.commandManager = new CommandManager();
        Phobos.eventManager = new EventManager();
        Phobos.configManager = new ConfigManager();
        Phobos.fileManager = new FileManager();
        Phobos.friendManager = new FriendManager();
        Phobos.potionManager = new PotionManager();
        Phobos.inventoryManager = new InventoryManager();
        Phobos.holeManager = new HoleManager();
        Phobos.notificationManager = new NotificationManager();
        Phobos.safetyManager = new SafetyManager();
        Phobos.waypointManager = new WaypointManager();
        Phobos.LOGGER.info("Initialized Managers");
        Phobos.moduleManager.init();
        Phobos.LOGGER.info("Modules loaded.");
        Phobos.configManager.init();
        Phobos.eventManager.init();
        Phobos.LOGGER.info("EventManager loaded.");
        Phobos.textManager.init(true);
        Phobos.moduleManager.onLoad();
        Phobos.totemPopManager.init();
        Phobos.timerManager.init();
        Phobos.cosmeticsManager = new CosmeticsManager();
        Phobos.LOGGER.info("3arthh4ck initialized!\n");
    }
    
    public static void unload(final boolean unload) {
        Phobos.LOGGER.info("\n\nUnloading 3arthh4ck 1.9.0");
        if (unload) {
            (Phobos.reloadManager = new ReloadManager()).init((Phobos.commandManager != null) ? Phobos.commandManager.getPrefix() : ".");
        }
        if (Phobos.baritoneManager != null) {
            Phobos.baritoneManager.stop();
        }
        onUnload();
        Phobos.eventManager = null;
        Phobos.holeManager = null;
        Phobos.timerManager = null;
        Phobos.moduleManager = null;
        Phobos.totemPopManager = null;
        Phobos.serverManager = null;
        Phobos.colorManager = null;
        Phobos.textManager = null;
        Phobos.speedManager = null;
        Phobos.rotationManager = null;
        Phobos.positionManager = null;
        Phobos.commandManager = null;
        Phobos.configManager = null;
        Phobos.fileManager = null;
        Phobos.friendManager = null;
        Phobos.potionManager = null;
        Phobos.inventoryManager = null;
        Phobos.notificationManager = null;
        Phobos.safetyManager = null;
        Phobos.LOGGER.info("3arthh4ck unloaded!\n");
    }
    
    public static void reload() {
        unload(false);
        load();
    }
    
    public static void onUnload() {
        if (!Phobos.unloaded) {
            Phobos.eventManager.onUnload();
            Phobos.moduleManager.onUnload();
            Phobos.configManager.saveConfig(Phobos.configManager.config.replaceFirst("phobos/", ""));
            Phobos.moduleManager.onUnloadPost();
            Phobos.timerManager.unload();
            Phobos.unloaded = true;
        }
    }
    
    static {
        LOGGER = LogManager.getLogger("3arthh4ck");
        Phobos.unloaded = false;
    }
}
