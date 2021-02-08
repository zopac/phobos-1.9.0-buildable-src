//Deobfuscated with https://github.com/PetoPetko/Minecraft-Deobfuscator3000 using mappings "1.12 stable mappings"!

package me.earth.phobos.features.modules.misc;

import me.earth.phobos.features.modules.*;
import com.mojang.text2speech.*;
import me.earth.phobos.features.setting.*;
import net.minecraftforge.fml.common.eventhandler.*;
import me.earth.phobos.event.events.*;

public class Companion extends Module
{
    private Narrator narrator;
    public Setting<String> totemPopMessage;
    public Setting<String> deathMessages;
    
    public Companion() {
        super("Companion", "The best module", Category.MISC, true, false, false);
        this.narrator = Narrator.getNarrator();
        this.totemPopMessage = (Setting<String>)this.register(new Setting("PopMessage", "<player> watch out you're popping!"));
        this.deathMessages = (Setting<String>)this.register(new Setting("DeathMessage", "<player> you retard you just fucking died!"));
    }
    
    @Override
    public void onEnable() {
        this.narrator.say("Hello and welcome to phobos");
    }
    
    @Override
    public void onDisable() {
        this.narrator.clear();
    }
    
    @SubscribeEvent
    public void onTotemPop(final TotemPopEvent event) {
        if (event.getEntity() == Companion.mc.player) {
            this.narrator.say(this.totemPopMessage.getValue().replaceAll("<player>", Companion.mc.player.getName()));
        }
    }
    
    @SubscribeEvent
    public void onDeath(final DeathEvent event) {
        if (event.player == Companion.mc.player) {
            this.narrator.say(this.deathMessages.getValue().replaceAll("<player>", Companion.mc.player.getName()));
        }
    }
}
