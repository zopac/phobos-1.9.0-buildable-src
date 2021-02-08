package me.earth.phobos.features;

import me.earth.phobos.util.*;
import me.earth.phobos.features.setting.*;
import me.earth.phobos.manager.*;
import me.earth.phobos.*;
import me.earth.phobos.features.modules.*;
import me.earth.phobos.features.gui.*;
import java.util.*;

public class Feature implements Util
{
    public List<Setting> settings;
    public TextManager renderer;
    private String name;
    
    public Feature() {
        this.settings = new ArrayList<Setting>();
        this.renderer = Phobos.textManager;
    }
    
    public Feature(final String name) {
        this.settings = new ArrayList<Setting>();
        this.renderer = Phobos.textManager;
        this.name = name;
    }
    
    public static boolean nullCheck() {
        return Feature.mc.player == null;
    }
    
    public static boolean fullNullCheck() {
        return Feature.mc.player == null || Feature.mc.world == null;
    }
    
    public String getName() {
        return this.name;
    }
    
    public List<Setting> getSettings() {
        return this.settings;
    }
    
    public boolean hasSettings() {
        return !this.settings.isEmpty();
    }
    
    public boolean isEnabled() {
        return this instanceof Module && ((Module)this).isOn();
    }
    
    public boolean isDisabled() {
        return !this.isEnabled();
    }
    
    public Setting register(final Setting setting) {
        setting.setFeature(this);
        this.settings.add(setting);
        if (this instanceof Module && Feature.mc.currentScreen instanceof PhobosGui) {
            PhobosGui.getInstance().updateModule((Module)this);
        }
        return setting;
    }
    
    public void unregister(final Setting settingIn) {
        final List<Setting> removeList = new ArrayList<Setting>();
        for (final Setting setting : this.settings) {
            if (setting.equals(settingIn)) {
                removeList.add(setting);
            }
        }
        if (!removeList.isEmpty()) {
            this.settings.removeAll(removeList);
        }
        if (this instanceof Module && Feature.mc.currentScreen instanceof PhobosGui) {
            PhobosGui.getInstance().updateModule((Module)this);
        }
    }
    
    public Setting getSettingByName(final String name) {
        for (final Setting setting : this.settings) {
            if (setting.getName().equalsIgnoreCase(name)) {
                return setting;
            }
        }
        return null;
    }
    
    public void reset() {
        for (final Setting setting : this.settings) {
            setting.setValue(setting.getDefaultValue());
        }
    }
    
    public void clearSettings() {
        this.settings = new ArrayList<Setting>();
    }
}
