//Deobfuscated with https://github.com/PetoPetko/Minecraft-Deobfuscator3000 using mappings "1.12 stable mappings"!

package me.earth.phobos.features.modules.movement;

import me.earth.phobos.features.modules.*;
import me.earth.phobos.features.setting.*;
import me.earth.phobos.util.*;

public class VanillaSpeed extends Module
{
    public Setting<Double> speed;
    
    public VanillaSpeed() {
        super("VanillaSpeed", "ec.me", Category.MOVEMENT, true, false, false);
        this.speed = (Setting<Double>)this.register(new Setting("Speed", 1.0, 1.0, 10.0));
    }
    
    @Override
    public void onUpdate() {
        if (VanillaSpeed.mc.player == null || VanillaSpeed.mc.world == null) {
            return;
        }
        final double[] calc = MathUtil.directionSpeed(this.speed.getValue() / 10.0);
        VanillaSpeed.mc.player.motionX = calc[0];
        VanillaSpeed.mc.player.motionZ = calc[1];
    }
}
