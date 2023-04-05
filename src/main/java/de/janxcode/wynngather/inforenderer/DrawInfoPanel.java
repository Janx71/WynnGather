package de.janxcode.wynngather.inforenderer;

import de.janxcode.wynngather.WynnGather;
import de.janxcode.wynngather.utils.ModConfig;
import de.janxcode.wynngather.utils.RenderUtils;
import net.minecraft.client.Minecraft;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.awt.*;
import java.util.Arrays;

public class DrawInfoPanel {
    // todo: this should be a non-singleton class called GatherStatsOverlay or similar
    private InfoLine[] infoLines;
    Minecraft mc = Minecraft.getMinecraft();
    private static DrawInfoPanel instance;

    private DrawInfoPanel() {
        if (instance != null) throw new IllegalStateException("Instance already exists");
    }

    public static DrawInfoPanel getInstance() {
        if (instance == null) {
            instance = new DrawInfoPanel();
        }

        return instance;
    }

    public void update() {
        infoLines = Arrays.stream(ModConfig.infoLines)
                .map(InfoLine::new).toArray(InfoLine[]::new);
    }

    @SubscribeEvent
    public void onRender(RenderGameOverlayEvent.Text e) {

        if (WynnGather.GUI) return;

        String[] lines = Arrays.stream(infoLines).map(InfoLine::getOutputLine).toArray(String[]::new);

        RenderUtils.drawLayeredString(lines, ModConfig.guiPosX, ModConfig.guiPosY, Color.WHITE.getRGB(), mc.fontRenderer);
    }
}
