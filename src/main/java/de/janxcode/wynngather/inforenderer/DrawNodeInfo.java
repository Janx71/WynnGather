package de.janxcode.wynngather.inforenderer;

import de.janxcode.wynngather.interfaces.INodeRegister;
import de.janxcode.wynngather.utils.HorizontalPos;
import de.janxcode.wynngather.utils.ModConfig;
import de.janxcode.wynngather.utils.RenderUtils;
import de.janxcode.wynngather.utils.Utils;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.item.EntityArmorStand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.client.event.RenderLivingEvent;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.awt.*;

public class DrawNodeInfo {
    private final Minecraft mc = Minecraft.getMinecraft();

    private final INodeRegister nodeRegister;

    public DrawNodeInfo(INodeRegister nodeRegister) {
        this.nodeRegister = nodeRegister;
    }

    private final static Color FAR = new Color(44, 94, 26);
    private final static Color CLOSE = new Color(139, 0, 0);

    @SubscribeEvent
    public void onRender(RenderWorldLastEvent e) {
        if (mc.world == null) return;

        for (Node node : nodeRegister.getNodes()) {
            BlockPos pos = new BlockPos(node.getPos().getX(), node.getYPos(), node.getPos().getZ());
            AxisAlignedBB aabb = Utils.getAabb(ModConfig.renderDistance);

            if (!aabb.contains(new Vec3d(pos.getX(), pos.getY(), pos.getZ()))) continue;

            //Ration is a value from 0 to 1 representing the Node regeneration process. Color depends on ratio
            double ratio = node.getMinedTime() / 60000d;
            int color = determineColor(node, ratio);

            //Gets yOffset depending on material type draws block bounds with offset for current Node.
            int yOffset = determineRenderOffset(node);
            RenderUtils.drawBox(pos.down(yOffset), color, e.getPartialTicks());


            //Draws material name one block above the current node
            //Includes regeneration percentage if not fully regenerated
            String text = TextFormatting.BOLD + node.getType();
            if (node.isMined()) text = text + " [" + (int) (ratio * 100) + "%]";
            RenderUtils.draw3DString(pos.up().down(yOffset), text, color, e.getPartialTicks());

            //Represents mining progress 2 blocks above node if mining
            if (node.getMiningProgress() != null) {
                RenderUtils.draw3DString(pos.up(2).down(yOffset), node.getMiningProgress(), color, e.getPartialTicks());
            }
        }
    }

    private int determineRenderOffset(Node node) {
        if (node.getProf().equals("mining")) return 2;
        if (node.getProf().equals("fishing")) return 1;
        if (node.getProf().equals("farming")) return 2;
        return 0;
    }

    private int determineColor(Node node, double ratio) {
        int color = FAR.getRGB();

        //Code copied from StackOverflow
        if (node.isMined()) {
            int red = (int) Math.abs((ratio * FAR.getRed()) + ((1 - ratio) * CLOSE.getRed()));
            int green = (int) Math.abs((ratio * FAR.getGreen()) + ((1 - ratio) * CLOSE.getGreen()));
            int blue = (int) Math.abs((ratio * FAR.getBlue()) + ((1 - ratio) * CLOSE.getBlue()));

            color = new Color(red, green, blue).getRGB();
        }

        return color;
    }

    @SubscribeEvent
    public void onRenderLiving(RenderLivingEvent.Specials.Pre e) {  // generic parameter?
        if (!(e.getEntity() instanceof EntityArmorStand)) return;

        EntityArmorStand stand = (EntityArmorStand) e.getEntity();
        HorizontalPos pos = new HorizontalPos((int) Math.floor(stand.posX), (int) Math.floor(stand.posZ));

        //Cancels rendering of EntityArmorStand and its name tag if aligns with node
        if (nodeRegister.getNodes().stream().anyMatch(node -> node.getPos().equals(pos))) {
            e.setCanceled(true);
        }
    }

}
