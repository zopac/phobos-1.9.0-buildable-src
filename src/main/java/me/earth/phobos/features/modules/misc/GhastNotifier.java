//Deobfuscated with https://github.com/PetoPetko/Minecraft-Deobfuscator3000 using mappings "1.12 stable mappings"!

package me.earth.phobos.features.modules.misc;

import me.earth.phobos.features.modules.*;
import net.minecraft.entity.*;
import me.earth.phobos.features.setting.*;
import net.minecraft.entity.monster.*;
import me.earth.phobos.features.command.*;
import net.minecraft.init.*;
import java.util.*;

public class GhastNotifier extends Module
{
    private Set<Entity> ghasts;
    public Setting<Boolean> Chat;
    public Setting<Boolean> Sound;
    
    public GhastNotifier() {
        super("GhastNotifier", "Helps you find ghasts", Category.MISC, true, false, false);
        this.ghasts = new HashSet<Entity>();
        this.Chat = (Setting<Boolean>)this.register(new Setting("Chat", true));
        this.Sound = (Setting<Boolean>)this.register(new Setting("Sound", true));
    }
    
    @Override
    public void onEnable() {
        this.ghasts.clear();
    }
    
    @Override
    public void onUpdate() {
        for (final Entity entity : GhastNotifier.mc.world.getLoadedEntityList()) {
            if (entity instanceof EntityGhast && !this.ghasts.contains(entity)) {
                if (this.Chat.getValue()) {
                    Command.sendMessage("Ghast Detected at: " + entity.getPosition().getX() + "x, " + entity.getPosition().getY() + "y, " + entity.getPosition().getZ() + "z.");
                }
                this.ghasts.add(entity);
                if (!this.Sound.getValue()) {
                    continue;
                }
                GhastNotifier.mc.player.playSound(SoundEvents.BLOCK_ANVIL_DESTROY, 1.0f, 1.0f);
            }
        }
    }
}
