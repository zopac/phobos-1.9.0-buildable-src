//Deobfuscated with https://github.com/PetoPetko/Minecraft-Deobfuscator3000 using mappings "1.12 stable mappings"!

package me.earth.phobos.features.modules.render;

import me.earth.phobos.features.modules.*;
import me.earth.phobos.features.setting.*;
import net.minecraft.entity.item.*;
import java.util.concurrent.*;
import net.minecraft.entity.*;
import java.util.*;
import net.minecraft.network.play.server.*;
import net.minecraftforge.fml.common.eventhandler.*;
import me.earth.phobos.event.events.*;
import me.earth.phobos.features.modules.client.*;
import me.earth.phobos.util.*;
import org.lwjgl.opengl.*;
import net.minecraft.client.renderer.*;
import java.awt.*;

public class CrystalScale extends Module
{
    public Setting<Boolean> animateScale;
    public Setting<Boolean> chams;
    public Setting<Boolean> throughWalls;
    public Setting<Boolean> wireframeThroughWalls;
    public Setting<Boolean> glint;
    public Setting<Boolean> wireframe;
    public Setting<Float> scale;
    public Setting<Float> lineWidth;
    public Setting<Boolean> colorSync;
    public Setting<Boolean> rainbow;
    public Setting<Integer> saturation;
    public Setting<Integer> brightness;
    public Setting<Integer> speed;
    public Setting<Boolean> xqz;
    public Setting<Integer> red;
    public Setting<Integer> green;
    public Setting<Integer> blue;
    public Setting<Integer> alpha;
    public Setting<Integer> hiddenRed;
    public Setting<Integer> hiddenGreen;
    public Setting<Integer> hiddenBlue;
    public Setting<Integer> hiddenAlpha;
    public Map<EntityEnderCrystal, Float> scaleMap;
    public static CrystalScale INSTANCE;
    
    public CrystalScale() {
        super("CrystalModifier", "Modifies crystal rendering in different ways", Category.RENDER, true, false, false);
        this.animateScale = (Setting<Boolean>)this.register(new Setting("AnimateScale", false));
        this.chams = (Setting<Boolean>)this.register(new Setting("Chams", false));
        this.throughWalls = (Setting<Boolean>)this.register(new Setting("ThroughWalls", true));
        this.wireframeThroughWalls = (Setting<Boolean>)this.register(new Setting("WireThroughWalls", true));
        this.glint = (Setting<Boolean>)this.register(new Setting("Glint", false, v -> this.chams.getValue()));
        this.wireframe = (Setting<Boolean>)this.register(new Setting("Wireframe", false));
        this.scale = (Setting<Float>)this.register(new Setting("Scale", 1.0f, 0.1f, 10.0f));
        this.lineWidth = (Setting<Float>)this.register(new Setting("LineWidth", 1.0f, 0.1f, 3.0f));
        this.colorSync = (Setting<Boolean>)this.register(new Setting("Sync", false));
        this.rainbow = (Setting<Boolean>)this.register(new Setting("Rainbow", false));
        this.saturation = (Setting<Integer>)this.register(new Setting("Saturation", 50, 0, 100, v -> this.rainbow.getValue()));
        this.brightness = (Setting<Integer>)this.register(new Setting("Brightness", 100, 0, 100, v -> this.rainbow.getValue()));
        this.speed = (Setting<Integer>)this.register(new Setting("Speed", 40, 1, 100, v -> this.rainbow.getValue()));
        this.xqz = (Setting<Boolean>)this.register(new Setting("XQZ", false, v -> !this.rainbow.getValue() && this.throughWalls.getValue()));
        this.red = (Setting<Integer>)this.register(new Setting("Red", 0, 0, 255, v -> !this.rainbow.getValue()));
        this.green = (Setting<Integer>)this.register(new Setting("Green", 255, 0, 255, v -> !this.rainbow.getValue()));
        this.blue = (Setting<Integer>)this.register(new Setting("Blue", 0, 0, 255, v -> !this.rainbow.getValue()));
        this.alpha = (Setting<Integer>)this.register(new Setting("Alpha", 255, 0, 255));
        this.hiddenRed = (Setting<Integer>)this.register(new Setting("Hidden Red", 255, 0, 255, v -> this.xqz.getValue() && !this.rainbow.getValue()));
        this.hiddenGreen = (Setting<Integer>)this.register(new Setting("Hidden Green", 0, 0, 255, v -> this.xqz.getValue() && !this.rainbow.getValue()));
        this.hiddenBlue = (Setting<Integer>)this.register(new Setting("Hidden Blue", 255, 0, 255, v -> this.xqz.getValue() && !this.rainbow.getValue()));
        this.hiddenAlpha = (Setting<Integer>)this.register(new Setting("Hidden Alpha", 255, 0, 255, v -> this.xqz.getValue() && !this.rainbow.getValue()));
        this.scaleMap = new ConcurrentHashMap<EntityEnderCrystal, Float>();
        CrystalScale.INSTANCE = this;
    }
    
    @Override
    public void onUpdate() {
        for (final Entity crystal : CrystalScale.mc.world.loadedEntityList) {
            if (crystal instanceof EntityEnderCrystal) {
                if (!this.scaleMap.containsKey(crystal)) {
                    this.scaleMap.put((EntityEnderCrystal)crystal, 3.125E-4f);
                }
                else {
                    this.scaleMap.put((EntityEnderCrystal)crystal, this.scaleMap.get(crystal) + 3.125E-4f);
                }
                if (this.scaleMap.get(crystal) < 0.0625f * this.scale.getValue()) {
                    continue;
                }
                this.scaleMap.remove(crystal);
            }
        }
    }
    
    @SubscribeEvent
    public void onReceivePacket(final PacketEvent.Receive event) {
        if (event.getPacket() instanceof SPacketDestroyEntities) {
            final SPacketDestroyEntities packet = event.getPacket();
            for (final int id : packet.getEntityIDs()) {
                final Entity entity = CrystalScale.mc.world.getEntityByID(id);
                if (entity instanceof EntityEnderCrystal) {
                    this.scaleMap.remove(entity);
                }
            }
        }
    }
    
    public void onRenderModel(final RenderEntityModelEvent event) {
        if (event.getStage() != 0 || !(event.entity instanceof EntityEnderCrystal) || !this.wireframe.getValue()) {
            return;
        }
        final Color color = this.colorSync.getValue() ? Colors.INSTANCE.getCurrentColor() : EntityUtil.getColor(event.entity, this.red.getValue(), this.green.getValue(), this.blue.getValue(), this.alpha.getValue(), false);
        final boolean fancyGraphics = CrystalScale.mc.gameSettings.fancyGraphics;
        CrystalScale.mc.gameSettings.fancyGraphics = false;
        final float gamma = CrystalScale.mc.gameSettings.gammaSetting;
        CrystalScale.mc.gameSettings.gammaSetting = 10000.0f;
        GL11.glPushMatrix();
        GL11.glPushAttrib(1048575);
        GL11.glPolygonMode(1032, 6913);
        GL11.glDisable(3553);
        GL11.glDisable(2896);
        if (this.wireframeThroughWalls.getValue()) {
            GL11.glDisable(2929);
        }
        GL11.glEnable(2848);
        GL11.glEnable(3042);
        GlStateManager.blendFunc(770, 771);
        GlStateManager.color(color.getRed() / 255.0f, color.getGreen() / 255.0f, color.getBlue() / 255.0f, color.getAlpha() / 255.0f);
        GlStateManager.glLineWidth((float)this.lineWidth.getValue());
        event.modelBase.render(event.entity, event.limbSwing, event.limbSwingAmount, event.age, event.headYaw, event.headPitch, event.scale);
        GL11.glPopAttrib();
        GL11.glPopMatrix();
    }
}
