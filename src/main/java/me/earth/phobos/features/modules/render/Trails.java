//Deobfuscated with https://github.com/PetoPetko/Minecraft-Deobfuscator3000 using mappings "1.12 stable mappings"!

package me.earth.phobos.features.modules.render;

import me.earth.phobos.features.modules.*;
import me.earth.phobos.features.setting.*;
import net.minecraft.entity.*;
import net.minecraft.util.math.*;
import net.minecraft.entity.projectile.*;
import java.util.*;
import net.minecraftforge.fml.common.eventhandler.*;
import me.earth.phobos.event.events.*;
import net.minecraft.client.renderer.*;
import me.earth.phobos.util.*;
import org.lwjgl.opengl.*;

public class Trails extends Module
{
    private final Setting<Float> lineWidth;
    private final Setting<Integer> red;
    private final Setting<Integer> green;
    private final Setting<Integer> blue;
    private final Setting<Integer> alpha;
    private Map<Entity, List<Vec3d>> renderMap;
    
    public Trails() {
        super("Trails", "Draws trails on projectiles", Category.RENDER, true, false, false);
        this.lineWidth = (Setting<Float>)this.register(new Setting("LineWidth", 1.5f, 0.1f, 5.0f));
        this.red = (Setting<Integer>)this.register(new Setting("Red", 0, 0, 255));
        this.green = (Setting<Integer>)this.register(new Setting("Green", 255, 0, 255));
        this.blue = (Setting<Integer>)this.register(new Setting("Blue", 0, 0, 255));
        this.alpha = (Setting<Integer>)this.register(new Setting("Alpha", 255, 0, 255));
        this.renderMap = new HashMap<Entity, List<Vec3d>>();
    }
    
    @SubscribeEvent
    public void onUpdateWalkingPlayer(final UpdateWalkingPlayerEvent event) {
        for (final Entity entity : Trails.mc.world.loadedEntityList) {
            if (entity instanceof EntityThrowable || entity instanceof EntityArrow) {
                List<Vec3d> vectors;
                if (this.renderMap.get(entity) != null) {
                    vectors = this.renderMap.get(entity);
                }
                else {
                    vectors = new ArrayList<Vec3d>();
                }
                vectors.add(new Vec3d(entity.posX, entity.posY, entity.posZ));
                this.renderMap.put(entity, vectors);
            }
        }
    }
    
    @Override
    public void onRender3D(final Render3DEvent event) {
        for (final Entity entity : Trails.mc.world.loadedEntityList) {
            if (!this.renderMap.containsKey(entity)) {
                continue;
            }
            GlStateManager.pushMatrix();
            RenderUtil.GLPre(this.lineWidth.getValue());
            GlStateManager.enableBlend();
            GlStateManager.disableTexture2D();
            GlStateManager.depthMask(false);
            GlStateManager.disableDepth();
            GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
            GL11.glColor4f(this.red.getValue() / 255.0f, this.green.getValue() / 255.0f, this.blue.getValue() / 255.0f, this.alpha.getValue() / 255.0f);
            GL11.glLineWidth((float)this.lineWidth.getValue());
            GL11.glBegin(1);
            for (int i = 0; i < this.renderMap.get(entity).size() - 1; ++i) {
                GL11.glVertex3d(this.renderMap.get(entity).get(i).x, this.renderMap.get(entity).get(i).y, this.renderMap.get(entity).get(i).z);
                GL11.glVertex3d(this.renderMap.get(entity).get(i + 1).x, this.renderMap.get(entity).get(i + 1).y, this.renderMap.get(entity).get(i + 1).z);
            }
            GL11.glEnd();
            GlStateManager.resetColor();
            GlStateManager.enableDepth();
            GlStateManager.depthMask(true);
            GlStateManager.enableTexture2D();
            GlStateManager.disableBlend();
            RenderUtil.GlPost();
            GlStateManager.popMatrix();
        }
    }
}
