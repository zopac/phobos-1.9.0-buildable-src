package me.earth.phobos.features.modules.movement;

import me.earth.phobos.features.modules.*;
import net.minecraftforge.client.event.*;
import net.minecraftforge.fml.common.eventhandler.*;

public class AutoWalk extends Module
{
    public AutoWalk() {
        super("AutoWalk", "Automatically walks in a straight line", Category.MOVEMENT, true, false, false);
    }
    
    @SubscribeEvent
    public void onUpdateInput(final InputUpdateEvent event) {
        event.getMovementInput().moveForward = 1.0f;
    }
}
