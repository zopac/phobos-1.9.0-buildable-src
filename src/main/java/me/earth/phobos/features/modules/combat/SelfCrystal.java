package me.earth.phobos.features.modules.combat;

import me.earth.phobos.features.modules.*;
import net.minecraft.entity.player.*;

public class SelfCrystal extends Module
{
    public SelfCrystal() {
        super("SelfCrystal", "Best module", Category.COMBAT, true, false, false);
    }
    
    @Override
    public void onTick() {
        if (AutoCrystal.getInstance().isEnabled()) {
            AutoCrystal.target = (EntityPlayer)SelfCrystal.mc.player;
        }
    }
}
