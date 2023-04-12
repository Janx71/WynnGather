package de.janxcode.wynngather.core;

import de.janxcode.wynngather.core.interfaces.IRegistrable;
import de.janxcode.wynngather.core.interfaces.IGlobalNodeRegistry;
import de.janxcode.wynngather.utils.HorizontalPos;
import de.janxcode.wynngather.utils.Utils;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.item.EntityArmorStand;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ArmorStandNodeRegistry implements IGlobalNodeRegistry, IRegistrable {
    private final Minecraft mc = Minecraft.getMinecraft();
    private final List<GatherNode> nodes = new ArrayList<>();
    private int mineTicks = 0;

    @SubscribeEvent
    public void onTick(TickEvent.ClientTickEvent e) {
        if (mc.world == null || mc.player == null) return;
        mineTicks++;

        //Player coordinates used to create an AxisAlignedBB with player at center to search for EntityArmorStand
        int boxRadius = 20;
        Iterable<EntityArmorStand> stands = mc.world.getEntitiesWithinAABB(EntityArmorStand.class, Utils.getAabb(20));

        //Loop over every EntityArmorStand in aabb and apply needed actions
        for (EntityArmorStand stand : stands) {
            NBTTagCompound nbt = stand.serializeNBT();

            if (nbt.getString("CustomName").equals("")) continue;

            String name = TextFormatting.getTextWithoutFormattingCodes(nbt.getString("CustomName"));
            if (name == null) continue;

            //Math.floor() is needed because rounding will sometimes produce a wrong outcome
            HorizontalPos pos = new HorizontalPos((int) Math.floor(stand.posX), (int) Math.floor(stand.posZ));

            //If the Node is identified as a material, check if already is in the List nodes by comparing position. If not -> add
            if (isMaterial(name) && nodes.stream().noneMatch(node -> node.getPos().equals(pos))) {
                nodes.add(new GatherNode(pos, (int) stand.posY, name));
            }

            //Get current node by comparing positions
            GatherNode node = null;
            for (GatherNode n : nodes) {
                if (n.getPos().equals(pos)) {
                    node = n;
                }
            }

            if (node == null) continue;

            //Perform actions for specific Nodes
            if (node.getProf().equals("")) setProfession(node, name);
            if (name.contains("XP] [")) mineTicks = setMined(node, stand, name, mineTicks);
            if (name.contains("[|||")) setProgress(node, nbt);
        }
    }

    private boolean isMaterial(String name) {  // move to NodeType
        return Arrays.stream(NodeType.nodeTypes).anyMatch(nodeType -> nodeType.equalsIgnoreCase(name));
    }

    private void setProgress(GatherNode node, NBTTagCompound nbt) {
        //Sets the progress Strings for the current node and the info tab
        if (!node.isMined()) {
            node.setMiningProgress(nbt.getString("CustomName"));
            MinecraftForge.EVENT_BUS.post(new NodeProgressUpdatedEvent(node));
        }
    }

    private void setProfession(GatherNode node, String name) {
        // todo: refactor with enum, also this doesnt belong here
        if (name.toLowerCase().contains("mining")) node.setProf("mining");
        else if (name.toLowerCase().contains("cutting")) node.setProf("cutting");
        else if (name.toLowerCase().contains("fishing")) node.setProf("fishing");
        else if (name.toLowerCase().contains("farming")) node.setProf("farming");
    }

    // set method with return value and side effects, todo: refactor
    private int setMined(GatherNode node, EntityArmorStand stand, String name, int ticks) {
        //The tag needs to be removed instantly because it would count multiple times.
        //Mining a Node will produce 2 separate finished tags in short succession. Sets cool down between checks
        //todo: find better solution, presumably using the cooldown
        stand.setCustomNameTag("");
        if (ticks <= 40) return ticks;

        mineTicks = 0;

        node.setMined();
        node.setMiningProgress("");
        Utils.postNodeMined(name, node);

        return 0;
    }


    @Override
    public List<GatherNode> getNodes() {  // IReadOnlyList when
        return this.nodes;
    }
}
