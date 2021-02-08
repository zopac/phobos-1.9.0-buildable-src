//Deobfuscated with https://github.com/PetoPetko/Minecraft-Deobfuscator3000 using mappings "1.12 stable mappings"!

package me.earth.phobos.features.modules.render;

import me.earth.phobos.features.modules.*;
import me.earth.phobos.features.setting.*;
import net.minecraft.init.*;
import net.minecraft.potion.*;
import me.earth.phobos.event.events.*;
import net.minecraft.network.play.server.*;
import net.minecraftforge.fml.common.eventhandler.*;

public class Fullbright extends Module
{
    public Setting<Mode> mode;
    public Setting<Boolean> effects;
    private float previousSetting;
    
    public Fullbright() {
        super("Fullbright", "Makes your game brighter.", Category.RENDER, true, false, false);
        this.mode = (Setting<Mode>)this.register(new Setting("Mode", Mode.GAMMA));
        this.effects = (Setting<Boolean>)this.register(new Setting("Effects", false));
        this.previousSetting = 1.0f;
    }
    
    @Override
    public void onEnable() {
        this.previousSetting = Fullbright.mc.gameSettings.gammaSetting;
    }
    
    @Override
    public void onUpdate() {
        if (this.mode.getValue() == Mode.GAMMA) {
            Fullbright.mc.gameSettings.gammaSetting = 1000.0f;
        }
        if (this.mode.getValue() == Mode.POTION) {
            Fullbright.mc.player.addPotionEffect(new PotionEffect(MobEffects.NIGHT_VISION, 5210));
        }
    }
    
    @Override
    public void onDisable() {
        if (this.mode.getValue() == Mode.POTION) {
            Fullbright.mc.player.removePotionEffect(MobEffects.NIGHT_VISION);
        }
        Fullbright.mc.gameSettings.gammaSetting = this.previousSetting;
    }
    
    @SubscribeEvent
    public void onPacketReceive(final PacketEvent.Receive event) {
        if (event.getStage() == 0 && event.getPacket() instanceof SPacketEntityEffect && this.effects.getValue()) {
            final SPacketEntityEffect packet = event.getPacket();
            if (Fullbright.mc.player != null && packet.getEntityId() == Fullbright.mc.player.getEntityId() && (packet.getEffectId() == 9 || packet.getEffectId() == 15)) {
                event.setCanceled(true);
            }
        }
    }
    
    public enum Mode
    {
        GAMMA, 
        POTION;
    }
}
