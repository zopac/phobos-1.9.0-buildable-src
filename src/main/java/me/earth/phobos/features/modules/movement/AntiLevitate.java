//Deobfuscated with https://github.com/PetoPetko/Minecraft-Deobfuscator3000 using mappings "1.12 stable mappings"!

package me.earth.phobos.features.modules.movement;

import me.earth.phobos.features.modules.*;
import net.minecraft.potion.*;
import java.util.*;

public class AntiLevitate extends Module
{
    public AntiLevitate() {
        super("AntiLevitate", "Removes shulker levitation", Category.MOVEMENT, false, false, false);
    }
    
    @Override
    public void onUpdate() {
        if (AntiLevitate.mc.player.isPotionActive((Potion)Objects.requireNonNull(Potion.getPotionFromResourceLocation("levitation")))) {
            AntiLevitate.mc.player.removeActivePotionEffect(Potion.getPotionFromResourceLocation("levitation"));
        }
    }
}
