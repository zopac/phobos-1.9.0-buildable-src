package me.earth.phobos.features.modules.misc;

import me.earth.phobos.features.modules.*;
import me.earth.phobos.features.setting.*;
import net.minecraftforge.client.event.*;
import net.minecraft.client.gui.*;
import me.earth.phobos.features.command.*;
import net.minecraftforge.fml.common.eventhandler.*;

public class AutoRespawn extends Module
{
    public Setting<Boolean> antiDeathScreen;
    public Setting<Boolean> deathCoords;
    public Setting<Boolean> respawn;
    
    public AutoRespawn() {
        super("AutoRespawn", "Respawns you when you die.", Category.MISC, true, false, false);
        this.antiDeathScreen = (Setting<Boolean>)this.register(new Setting("AntiDeathScreen", true));
        this.deathCoords = (Setting<Boolean>)this.register(new Setting("DeathCoords", false));
        this.respawn = (Setting<Boolean>)this.register(new Setting("Respawn", true));
    }
    
    @SubscribeEvent
    public void onDisplayDeathScreen(final GuiOpenEvent event) {
        if (event.getGui() instanceof GuiGameOver) {
            if (this.deathCoords.getValue() && event.getGui() instanceof GuiGameOver) {
                Command.sendMessage(String.format("You died at x %d y %d z %d", (int)AutoRespawn.mc.player.posX, (int)AutoRespawn.mc.player.posY, (int)AutoRespawn.mc.player.posZ));
            }
            if ((this.respawn.getValue() && AutoRespawn.mc.player.getHealth() <= 0.0f) || (this.antiDeathScreen.getValue() && AutoRespawn.mc.player.getHealth() > 0.0f)) {
                event.setCanceled(true);
                AutoRespawn.mc.player.respawnPlayer();
            }
        }
    }
}
