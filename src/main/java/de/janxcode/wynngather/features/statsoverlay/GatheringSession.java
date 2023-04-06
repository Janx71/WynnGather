package de.janxcode.wynngather.features.statsoverlay;

import de.janxcode.wynngather.WynnGather;
import de.janxcode.wynngather.core.NodeProgressUpdatedEvent;
import de.janxcode.wynngather.core.interfaces.IRegisterable;
import de.janxcode.wynngather.utils.Utils;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GatheringSession implements IRegisterable {
    // todo:
    //  - extract parts of this class that are not bound to the current gathering session (artifacts from Info(Helper) class)
    private int xp = 0;
    private long sessionStart = -1;
    private int nodesMined = 0;
    private int nextLevel = 0;
    private String toolDurability = null;
    private String gatheringType = String.valueOf(TextFormatting.RED) + TextFormatting.BOLD + "Not Set";  // todo: initialize to null, move default value to GUI class
    private static final Minecraft mc = Minecraft.getMinecraft();

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

    @SubscribeEvent
    public void onClientTick(TickEvent.ClientTickEvent e) {
        if (mc.player == null || mc.world == null || e.phase != TickEvent.Phase.END) return;

        //current item in hand
        ItemStack itemStack = mc.player.getHeldItemMainhand();
        toolDurability = getDurability(itemStack);
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

    public String getToolDurability(){
        return toolDurability;
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

    private static final Pattern DURABILITY_PATTERN = Pattern.compile("(\\d+/\\d+)\\sDurability");

    private String getDurability(ItemStack itemStack) {
        if (!itemStack.hasTagCompound()) return null;

        NBTTagCompound tag = itemStack.getTagCompound();

        assert tag != null;
        NBTTagCompound display = tag.getCompoundTag("display");

        NBTTagList lore = display.getTagList("Lore", 8);
        for (int i = 0; i < lore.tagCount(); i++) {
            String loreLine = lore.getStringTagAt(i);

            if (!loreLine.contains("Gathering Tool")) continue;

            Matcher matcher = DURABILITY_PATTERN.matcher(loreLine);
            if (matcher.find()) return matcher.group(1);
        }

        return null;
    }
}
