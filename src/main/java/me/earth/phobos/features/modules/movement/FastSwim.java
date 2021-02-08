package me.earth.phobos.features.modules.movement;

import me.earth.phobos.features.modules.*;
import me.earth.phobos.features.setting.*;
import me.earth.phobos.event.events.*;
import net.minecraftforge.fml.common.eventhandler.*;

public class FastSwim extends Module
{
    public Setting<Double> waterHorizontal;
    public Setting<Double> waterVertical;
    public Setting<Double> lavaHorizontal;
    public Setting<Double> lavaVertical;
    
    public FastSwim() {
        super("FastSwim", "Swim fast", Category.MOVEMENT, true, false, false);
        this.waterHorizontal = (Setting<Double>)this.register(new Setting("WaterHorizontal", 3.0, 1.0, 20.0));
        this.waterVertical = (Setting<Double>)this.register(new Setting("WaterVertical", 3.0, 1.0, 20.0));
        this.lavaHorizontal = (Setting<Double>)this.register(new Setting("LavaHorizontal", 4.0, 1.0, 20.0));
        this.lavaVertical = (Setting<Double>)this.register(new Setting("LavaVertical", 4.0, 1.0, 20.0));
    }
    
    @SubscribeEvent
    public void onMove(final MoveEvent event) {
        if (FastSwim.mc.player.isInLava() && !FastSwim.mc.player.onGround) {
            event.setX(event.getX() * this.lavaHorizontal.getValue());
            event.setZ(event.getZ() * this.lavaHorizontal.getValue());
            event.setY(event.getY() * this.lavaVertical.getValue());
        }
        else if (FastSwim.mc.player.isInWater() && !FastSwim.mc.player.onGround) {
            event.setX(event.getX() * this.waterHorizontal.getValue());
            event.setZ(event.getZ() * this.waterHorizontal.getValue());
            event.setY(event.getY() * this.waterVertical.getValue());
        }
    }
}
