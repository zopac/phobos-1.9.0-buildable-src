package me.earth.phobos.features.modules;

import me.earth.phobos.features.*;
import me.earth.phobos.features.setting.*;
import net.minecraftforge.common.*;
import me.earth.phobos.event.events.*;
import net.minecraftforge.fml.common.eventhandler.*;
import me.earth.phobos.*;
import me.earth.phobos.features.command.*;
import me.earth.phobos.features.modules.client.*;
import me.earth.phobos.util.*;
import java.util.concurrent.*;

public class Module extends Feature
{
    private final String description;
    private final Category category;
    public Setting<Boolean> enabled;
    public Setting<Boolean> drawn;
    public Setting<Bind> bind;
    public Setting<String> displayName;
    public boolean hasListener;
    public boolean alwaysListening;
    public boolean hidden;
    public float arrayListOffset;
    public float arrayListVOffset;
    public float offset;
    public float vOffset;
    public boolean sliding;
    public Animation animation;
    
    public Module(final String name, final String description, final Category category, final boolean hasListener, final boolean hidden, final boolean alwaysListening) {
        super(name);
        this.enabled = (Setting<Boolean>)this.register(new Setting("Enabled", false));
        this.drawn = (Setting<Boolean>)this.register(new Setting("Drawn", true));
        this.bind = (Setting<Bind>)this.register(new Setting("Bind", new Bind(-1)));
        this.arrayListOffset = 0.0f;
        this.arrayListVOffset = 0.0f;
        this.displayName = (Setting<String>)this.register(new Setting("DisplayName", name));
        this.description = description;
        this.category = category;
        this.hasListener = hasListener;
        this.hidden = hidden;
        this.alwaysListening = alwaysListening;
        this.animation = new Animation(this);
    }
    
    public void onEnable() {
    }
    
    public void onDisable() {
    }
    
    public void onToggle() {
    }
    
    public void onLoad() {
    }
    
    public void onTick() {
    }
    
    public void onLogin() {
    }
    
    public void onLogout() {
    }
    
    public void onUpdate() {
    }
    
    public void onRender2D(final Render2DEvent event) {
    }
    
    public void onRender3D(final Render3DEvent event) {
    }
    
    public void onUnload() {
    }
    
    public String getDisplayInfo() {
        return null;
    }
    
    public boolean isOn() {
        return this.enabled.getValue();
    }
    
    public boolean isOff() {
        return !this.enabled.getValue();
    }
    
    public void setEnabled(final boolean enabled) {
        if (enabled) {
            this.enable();
        }
        else {
            this.disable();
        }
    }
    
    public void enable() {
        this.enabled.setValue(true);
        this.onToggle();
        this.onEnable();
        if (this.isOn() && this.hasListener && !this.alwaysListening) {
            MinecraftForge.EVENT_BUS.register((Object)this);
        }
    }
    
    public void disable() {
        if (this.hasListener && !this.alwaysListening) {
            MinecraftForge.EVENT_BUS.unregister((Object)this);
        }
        this.enabled.setValue(false);
        this.onToggle();
        this.onDisable();
    }
    
    public void toggle() {
        final ClientEvent event = new ClientEvent(this.isEnabled() ? 0 : 1, this);
        MinecraftForge.EVENT_BUS.post((Event)event);
        if (!event.isCanceled()) {
            this.setEnabled(!this.isEnabled());
        }
    }
    
    public String getDisplayName() {
        return this.displayName.getValue();
    }
    
    public String getDescription() {
        return this.description;
    }
    
    public void setDisplayName(final String name) {
        final Module module = Phobos.moduleManager.getModuleByDisplayName(name);
        final Module originalModule = Phobos.moduleManager.getModuleByName(name);
        if (module == null && originalModule == null) {
            Command.sendMessage(this.getDisplayName() + ", Original name: " + this.getName() + ", has been renamed to: " + name);
            this.displayName.setValue(name);
            return;
        }
        Command.sendMessage("�cA module of this name already exists.");
    }
    
    public boolean isSliding() {
        return this.sliding;
    }
    
    public boolean isDrawn() {
        return this.drawn.getValue();
    }
    
    public void setDrawn(final boolean drawn) {
        this.drawn.setValue(drawn);
    }
    
    public Category getCategory() {
        return this.category;
    }
    
    public String getInfo() {
        return null;
    }
    
    public Bind getBind() {
        return this.bind.getValue();
    }
    
    public void setBind(final int key) {
        this.bind.setValue(new Bind(key));
    }
    
    public boolean listening() {
        return (this.hasListener && this.isOn()) || this.alwaysListening;
    }
    
    public String getFullArrayString() {
        return this.getDisplayName() + "�8" + ((this.getDisplayInfo() != null) ? (" [�r" + this.getDisplayInfo() + "�8" + "]") : "");
    }
    
    public enum Category
    {
        COMBAT("Combat"), 
        MISC("Misc"), 
        RENDER("Render"), 
        MOVEMENT("Movement"), 
        PLAYER("Player"), 
        CLIENT("Client");
        
        private final String name;
        
        private Category(final String name) {
            this.name = name;
        }
        
        public String getName() {
            return this.name;
        }
    }
    
    public class Animation extends Thread
    {
        ScheduledExecutorService service;
        public Module module;
        public float offset;
        public float vOffset;
        public String lastText;
        public boolean shouldMetaSlide;
        
        public Animation(final Module module) {
            super("Animation");
            this.service = Executors.newSingleThreadScheduledExecutor();
            this.module = module;
        }
        
        @Override
        public void run() {
            final String text = this.module.getDisplayName() + "�7" + ((this.module.getDisplayInfo() != null) ? (" [�f" + this.module.getDisplayInfo() + "�7" + "]") : "");
            this.module.offset = Module.this.renderer.getStringWidth(text) / (float)HUD.getInstance().animationHorizontalTime.getValue();
            this.module.vOffset = Module.this.renderer.getFontHeight() / (float)HUD.getInstance().animationVerticalTime.getValue();
            if (this.module.isEnabled() && HUD.getInstance().animationHorizontalTime.getValue() != 1) {
                if (this.module.arrayListOffset > this.module.offset && Util.mc.world != null) {
                    final Module module = this.module;
                    module.arrayListOffset -= this.module.offset;
                    this.module.sliding = true;
                }
            }
            else if (this.module.isDisabled() && HUD.getInstance().animationHorizontalTime.getValue() != 1) {
                if (this.module.arrayListOffset < Module.this.renderer.getStringWidth(text) && Util.mc.world != null) {
                    final Module module2 = this.module;
                    module2.arrayListOffset += this.module.offset;
                    this.module.sliding = true;
                }
                else {
                    this.module.sliding = false;
                }
            }
        }
        
        @Override
        public void start() {
            System.out.println("Starting animation thread for " + this.module.getName());
            this.service.scheduleAtFixedRate(this, 0L, 1L, TimeUnit.MILLISECONDS);
        }
    }
}
