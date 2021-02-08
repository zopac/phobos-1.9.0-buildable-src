package me.earth.phobos.features.modules.movement;

import me.earth.phobos.features.modules.*;
import me.earth.phobos.features.setting.*;
import net.minecraft.init.*;

public class IceSpeed extends Module
{
    private Setting<Float> speed;
    private static IceSpeed INSTANCE;
    
    public IceSpeed() {
        super("IceSpeed", "Speeds you up on ice.", Category.MOVEMENT, false, false, false);
        this.speed = (Setting<Float>)this.register(new Setting("Speed", 0.4f, 0.2f, 1.5f));
        IceSpeed.INSTANCE = this;
    }
    
    public static IceSpeed getINSTANCE() {
        if (IceSpeed.INSTANCE == null) {
            IceSpeed.INSTANCE = new IceSpeed();
        }
        return IceSpeed.INSTANCE;
    }
    
    @Override
    public void onUpdate() {
        Blocks.ICE.slipperiness = this.speed.getValue();
        Blocks.PACKED_ICE.slipperiness = this.speed.getValue();
        Blocks.FROSTED_ICE.slipperiness = this.speed.getValue();
    }
    
    @Override
    public void onDisable() {
        Blocks.ICE.slipperiness = 0.98f;
        Blocks.PACKED_ICE.slipperiness = 0.98f;
        Blocks.FROSTED_ICE.slipperiness = 0.98f;
    }
    
    static {
        IceSpeed.INSTANCE = new IceSpeed();
    }
}
