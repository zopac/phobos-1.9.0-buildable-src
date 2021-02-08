package me.earth.phobos.manager;

import me.earth.phobos.features.*;
import me.earth.phobos.features.modules.player.*;
import me.earth.phobos.*;

public class TimerManager extends Feature
{
    private float timer;
    private TimerSpeed module;
    
    public TimerManager() {
        this.timer = 1.0f;
    }
    
    public void init() {
        this.module = Phobos.moduleManager.getModuleByClass(TimerSpeed.class);
    }
    
    public void unload() {
        this.timer = 1.0f;
        TimerManager.mc.timer.tickLength = 50.0f;
    }
    
    public void update() {
        if (this.module != null && this.module.isEnabled()) {
            this.timer = this.module.speed;
        }
        TimerManager.mc.timer.tickLength = 50.0f / ((this.timer <= 0.0f) ? 0.1f : this.timer);
    }
    
    public void setTimer(final float timer) {
        if (timer > 0.0f) {
            this.timer = timer;
        }
    }
    
    public float getTimer() {
        return this.timer;
    }
    
    @Override
    public void reset() {
        this.timer = 1.0f;
    }
}
