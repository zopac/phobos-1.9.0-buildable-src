//Deobfuscated with https://github.com/PetoPetko/Minecraft-Deobfuscator3000 using mappings "1.12 stable mappings"!

package me.earth.phobos.features.modules.movement;

import me.earth.phobos.features.modules.*;
import net.minecraft.client.entity.*;

public class ReverseStep extends Module
{
    public ReverseStep() {
        super("ReverseStep", "Screams chinese words and teleports you", Category.MOVEMENT, true, false, false);
    }
    
    @Override
    public void onUpdate() {
        if (ReverseStep.mc.player.onGround) {
            final EntityPlayerSP player = ReverseStep.mc.player;
            --player.motionY;
        }
    }
}
