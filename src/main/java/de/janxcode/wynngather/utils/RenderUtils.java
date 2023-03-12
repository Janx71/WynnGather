package de.janxcode.wynngather.utils;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import org.lwjgl.opengl.GL11;

import java.awt.*;

//Got some of this code from a forum of that I forgot the name. Sure do hope it's not licensed xD
public class RenderUtils {
    public static void draw3DString(BlockPos pos, String text, int colour, float partialTicks) {
        Minecraft mc = Minecraft.getMinecraft();
        EntityPlayer player = mc.player;
        double x = (pos.getX() + 0.5 - player.lastTickPosX) + ((pos.getX() - player.posX + 0.5) - (pos.getX() - player.lastTickPosX + 0.5)) * partialTicks;
        double y = (pos.getY() + 0.5 - player.lastTickPosY) + ((pos.getY() - player.posY + 0.5) - (pos.getY() - player.lastTickPosY + 0.5)) * partialTicks;
        double z = (pos.getZ() + 0.5 - player.lastTickPosZ) + ((pos.getZ() - player.posZ + 0.5) - (pos.getZ() - player.lastTickPosZ + 0.5)) * partialTicks;
        RenderManager renderManager = mc.getRenderManager();

        float f = 1.6F;
        float f1 = 0.016666668F * f;
        int width = mc.fontRenderer.getStringWidth(text) / 2;
        GlStateManager.pushMatrix();
        GlStateManager.translate(x, y, z);
        GL11.glNormal3f(0f, 1f, 0f);
        GlStateManager.rotate(-renderManager.playerViewY, 0f, 1f, 0f);
        GlStateManager.rotate(renderManager.playerViewX, 1f, 0f, 0f);
        GlStateManager.scale(-f1, -f1, -f1);
        GlStateManager.enableBlend();
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        mc.fontRenderer.drawString(text, -width, 0, colour);
        GlStateManager.disableBlend();
        GlStateManager.popMatrix();
    }

    public static void drawBox(BlockPos pos, int colourInt, float partialTicks) {
        AxisAlignedBB aabb = new AxisAlignedBB(pos.getX(), pos.getY(), pos.getZ(), pos.getX() + 1, pos.getY() + 1, pos.getZ() + 1);

        Entity render = Minecraft.getMinecraft().getRenderViewEntity();
        BufferBuilder worldRenderer = Tessellator.getInstance().getBuffer();
        Color colour = new Color(colourInt);

        assert render != null;
        double realX = render.lastTickPosX + (render.posX - render.lastTickPosX) * partialTicks;
        double realY = render.lastTickPosY + (render.posY - render.lastTickPosY) * partialTicks;
        double realZ = render.lastTickPosZ + (render.posZ - render.lastTickPosZ) * partialTicks;

        GlStateManager.pushMatrix();
        GlStateManager.pushAttrib();
        GlStateManager.translate(-realX, -realY, -realZ);
        GlStateManager.disableTexture2D();
        GlStateManager.enableAlpha();
        GlStateManager.enableBlend();
        GlStateManager.disableCull();
        GlStateManager.tryBlendFuncSeparate(770, 1, 1, 0);
        GlStateManager.color(colour.getRed() / 255f, colour.getGreen() / 255f, colour.getBlue() / 255f, 70 / 255f);

        GL11.glDisable(GL11.GL_DEPTH_TEST);
        GlStateManager.depthMask(false);

        worldRenderer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION);
        // Bottom
        worldRenderer.pos(aabb.minX, aabb.minY, aabb.minZ).endVertex();
        worldRenderer.pos(aabb.maxX, aabb.minY, aabb.minZ).endVertex();
        worldRenderer.pos(aabb.maxX, aabb.minY, aabb.maxZ).endVertex();
        worldRenderer.pos(aabb.minX, aabb.minY, aabb.maxZ).endVertex();
        // Top
        worldRenderer.pos(aabb.minX, aabb.maxY, aabb.minZ).endVertex();
        worldRenderer.pos(aabb.maxX, aabb.maxY, aabb.minZ).endVertex();
        worldRenderer.pos(aabb.maxX, aabb.maxY, aabb.maxZ).endVertex();
        worldRenderer.pos(aabb.minX, aabb.maxY, aabb.maxZ).endVertex();
        // West
        worldRenderer.pos(aabb.minX, aabb.minY, aabb.minZ).endVertex();
        worldRenderer.pos(aabb.minX, aabb.maxY, aabb.minZ).endVertex();
        worldRenderer.pos(aabb.minX, aabb.maxY, aabb.maxZ).endVertex();
        worldRenderer.pos(aabb.minX, aabb.minY, aabb.maxZ).endVertex();
        // East
        worldRenderer.pos(aabb.maxX, aabb.minY, aabb.minZ).endVertex();
        worldRenderer.pos(aabb.maxX, aabb.maxY, aabb.minZ).endVertex();
        worldRenderer.pos(aabb.maxX, aabb.maxY, aabb.maxZ).endVertex();
        worldRenderer.pos(aabb.maxX, aabb.minY, aabb.maxZ).endVertex();
        // North
        worldRenderer.pos(aabb.minX, aabb.minY, aabb.minZ).endVertex();
        worldRenderer.pos(aabb.maxX, aabb.minY, aabb.minZ).endVertex();
        worldRenderer.pos(aabb.maxX, aabb.maxY, aabb.minZ).endVertex();
        worldRenderer.pos(aabb.minX, aabb.maxY, aabb.minZ).endVertex();
        // South
        worldRenderer.pos(aabb.minX, aabb.minY, aabb.maxZ).endVertex();
        worldRenderer.pos(aabb.maxX, aabb.minY, aabb.maxZ).endVertex();
        worldRenderer.pos(aabb.maxX, aabb.maxY, aabb.maxZ).endVertex();
        worldRenderer.pos(aabb.minX, aabb.maxY, aabb.maxZ).endVertex();
        Tessellator.getInstance().draw();

        GlStateManager.translate(realX, realY, realZ);

        GL11.glEnable(GL11.GL_DEPTH_TEST);
        GlStateManager.depthMask(true);

        GlStateManager.enableCull();
        GlStateManager.disableAlpha();
        GlStateManager.disableBlend();
        GlStateManager.enableTexture2D();
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        GlStateManager.popAttrib();
        GlStateManager.popMatrix();
    }

    public static void drawLayeredString(String[] text, int x, int y, int color, FontRenderer renderer){
        int lines = 0;
        for (String line : text){
            renderer.drawString(line, x, y + renderer.FONT_HEIGHT * lines, color);
            lines++;
        }
    }
}
