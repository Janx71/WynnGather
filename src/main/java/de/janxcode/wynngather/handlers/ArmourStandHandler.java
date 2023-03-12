package de.janxcode.wynngather.handlers;

import de.janxcode.wynngather.WynnGather;
import de.janxcode.wynngather.inforenderer.DrawInfoPanel;
import de.janxcode.wynngather.inforenderer.Node;
import de.janxcode.wynngather.inforenderer.NodeType;
import de.janxcode.wynngather.utils.HorizontalPos;
import de.janxcode.wynngather.utils.Utils;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.item.EntityArmorStand;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.util.Arrays;
import java.util.List;

public class ArmourStandHandler {
    private final Minecraft mc = Minecraft.getMinecraft();
    private final DrawInfoPanel infoPanel = DrawInfoPanel.getPanel();
    private final List<Node> nodes = WynnGather.NODES;
    private int mineTicks = 0;

    @SubscribeEvent
    public void onTick(TickEvent.ClientTickEvent e){
        if(mc.world == null || mc.player == null ) return;
        mineTicks++;

        //Player coordinates used to create an AxisAlignedBB with player at center to search for EntityArmorStand
        int boxRadius = 20;
        Iterable<EntityArmorStand> stands = mc.world.getEntitiesWithinAABB(EntityArmorStand.class, Utils.getAabb(20));

        //Loop over every EntityArmorStand in aabb and apply needed actions
        for(EntityArmorStand stand : stands) {
            NBTTagCompound nbt = stand.serializeNBT();

            if (nbt.getString("CustomName").equals("")) continue;

            String name = TextFormatting.getTextWithoutFormattingCodes(nbt.getString("CustomName"));
            if (name == null) continue;

            //Math.floor() is needed because rounding will sometimes produce a wrong outcome
            HorizontalPos pos = new HorizontalPos((int) Math.floor(stand.posX), (int) Math.floor(stand.posZ));

            //If the Node is identified as a material, check if already is in the List nodes by comparing position. If not -> add
            if (isMaterial(name) && nodes.stream().noneMatch(node -> node.getPos().equals(pos))) {
                nodes.add(new Node(pos, (int)stand.posY, name));
            }

            //Get current node by comparing positions
            Node node = null;
            for (Node n: nodes){
                if(n.getPos().equals(pos)){
                    node = n;
                }
            }

            if(node == null) continue;

            //Perform actions for specific Nodes
            if(node.getProf().equals("")) setProfession(node, name);
            if(name.contains("XP] [")) mineTicks = setMined(node, stand, name, mineTicks);
            if(name.contains("[|||")) setProgress(node, nbt);
        }
    }

    private boolean isMaterial(String name){
        return Arrays.stream(NodeType.nodeTypes).anyMatch(nodeType -> nodeType.equalsIgnoreCase(name));
    }

    private void setProgress(Node node, NBTTagCompound nbt){
        //Sets the progress Strings for the current node and the info tab
        if(!node.isMined()){
            node.setMiningProgress(nbt.getString("CustomName"));
            infoPanel.setProgress(nbt.getString("CustomName"));
        }
    }

    private void setProfession(Node node, String name){
        if(name.toLowerCase().contains("mining")) node.setProf("mining");
        else if(name.toLowerCase().contains("cutting")) node.setProf("cutting");
        else if(name.toLowerCase().contains("fishing")) node.setProf("fishing");
        else if(name.toLowerCase().contains("farming")) node.setProf("farming");
    }

    private int setMined(Node node, EntityArmorStand stand, String name, int ticks){
        //The tag needs to be removed instantly because it would count multiple times.
        //Mining a Node will produce 2 separate finished tags in short succession. Sets cool down between checks
        stand.setCustomNameTag("");
        if(ticks <= 40) return ticks;

        Utils.parseXpInfo(name, node);
        mineTicks = 0;

        node.setMined();
        node.setMiningProgress("");
        infoPanel.setProgress("");
        return 0;
    }
}
