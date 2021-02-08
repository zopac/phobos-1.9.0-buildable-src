package me.earth.phobos.event.events;

import me.earth.phobos.event.*;

public class KeyEvent extends EventStage
{
    public boolean info;
    public boolean pressed;
    
    public KeyEvent(final int stage, final boolean info, final boolean pressed) {
        super(stage);
        this.info = info;
        this.pressed = pressed;
    }
}
