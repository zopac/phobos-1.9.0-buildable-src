package me.earth.phobos.event.events;

import me.earth.phobos.event.*;

public class Render3DEvent extends EventStage
{
    private float partialTicks;
    
    public Render3DEvent(final float partialTicks) {
        this.partialTicks = partialTicks;
    }
    
    public float getPartialTicks() {
        return this.partialTicks;
    }
}
