package de.janxcode.wynngather.features.statsoverlay;

import de.janxcode.wynngather.core.NodeProgressUpdatedEvent;
import de.janxcode.wynngather.core.interfaces.IRegisterable;
import de.janxcode.wynngather.utils.Utils;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class GatheringSession implements IRegisterable {
    // todo:
    //  - extract parts of this class that are not bound to the current gathering session (artifacts from Info(Helper) class)
    private int xp = 0;
    private long sessionStart = -1;
    private int nodesMined = 0;
    private int nextLevel = 0;
    private String gatheringType = "" + TextFormatting.RED + TextFormatting.BOLD + "Not Set";  // todo: initialize to null, move default value to GUI class

    public void start() {
        sessionStart = System.currentTimeMillis(); // todo: use Instant API
    }

    @SubscribeEvent
    public void onNodeMined(NodeProgressUpdatedEvent.MinedEvent e) {
        nodesMined++;
        nextLevel = e.next;
        xp += e.xp;
        gatheringType = e.node.getType();
    }


    public int getNodesMined() {
        return nodesMined;
    }

    public String getType() {
        return gatheringType;
    }

    public int getNextLevel() {
        return nextLevel;
    }


    public String getTime() { // todo: use Duration API
        long seconds = (System.currentTimeMillis() - sessionStart) / 1000;

        return String.format("%02d:%02d:%02d", seconds / 3600, (seconds % 3600) / 60, seconds % 60);
    }

    public int getNodesPerMinute() { // todo: use Duration API
        long seconds = (System.currentTimeMillis() - sessionStart) / 1000;

        if (seconds / 60d > 0) {
            return (int) (nodesMined / (seconds / 60d));
        }

        return 0;
    }

    public String getXpPerHour() { // todo: use Duration API
        long seconds = (System.currentTimeMillis() - sessionStart) / 1000;
        if (seconds / 3600d > 0) {
            return Utils.formatValue((long) (xp / (seconds / 3600d)));
        }

        return "0";
    }
}
