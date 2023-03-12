package de.janxcode.wynngather.commands;

import de.janxcode.wynngather.WynnGather;
import de.janxcode.wynngather.handlers.ArmourStandHandler;
import de.janxcode.wynngather.inforenderer.DrawInfoPanel;
import de.janxcode.wynngather.inforenderer.DrawNodeInfo;
import de.janxcode.wynngather.inforenderer.Node;
import net.minecraftforge.common.MinecraftForge;

import java.util.List;

public class ToggleMod {
    private final DrawNodeInfo box = new DrawNodeInfo();
    private final ArmourStandHandler handler = new ArmourStandHandler();
    private final DrawInfoPanel info = DrawInfoPanel.getPanel();
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
        MinecraftForge.EVENT_BUS.unregister(info);
        nodes.clear();
    }

    public void register(){
        MinecraftForge.EVENT_BUS.register(box);
        MinecraftForge.EVENT_BUS.register(handler);
        MinecraftForge.EVENT_BUS.register(info);
        info.startTime = System.currentTimeMillis();
        nodes.clear();
    }
}
