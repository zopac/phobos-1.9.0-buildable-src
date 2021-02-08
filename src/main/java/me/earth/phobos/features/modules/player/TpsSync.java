package me.earth.phobos.features.modules.player;

import me.earth.phobos.features.modules.*;
import me.earth.phobos.features.setting.*;

public class TpsSync extends Module
{
    public Setting<Boolean> mining;
    public Setting<Boolean> attack;
    private static TpsSync INSTANCE;
    
    public TpsSync() {
        super("TpsSync", "Syncs your client with the TPS.", Category.PLAYER, true, false, false);
        this.mining = (Setting<Boolean>)this.register(new Setting("Mining", true));
        this.attack = (Setting<Boolean>)this.register(new Setting("Attack", false));
        this.setInstance();
    }
    
    private void setInstance() {
        TpsSync.INSTANCE = this;
    }
    
    public static TpsSync getInstance() {
        if (TpsSync.INSTANCE == null) {
            TpsSync.INSTANCE = new TpsSync();
        }
        return TpsSync.INSTANCE;
    }
    
    static {
        TpsSync.INSTANCE = new TpsSync();
    }
}
