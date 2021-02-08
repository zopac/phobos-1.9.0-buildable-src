package me.earth.phobos.event.events;

import me.earth.phobos.event.*;
import net.minecraftforge.fml.common.eventhandler.*;

@Cancelable
public class UpdateWalkingPlayerEvent extends EventStage
{
    public UpdateWalkingPlayerEvent(final int stage) {
        super(stage);
    }
}
