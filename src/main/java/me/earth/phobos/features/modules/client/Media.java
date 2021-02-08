package me.earth.phobos.features.modules.client;

import me.earth.phobos.features.modules.*;
import me.earth.phobos.features.setting.*;
import me.earth.phobos.features.*;

public class Media extends Module
{
    public final Setting<Boolean> changeOwn;
    public final Setting<String> ownName;
    private static Media instance;
    
    public Media() {
        super("Media", "Helps with creating Media", Category.CLIENT, false, false, false);
        this.changeOwn = (Setting<Boolean>)this.register(new Setting("MyName", true));
        this.ownName = (Setting<String>)this.register(new Setting("Name", "Name here...", v -> this.changeOwn.getValue()));
        Media.instance = this;
    }
    
    public static Media getInstance() {
        if (Media.instance == null) {
            Media.instance = new Media();
        }
        return Media.instance;
    }
    
    public static String getPlayerName() {
        if (Feature.fullNullCheck() || !ServerModule.getInstance().isConnected()) {
            return Media.mc.getSession().getUsername();
        }
        final String name = ServerModule.getInstance().getPlayerName();
        if (name == null || name.isEmpty()) {
            return Media.mc.getSession().getUsername();
        }
        return name;
    }
}
