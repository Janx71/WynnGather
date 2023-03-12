package de.janxcode.wynngather.inforenderer;

import de.janxcode.wynngather.handlers.NodeMinedEvent;
import de.janxcode.wynngather.utils.ModConfig;
import de.janxcode.wynngather.utils.RenderUtils;
import de.janxcode.wynngather.utils.Utils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public  class DrawInfoPanel {
    private static final DrawInfoPanel panel = new DrawInfoPanel();
    private final Minecraft mc = Minecraft.getMinecraft();
    FontRenderer renderer = mc.fontRenderer;
    public long startTime;
    public int nodesMined;
    private int nextLevel;
    private int xp;
    private String type = "" + TextFormatting.RED + TextFormatting.BOLD + "Not Set";
    private String progress = "";

    //Private Constructor for use in Singleton Pattern
    private DrawInfoPanel(){
        startTime = System.currentTimeMillis();
        nodesMined = 0;
        nextLevel = 0;
        xp = 0;
    }

    public static DrawInfoPanel getPanel(){
        return panel;
    }

    public void setProgress(String progress){
        this.progress = progress;
    }

    @SubscribeEvent
    public void onNode(NodeMinedEvent e){
        nodesMined++;
        nextLevel = e.next;
        xp += e.xp;
        type = e.node.getType();
    }

    @SubscribeEvent
    public void onRender(RenderGameOverlayEvent.Text e){
        long seconds = (System.currentTimeMillis() - startTime) / 1000;

        //Help its 5am
        long nodesPerMinute = 0;
        long xpPerSecond = 0;
        if(seconds / 60d > 0){
            nodesPerMinute = (long) (nodesMined / (seconds / 60d));
            xpPerSecond = (long) (xp / (seconds / 3600d));
        }

        //haven't slept the whole night ):
        String[] text = {
                "Resource Type: " + ModConfig.highlight + TextFormatting.BOLD + type,
                "Time spent: " + ModConfig.highlight + TextFormatting.BOLD + String.format("%02d:%02d:%02d", seconds / 3600, (seconds % 3600) / 60, seconds % 60),
                "Nodes Mined: " + ModConfig.highlight + TextFormatting.BOLD + nodesMined,
                "Nodes Per Minute: " + ModConfig.highlight + TextFormatting.BOLD + nodesPerMinute,
                "Percentage to next Level: " + ModConfig.highlight + TextFormatting.BOLD + nextLevel + "%",
                "XP per Hour: " + ModConfig.highlight + TextFormatting.BOLD + Utils.formatValue(xpPerSecond),
                progress
        };

        RenderUtils.drawLayeredString(text, ModConfig.guiPosX, ModConfig.guiPosY, ModConfig.TextColor, renderer);
    }
}
