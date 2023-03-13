package de.janxcode.wynngather.inforenderer;

import de.janxcode.wynngather.WynnGather;
import de.janxcode.wynngather.utils.ModConfig;
import de.janxcode.wynngather.utils.RenderUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.awt.*;
import java.util.Arrays;

public  class DrawInfoPanel {
    private InfoLine[] infoLines;
    FontRenderer renderer = Minecraft.getMinecraft().fontRenderer;

    private static DrawInfoPanel panel;

    private DrawInfoPanel(){}

    public static DrawInfoPanel getPanel(){
        if(panel == null){
            panel = new DrawInfoPanel();
        }

        return panel;
    }

    public void update(){
        infoLines = Arrays.stream(ModConfig.infoLines)
                .map(InfoLine::new)
                .toArray(InfoLine[]::new);
    }

    @SubscribeEvent
    public void onRender(RenderGameOverlayEvent.Text e){

        if(WynnGather.GUI) return;

        String[] lines = Arrays.stream(infoLines)
                .map(InfoLine::getOutputLine)
                .toArray(String[]::new);

        RenderUtils.drawLayeredString(lines, ModConfig.guiPosX, ModConfig.guiPosY, Color.WHITE.getRGB(), renderer);
    }
}
