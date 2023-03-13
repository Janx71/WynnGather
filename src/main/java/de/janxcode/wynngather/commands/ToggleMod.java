package de.janxcode.wynngather.commands;

import de.janxcode.wynngather.WynnGather;
import de.janxcode.wynngather.handlers.ArmourStandHandler;
import de.janxcode.wynngather.inforenderer.DrawInfoPanel;
import de.janxcode.wynngather.inforenderer.DrawNodeInfo;
import de.janxcode.wynngather.inforenderer.Info;
import de.janxcode.wynngather.inforenderer.Node;
import net.minecraftforge.common.MinecraftForge;

import java.util.List;

public class ToggleMod {
    private final Info info = Info.getInstance();

    private final DrawInfoPanel panel = DrawInfoPanel.getPanel();
    private final DrawNodeInfo box = new DrawNodeInfo();
    private final ArmourStandHandler handler = new ArmourStandHandler();
    private final List<Node> nodes = WynnGather.NODES;
    private boolean toggled = false;

    public void toggle(){
        if(toggled) unregister();
        else register();

        toggled = !toggled;
    }

    public void unregister(){
        MinecraftForge.EVENT_BUS.unregister(box);
        MinecraftForge.EVENT_BUS.unregister(handler);
        MinecraftForge.EVENT_BUS.unregister(panel);

        info.unregister();
        nodes.clear();
    }

    public void register(){
        MinecraftForge.EVENT_BUS.register(box);
        MinecraftForge.EVENT_BUS.register(handler);
        MinecraftForge.EVENT_BUS.register(panel);

        panel.update();
        info.register();
        nodes.clear();
    }
}
