package me.earth.phobos.features.modules.player;

import me.earth.phobos.features.modules.*;

public class LiquidInteract extends Module
{
    private static LiquidInteract INSTANCE;
    
    public LiquidInteract() {
        super("LiquidInteract", "Interact with liquids", Category.PLAYER, false, false, false);
        this.setInstance();
    }
    
    private void setInstance() {
        LiquidInteract.INSTANCE = this;
    }
    
    public static LiquidInteract getInstance() {
        if (LiquidInteract.INSTANCE == null) {
            LiquidInteract.INSTANCE = new LiquidInteract();
        }
        return LiquidInteract.INSTANCE;
    }
    
    static {
        LiquidInteract.INSTANCE = new LiquidInteract();
    }
}
