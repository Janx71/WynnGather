package de.janxcode.wynngather.features.statsoverlay;

import de.janxcode.wynngather.core.NodeProgressUpdatedEvent;
import de.janxcode.wynngather.core.interfaces.IRegisterable;
import de.janxcode.wynngather.utils.Utils;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class StatsHelper implements IRegisterable {
    // todo:
    //  - Better name
    //  - Just make new instances instead of resetting a singleton
    //    - This would eliminate the uninitialized fields and the need to register locally
    private int xp;
    private long startTime;  // todo: more control over how and when time resets
    private int nodesMined;
    private int nextLevel;
    private String type;  // type of what?

    public void reset() {
        startTime = System.currentTimeMillis();  // todo: find a better way to handle time
        xp = 0;
        nodesMined = 0;
        nextLevel = 0;
        type = "" + TextFormatting.RED + TextFormatting.BOLD + "Not Set";
    }

    @Override
    public void register() {
        reset();
        IRegisterable.super.register();
    }

    @SubscribeEvent
    public void onNodeMined(NodeProgressUpdatedEvent.MinedEvent e) {
        nodesMined++;
        nextLevel = e.next;
        xp += e.xp;
        type = e.node.getType();
    }


    public int getNodesMined() {
        return nodesMined;
    }


    public String getType() {
        return type;
    }

    public int getNextLevel() {
        return nextLevel;
    }


    public String getTime() { // todo: use Duration API
        long seconds = (System.currentTimeMillis() - startTime) / 1000;

        return String.format("%02d:%02d:%02d", seconds / 3600, (seconds % 3600) / 60, seconds % 60);
    }

    public int getNodesPerMinute() { // todo: use Duration API
        long seconds = (System.currentTimeMillis() - startTime) / 1000;

        if (seconds / 60d > 0) {
            return (int) (nodesMined / (seconds / 60d));
        }

        return 0;
    }

    public String getXpPerHour() { // todo: use Duration API
        long seconds = (System.currentTimeMillis() - startTime) / 1000;
        if (seconds / 3600d > 0) {
            return Utils.formatValue((long) (xp / (seconds / 3600d)));
        }

        return "0";
    }
}
