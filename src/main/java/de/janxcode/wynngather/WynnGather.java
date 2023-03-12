package de.janxcode.wynngather;

import de.janxcode.wynngather.commands.ConfigCommand;
import de.janxcode.wynngather.commands.ToggleCommand;
import de.janxcode.wynngather.inforenderer.Node;
import net.minecraftforge.client.ClientCommandHandler;
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
    public static boolean GUI = false;

    @EventHandler
    public void init(FMLInitializationEvent event)
    {
        ClientCommandHandler.instance.registerCommand(new ToggleCommand());
        ClientCommandHandler.instance.registerCommand(new ConfigCommand());
    }
}
