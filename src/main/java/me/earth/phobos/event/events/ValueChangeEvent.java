package me.earth.phobos.event.events;

import me.earth.phobos.event.*;
import me.earth.phobos.features.setting.*;

public class ValueChangeEvent extends EventStage
{
    public Setting setting;
    public Object value;
    
    public ValueChangeEvent(final Setting setting, final Object value) {
        this.setting = setting;
        this.value = value;
    }
}
