package me.earth.phobos.mixin.mixins;

import org.spongepowered.asm.mixin.*;
import net.minecraft.client.*;
import org.spongepowered.asm.mixin.injection.callback.*;
import me.earth.phobos.features.gui.custom.*;
import org.spongepowered.asm.mixin.injection.*;
import net.minecraft.client.gui.*;
import me.earth.phobos.features.modules.render.*;
import me.earth.phobos.*;
import me.earth.phobos.features.modules.client.*;

@Mixin({ GuiIngame.class })
public class MixinGuiIngame extends Gui
{
    @Shadow
    @Final
    public GuiNewChat persistantChatGUI;
    
    @Inject(method = { "<init>" }, at = { @At("RETURN") })
    public void init(final Minecraft mcIn, final CallbackInfo ci) {
        this.persistantChatGUI = new GuiCustomNewChat(mcIn);
    }
    
    @Inject(method = { "renderPortal" }, at = { @At("HEAD") }, cancellable = true)
    protected void renderPortalHook(final float n, final ScaledResolution scaledResolution, final CallbackInfo info) {
        if (NoRender.getInstance().isOn() && NoRender.getInstance().portal.getValue()) {
            info.cancel();
        }
    }
    
    @Inject(method = { "renderPumpkinOverlay" }, at = { @At("HEAD") }, cancellable = true)
    protected void renderPumpkinOverlayHook(final ScaledResolution scaledRes, final CallbackInfo info) {
        if (NoRender.getInstance().isOn() && NoRender.getInstance().pumpkin.getValue()) {
            info.cancel();
        }
    }
    
    @Inject(method = { "renderPotionEffects" }, at = { @At("HEAD") }, cancellable = true)
    protected void renderPotionEffectsHook(final ScaledResolution scaledRes, final CallbackInfo info) {
        if (Phobos.moduleManager != null && !HUD.getInstance().potionIcons.getValue()) {
            info.cancel();
        }
    }
}
