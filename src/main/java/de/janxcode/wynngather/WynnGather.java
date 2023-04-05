package de.janxcode.wynngather;

import de.janxcode.wynngather.gui.InfoLineGui;
import de.janxcode.wynngather.gui.ModMenuGui;
import de.janxcode.wynngather.handlers.ArmorStandNodeRegister;
import de.janxcode.wynngather.inforenderer.DrawInfoPanel;
import de.janxcode.wynngather.inforenderer.DrawNodeInfo;
import de.janxcode.wynngather.inforenderer.Info;
import de.janxcode.wynngather.interfaces.INodeRegister;
import de.janxcode.wynngather.utils.DelayedTaskFrames;
import net.minecraft.client.Minecraft;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.client.ClientCommandHandler;
import net.minecraftforge.client.IClientCommand;
import net.minecraftforge.common.MinecraftForge;
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
//  - Rewrite in Kotlin
//  - Rewrite in Scratch (for performance)
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
    }


    private final Info info = Info.getInstance();
    private final DrawInfoPanel panel = DrawInfoPanel.getInstance();
    private final INodeRegister nodeRegister = ArmorStandNodeRegister.getInstance();
    private final DrawNodeInfo renderNodeInfo = new DrawNodeInfo(nodeRegister);
    private boolean modActive = false;

    private void toggle() {
        if (modActive) unregister();
        else register();

        modActive = !modActive;
    }

    private void unregister() {
        MinecraftForge.EVENT_BUS.unregister(nodeRegister);
        MinecraftForge.EVENT_BUS.unregister(panel);
        MinecraftForge.EVENT_BUS.unregister(renderNodeInfo);

        info.unregister();
    }

    private void register() {
        MinecraftForge.EVENT_BUS.register(nodeRegister);
        MinecraftForge.EVENT_BUS.register(panel);
        MinecraftForge.EVENT_BUS.register(renderNodeInfo);


        panel.update();
        info.register();
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

        @Override
        public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
            if (args.length == 0) {
                toggle();
                return;
            }

            switch (args[0]) {
                case "start":
                    if (modActive) throw new CommandException("Mod already active");
                    register();
                    break;

                case "stop":
                    if (!modActive) throw new CommandException("Mod not active");
                    unregister();
                    break;

                case "pos":
                    new DelayedTaskFrames((() -> Minecraft.getMinecraft().displayGuiScreen(new ModMenuGui())), 1);
                    break;

                case "menu":
                    new DelayedTaskFrames((() -> Minecraft.getMinecraft().displayGuiScreen(new InfoLineGui())), 1);
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
