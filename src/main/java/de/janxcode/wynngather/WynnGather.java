package de.janxcode.wynngather;

import de.janxcode.wynngather.commands.ToggleCommand;
import de.janxcode.wynngather.inforenderer.DrawInfoPanel;
import de.janxcode.wynngather.inforenderer.Node;
import de.janxcode.wynngather.utils.SessionLogin;
import net.minecraftforge.client.ClientCommandHandler;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;

import java.util.LinkedList;
import java.util.List;

@Mod(modid = de.janxcode.wynngather.WynnGather.MODID, name = de.janxcode.wynngather.WynnGather.NAME, version = de.janxcode.wynngather.WynnGather.VERSION)
public class WynnGather
{
    public static final String MODID = "wynngather";
    public static final String NAME = "Wynn Gather";
    public static final String VERSION = "1.0";
    public static final List<Node> NODES = new LinkedList<>();
    public static String mineProgress = "";

    @EventHandler
    public void init(FMLInitializationEvent event)
    {
        SessionLogin.setNewSession();
        ClientCommandHandler.instance.registerCommand(new ToggleCommand());
    }
}
