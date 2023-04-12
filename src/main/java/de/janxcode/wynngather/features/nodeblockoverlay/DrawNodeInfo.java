package de.janxcode.wynngather.features.nodeblockoverlay;

import de.janxcode.wynngather.WynnGather;
import de.janxcode.wynngather.core.GatherNode;
import de.janxcode.wynngather.core.interfaces.IRegistrable;
import de.janxcode.wynngather.core.interfaces.IGlobalNodeRegistry;
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

public class DrawNodeInfo implements IRegistrable {
    private final Minecraft mc = Minecraft.getMinecraft();

    private final IGlobalNodeRegistry nodeRegister = WynnGather.getGlobalNodeRegistry();

    private final static Color FAR = new Color(44, 94, 26);
    private final static Color CLOSE = new Color(139, 0, 0);

    @SubscribeEvent
    public void onRender(RenderWorldLastEvent e) {
        if (mc.world == null) return;

        for (GatherNode node : nodeRegister.getNodes()) {
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
            if (node.isMined()) text += " [" + (int) (ratio * 100) + "%]";

            //Represents mining progress 2 blocks above node if mining
            if (node.getMiningProgress() != null && !node.getMiningProgress().equals("")) {
                RenderUtils.draw3DString(pos.getX(), pos.up().down(yOffset).getY(), pos.getZ(), node.getMiningProgress(), color, e.getPartialTicks());
                RenderUtils.draw3DString(pos.getX(), pos.up().down(yOffset).getY() + 0.3, pos.getZ(), text, color, e.getPartialTicks());
                continue;
            }

            RenderUtils.draw3DString(pos.getX(), pos.up().down(yOffset).getY(), pos.getZ(), text, color, e.getPartialTicks());
        }
    }

    private int determineRenderOffset(GatherNode node) {
        if (node.getProf().equals("mining")) return 2;
        if (node.getProf().equals("fishing")) return 1;
        if (node.getProf().equals("farming")) return 2;
        return 0;
    }

    private int determineColor(GatherNode node, double ratio) {
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
