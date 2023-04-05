package de.janxcode.wynngather.features.statsoverlay;

import de.janxcode.wynngather.WynnGather;
import de.janxcode.wynngather.core.NodeProgressUpdatedEvent;
import de.janxcode.wynngather.core.interfaces.IRegisterable;
import de.janxcode.wynngather.utils.ModConfig;
import de.janxcode.wynngather.utils.RenderUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.awt.*;
import java.util.Arrays;

public class DrawInfoPanel implements IRegisterable {
    // todo: this should be a non-singleton class called GatherStatsOverlay or similar
    private String[] infoLinePatterns;
    Minecraft mc = Minecraft.getMinecraft();
    private final StatsHelper stats = new StatsHelper();

    private String latestNodeProgress = "";

    @SubscribeEvent
    public void onNodeUpdate(NodeProgressUpdatedEvent e) {
        this.latestNodeProgress = e.node.getMiningProgress();
    }

    public void register() {
        stats.register();
        updatePatterns();
        IRegisterable.super.register();
    }

    public void unregister() {
        stats.unregister();
        IRegisterable.super.unregister();
    }

    public void updatePatterns() {
        infoLinePatterns = ModConfig.infoLines;
    }

    @SubscribeEvent
    public void onRender(RenderGameOverlayEvent.Text e) {

        if (WynnGather.GUI) return;

        RenderUtils.drawLayeredString(Arrays.stream(infoLinePatterns).map(this::getOutputLine), ModConfig.guiPosX, ModConfig.guiPosY, Color.WHITE.getRGB(), mc.fontRenderer);
    }

    @SubscribeEvent
    public void onConfigChange(ConfigChangedEvent.PostConfigChangedEvent e) {
        if (e.getModID().equals(WynnGather.MODID)) {
            updatePatterns();
        }
    }

    private String getOutputLine(String line) { // todo: refactor
        StringBuilder output = new StringBuilder();
        String remainingLine = line;
        int start;
        int end = 0;
        while ((start = remainingLine.indexOf('{', end)) != -1 && (end = remainingLine.indexOf('}', start)) != -1) {
            output.append(remainingLine, 0, start); // append everything before the opening brace
            String textInsideBraces = remainingLine.substring(start + 1, end);
            String newTextInsideBraces = getStatsValue(textInsideBraces);
            output.append(newTextInsideBraces); // append the modified text inside the braces
            remainingLine = remainingLine.substring(end + 1); // set the remainingLine to everything after the closing brace, excluding the closing brace itself
            end = 0; // reset the end variable to 0 so it starts searching for the next opening brace from the beginning of remainingLine
        }
        output.append(remainingLine); // append anything that comes after the last closing brace
        return output.toString();
    }

    private String getStatsValue(String v) {
        // todo: refactor with enum
        switch (v) {
            case "nodesMined":
                return String.valueOf(stats.getNodesMined());

            case "nodesPerMinute":
                return String.valueOf(stats.getNodesPerMinute());

            case "xpPerHour":
                return String.valueOf(stats.getXpPerHour());

            case "nextLevel":
                return String.valueOf(stats.getNextLevel());

            case "type":
                return stats.getType();

            case "time":
                return stats.getTime();

            case "progress":
                return latestNodeProgress;

        }

        for (TextFormatting value : TextFormatting.values()) {
            try {
                TextFormatting formatting = TextFormatting.valueOf(v);
                if (value.equals(formatting)) {
                    return value.toString();
                }
            } catch (IllegalArgumentException e) {
                return "{" + v + "}";
            }
        }

        return "{" + v + "}";
    }
//    private int xp = 0;
//    private long startTime = System.currentTimeMillis();  // todo: more control over how and when time resets
//    private int nodesMined = 0;
//    private int nextLevel = 0;
//    private String type = "" + TextFormatting.RED + TextFormatting.BOLD + "Not Set";  // type of what?
//    private String progress = ""; // progress of what?


}
