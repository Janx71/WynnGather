package de.janxcode.wynngather.handlers;

import de.janxcode.wynngather.WynnGather;
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
    private final List<Node> nodes = WynnGather.NODES;
    private int mineTicks = 0;

    @SubscribeEvent
    public void onTick(TickEvent.ClientTickEvent e){
        if(mc.world == null || mc.player == null ) return;
        mineTicks++;

        double x = mc.player.posX;
        double y = mc.player.posY;
        double z = mc.player.posZ;

        int boxRadius = 20;
        Iterable<EntityArmorStand> stands = mc.world.getEntitiesWithinAABB(EntityArmorStand.class, new AxisAlignedBB(x - boxRadius, y - boxRadius, z - boxRadius, x + boxRadius, y + boxRadius, z+ boxRadius));

        for(EntityArmorStand stand : stands) {
            NBTTagCompound nbt = stand.serializeNBT();

            if (nbt.getString("CustomName").equals("")) continue;

            String name = TextFormatting.getTextWithoutFormattingCodes(nbt.getString("CustomName"));
            if (name == null) continue;

            HorizontalPos pos = new HorizontalPos((int) Math.floor(stand.posX), (int) Math.floor(stand.posZ));

            if (isMaterial(name) && nodes.stream().noneMatch(node -> node.getPos().equals(pos))) {
                nodes.add(new Node(pos, (int)stand.posY, name));
            }

            Node node = null;
            for (Node n: nodes){
                if(n.getPos().equals(pos)){
                    node = n;
                }
            }

            if(node == null) continue;

            if(node.getProf().equals("")){
                if(name.toLowerCase().contains("mining")) node.setProf("mining");
                else if(name.toLowerCase().contains("cutting")) node.setProf("cutting");
                else if(name.toLowerCase().contains("fishing")) node.setProf("fishing");
                else if(name.toLowerCase().contains("farming")) node.setProf("farming");
            }

            if(name.contains("XP] [")){
                stand.setCustomNameTag("");

                if(mineTicks <= 40) continue;

                Utils.parseXpInfo(name);
                mineTicks = 0;

                node.setMined();
                node.setMiningProgress("");
                WynnGather.mineProgress = "";
            }

            if(name.contains("[|||")){
                if(!node.isMined()){
                    node.setMiningProgress(nbt.getString("CustomName"));
                    WynnGather.mineProgress = nbt.getString("CustomName");
                }
            }
        }
    }

    private boolean isMaterial(String name){
        return Arrays.stream(NodeType.nodeTypes).anyMatch(nodeType -> nodeType.equalsIgnoreCase(name));
    }
}
