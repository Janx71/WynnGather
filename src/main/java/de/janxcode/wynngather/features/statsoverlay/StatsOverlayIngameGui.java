package de.janxcode.wynngather.features.statsoverlay;

import de.janxcode.wynngather.WynnGather;
import de.janxcode.wynngather.core.GatheringSession;
import de.janxcode.wynngather.core.NodeProgressUpdatedEvent;
import de.janxcode.wynngather.core.interfaces.IRegistrable;
import de.janxcode.wynngather.utils.ItemUtils;
import de.janxcode.wynngather.utils.ModConfig;
import de.janxcode.wynngather.utils.RenderUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.awt.*;
import java.util.Arrays;

public class StatsOverlayIngameGui implements IRegistrable {
    // todo: this should be a non-singleton class called GatherStatsOverlay or similar
    Minecraft mc = Minecraft.getMinecraft();
    // todo: handle null case by not rendering anything depending on the current session
    //   for example, gathering progress should work without a session, while xp/h should not
    private GatheringSession currentSession = null;
    private String latestNodeProgress = ""; // todo: move to registry

    public void setDisplayedSession(GatheringSession session) { // todo: interface
        currentSession = session;
    }

    @SubscribeEvent
    public void onNodeUpdate(NodeProgressUpdatedEvent e) {
        this.latestNodeProgress = e.node.getMiningProgress();
    }


    @SubscribeEvent
    public void onRender(RenderGameOverlayEvent.Text e) {

        if (WynnGather.GUI) return;

        RenderUtils.drawLayeredString(Arrays.stream(ModConfig.infoLines).map(this::getOutputLine), ModConfig.guiPosX, ModConfig.guiPosY, Color.WHITE.getRGB(), mc.fontRenderer);
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

    static final String GATHER_TYPE_NOT_SET = String.valueOf(TextFormatting.RED) + TextFormatting.BOLD + "Not Set";

    private String getStatsValue(String v) {
        // todo: refactor with enum
        switch (v) {
            case "nodesMined":
                return String.valueOf(currentSession.getNodesMined());

            case "nodesPerMinute":
                return String.valueOf(currentSession.getNodesPerMinute());

            case "xpPerHour":
                return String.valueOf(currentSession.getXpPerHour());

            case "nextLevel":
                return String.valueOf(currentSession.getNextLevel());

            case "type":
                String type = currentSession.getType();
                if (type == null) return GATHER_TYPE_NOT_SET;
                return type;

            case "time":
                return currentSession.getTime();

            case "progress":
                return latestNodeProgress;

            case "durability":
                if (heldToolDurability == null) return "N/A";
                return heldToolDurability;
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


    // todo: move to registry
    private String heldToolDurability = null;  // "current/max" if tool in hand, null otherwise
    private ItemStack lastHeldItem = ItemStack.EMPTY;

    @SubscribeEvent
    public void onClientTickEnd(TickEvent.ClientTickEvent e) {
        if (e.phase != TickEvent.Phase.END) return;
        if (mc.player == null) {
            lastHeldItem = ItemStack.EMPTY;
            return;
        }

        ItemStack heldItem = mc.player.getHeldItemMainhand();

        if (!ItemStack.areItemStacksEqual(lastHeldItem, heldItem)) {
            lastHeldItem = heldItem.copy();
            heldToolDurability = ItemUtils.getGatherToolDurability(lastHeldItem);
        }
    }

    public void sync() {
        lastHeldItem = mc.player.getHeldItemMainhand();
        heldToolDurability = ItemUtils.getGatherToolDurability(lastHeldItem);
    }
}
