//Deobfuscated with https://github.com/PetoPetko/Minecraft-Deobfuscator3000 using mappings "1.12 stable mappings"!

package me.earth.phobos.manager;

import me.earth.phobos.features.*;
import me.earth.phobos.util.Util;
import net.minecraft.util.*;
import java.util.*;
import java.awt.*;
import org.lwjgl.opengl.*;
import net.minecraft.util.math.*;
import net.minecraft.client.renderer.*;
import me.earth.phobos.util.*;

public class WaypointManager extends Feature
{
    public static final ResourceLocation WAYPOINT_RESOURCE;
    public Map<String, Waypoint> waypoints;
    
    public WaypointManager() {
        this.waypoints = new HashMap<String, Waypoint>();
    }
    
    static {
        WAYPOINT_RESOURCE = new ResourceLocation("textures/waypoint.png");
    }
    
    public static class Waypoint
    {
        public String owner;
        public String server;
        public int dimension;
        public int x;
        public int y;
        public int z;
        public int red;
        public int green;
        public int blue;
        public int alpha;
        
        public Waypoint(final String owner, final String server, final int dimension, final int x, final int y, final int z, final Color color) {
            this.owner = owner;
            this.server = server;
            this.dimension = dimension;
            this.x = x;
            this.y = y;
            this.z = z;
            this.red = color.getRed();
            this.green = color.getGreen();
            this.blue = color.getBlue();
            this.alpha = color.getAlpha();
        }
        
        public void renderBox() {
            GL11.glPushMatrix();
            GL11.glDisable(2896);
            GL11.glDisable(2929);
            GL11.glEnable(3042);
            GL11.glBlendFunc(770, 771);
            GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
            RenderUtil.drawBlockOutline(new BlockPos(this.x, this.y, this.z), new Color(this.red, this.green, this.blue, this.alpha), 1.0f, true);
            GlStateManager.enableTexture2D();
            GL11.glDisable(3042);
            GL11.glEnable(2896);
            GL11.glEnable(2929);
            GL11.glPopMatrix();
        }
        
        public void render() {
            final double posX = this.x - Util.mc.getRenderManager().renderPosX + 0.5;
            final double posY = this.y - Util.mc.getRenderManager().renderPosY - 0.5;
            final double posZ = this.z - Util.mc.getRenderManager().renderPosZ + 0.5;
            float scale = (float)(Util.mc.player.getDistance(posX + Util.mc.getRenderManager().renderPosX, posY + Util.mc.getRenderManager().renderPosY, posZ + Util.mc.getRenderManager().renderPosZ) / 4.0);
            if (scale < 1.6f) {
                scale = 1.6f;
            }
            scale /= 25.0f;
            GL11.glPushMatrix();
            GL11.glTranslatef((float)posX, (float)posY + 1.4f, (float)posZ);
            GL11.glNormal3f(0.0f, 1.0f, 0.0f);
            GL11.glRotatef(-Util.mc.getRenderManager().playerViewY, 0.0f, 1.0f, 0.0f);
            GL11.glRotatef(Util.mc.getRenderManager().playerViewX, 1.0f, 0.0f, 0.0f);
            GL11.glScalef(-scale, -scale, scale);
            GL11.glDisable(2896);
            GL11.glDisable(2929);
            GL11.glEnable(3042);
            GL11.glBlendFunc(770, 771);
            final int width = Util.mc.fontRenderer.getStringWidth(this.owner) / 2;
            GL11.glPushMatrix();
            GL11.glPopMatrix();
            GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
            GL11.glScalef(0.5f, 0.5f, 0.5f);
            Util.mc.fontRenderer.drawStringWithShadow(this.owner, (float)(-width), (float)(-(Util.mc.fontRenderer.FONT_HEIGHT + 7)), new Color(this.red, this.green, this.blue, this.alpha).getRGB());
            GL11.glScalef(1.0f, 1.0f, 1.0f);
            GlStateManager.enableTexture2D();
            GL11.glDisable(3042);
            GL11.glEnable(2896);
            GL11.glEnable(2929);
            GL11.glPopMatrix();
            GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
        }
    }
}
