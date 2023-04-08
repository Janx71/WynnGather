package de.janxcode.wynngather;

import de.janxcode.wynngather.features.IFeature;
import de.janxcode.wynngather.features.nodeblockoverlay.NodeBlockOverlayFeature;
import de.janxcode.wynngather.features.statsoverlay.StatsOverlayConfigGui;
import de.janxcode.wynngather.core.ModMenuGui;
import de.janxcode.wynngather.core.ArmorStandNodeRegistry;
import de.janxcode.wynngather.core.interfaces.IGlobalNodeRegistry;
import de.janxcode.wynngather.features.statsoverlay.StatsOverlayFeature;
import de.janxcode.wynngather.utils.DelayedTaskFrames;
import net.minecraft.client.Minecraft;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.client.ClientCommandHandler;
import net.minecraftforge.client.IClientCommand;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

// todo:
//  - The gathering stats overlay and node boxes are seperate features, they should be fully seperated
//      - fully seperate features into seperate modules
//      - seperate mod into core and features
//      - use interfaces around core classes to allow dependency injection
//  - This mod has guis in the form of screens, ingame overlays and block overlays. A clear difference should be established in the naming scheme
//  - Wrapper around Forge events with better active/invactive handling
//  - Gathering Sessions
//  - Rewrite in Kotlin
//  - Rewrite in Scratch (for performance)
//  - keep a permanent record of gathering, just dynamically set the current session's stats based on user input and config
//     e.g. (optionally) start session on node mined, end session after config idle time; start, end, reset on command
//  - make timed averages more stable (also use interfaces here so that averaging implementations can be swapped easily)
@Mod(modid = de.janxcode.wynngather.WynnGather.MODID, name = de.janxcode.wynngather.WynnGather.NAME, version = de.janxcode.wynngather.WynnGather.VERSION)
public class WynnGather {
    // logger, use this instead of System.out.println
    public static Logger logger = LogManager.getLogger(WynnGather.MODID);

    public static final String MODID = "wynngather";
    public static final String NAME = "Wynn Gather";
    public static final String VERSION = "1.0";
    public static boolean GUI = false;  // todo: use mc.currentScreen instead

    @EventHandler
    public void init(FMLInitializationEvent event) {
        ClientCommandHandler.instance.registerCommand(new ModCommand());
        nodeRegistry.register();
    }

    private static final ArmorStandNodeRegistry nodeRegistry = new ArmorStandNodeRegistry();

    public static IGlobalNodeRegistry getGlobalNodeRegistry() {
        return nodeRegistry;
    }

    private boolean modActive = false;
    private final IFeature[] features = {
            new NodeBlockOverlayFeature(),
            new StatsOverlayFeature()
    };

    private void toggle() {
        if (modActive) stopMod();
        else startMod();
    }

    private void startMod() {  // todo: allow features to be enabled/disabled individually
        assert !modActive;
        for (IFeature f : features) {
            f.activate();
        }
        modActive = true;
    }

    private void stopMod() {
        assert modActive;
        for (IFeature f : features) {
            f.deactivate();
        }
        modActive = false;
    }

    // this would be much cleaner with an object declaration in Kotlin
    class ModCommand extends CommandBase implements IClientCommand {
        @Override
        public String getName() {
            return "gather";
        }

        @Override
        public int getRequiredPermissionLevel() {
            return 0;
        }

        @Override
        public String getUsage(ICommandSender sender) {
            return "Usage:\n/gather\n/gather start|stop|pos|menu";
        }

        private static final String MOD_ALREADY_ACTIVE = "Mod already active";
        private static final String MOD_NOT_ACTIVE = "Mod not active";

        @Override
        public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
            if (args.length == 0) {
                toggle();
                return;
            }

            switch (args[0]) {
                case "start":
                    if (modActive) throw new CommandException(MOD_ALREADY_ACTIVE);
                    startMod();
                    break;

                case "stop":
                    if (!modActive) throw new CommandException(MOD_NOT_ACTIVE);
                    stopMod();
                    break;

                case "pos":
                    new DelayedTaskFrames((() -> Minecraft.getMinecraft().displayGuiScreen(new ModMenuGui())), 1);
                    break;

                case "menu":
                    new DelayedTaskFrames((() -> Minecraft.getMinecraft().displayGuiScreen(new StatsOverlayConfigGui())), 1);
                    break;

                default:
                    throw new CommandException(getUsage(sender));
            }
        }


        @Override
        public boolean allowUsageWithoutPrefix(ICommandSender sender, String message) {
            return false;
        }
    }

    @Mod.Instance
    public static WynnGather instance;

    public WynnGather() {
        if (instance != null) throw new IllegalStateException("Instance already exists");
    }
}
