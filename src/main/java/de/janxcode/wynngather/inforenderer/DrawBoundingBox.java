package de.janxcode.wynngather.inforenderer;

import de.janxcode.wynngather.WynnGather;
import de.janxcode.wynngather.utils.HorizontalPos;
import de.janxcode.wynngather.utils.RenderUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.item.EntityArmorStand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.client.event.RenderLivingEvent;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.awt.*;
import java.util.List;

public class DrawBoundingBox {
    private final Minecraft mc = Minecraft.getMinecraft();
    private final List<Node> nodes = WynnGather.NODES;
    public final static Color FAR = new Color(44, 94, 26);
    public final static Color CLOSE = new Color(139, 0, 0);

    @SubscribeEvent
    public void onRender(RenderWorldLastEvent e){
        if(mc.world == null || nodes.isEmpty()) return;

        for (Node node : nodes){
            int color = FAR.getRGB();
            double ratio = node.getMinedTime() / 60000d;

            if(node.isMined()){
                int red = (int)Math.abs((ratio * FAR.getRed()) + ((1 - ratio) * CLOSE.getRed()));
                int green = (int)Math.abs((ratio * FAR.getGreen()) + ((1 - ratio) * CLOSE.getGreen()));
                int blue = (int)Math.abs((ratio * FAR.getBlue()) + ((1 - ratio) * CLOSE.getBlue()));

                color = new Color(red, green, blue).getRGB();
            }

            int yOffset = 0;
            BlockPos pos = new BlockPos(node.getPos().getX(), node.getYPos(), node.getPos().getZ());
            if(node.getProf().equals("mining")) yOffset = 2;
            if(node.getProf().equals("fishing")) yOffset = 1;
            if(node.getProf().equals("farming")) yOffset = 2;

            RenderUtils.drawFilled3DBox(pos.down(yOffset), color, true, false, e.getPartialTicks());

            String text = TextFormatting.BOLD + node.getType();
            if(node.isMined()) text = text + " [" + (int)(ratio * 100) + "%]";
            RenderUtils.draw3DString(pos.up().down(yOffset), text, color, e.getPartialTicks());

            if(node.getMiningProgress() != null) {
                RenderUtils.draw3DString(pos.up(2).down(yOffset), node.getMiningProgress(), color, e.getPartialTicks());
            }
        }
    }

    @SubscribeEvent
    public void onRenderLiving(RenderLivingEvent.Specials.Pre e){
        if(e.getEntity() instanceof EntityArmorStand) {
            EntityArmorStand stand = (EntityArmorStand) e.getEntity();
            HorizontalPos pos = new HorizontalPos((int)Math.floor(stand.posX), (int)Math.floor(stand.posZ));

            if(nodes.stream().anyMatch(node -> node.getPos().equals(pos))){
                e.setCanceled(true);
            }
        }
    }
}
