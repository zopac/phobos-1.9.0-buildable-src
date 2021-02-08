//Deobfuscated with https://github.com/PetoPetko/Minecraft-Deobfuscator3000 using mappings "1.12 stable mappings"!

package me.earth.phobos.features.notifications;

import me.earth.phobos.features.modules.client.*;
import me.earth.phobos.*;
import net.minecraft.client.*;
import net.minecraft.client.gui.*;
import me.earth.phobos.util.*;

public class Notifications
{
    private final String text;
    private final long disableTime;
    private final float width;
    private final Timer timer;
    
    public Notifications(final String text, final long disableTime) {
        this.timer = new Timer();
        this.text = text;
        this.disableTime = disableTime;
        this.width = (float)Phobos.moduleManager.getModuleByClass(HUD.class).renderer.getStringWidth(text);
        this.timer.reset();
    }
    
    public void onDraw(final int y) {
        final ScaledResolution scaledResolution = new ScaledResolution(Minecraft.instance);
        if (this.timer.passedMs(this.disableTime)) {
            Phobos.notificationManager.getNotifications().remove(this);
        }
        RenderUtil.drawRect(scaledResolution.getScaledWidth() - 4 - this.width, (float)y, (float)(scaledResolution.getScaledWidth() - 2), (float)(y + Phobos.moduleManager.getModuleByClass(HUD.class).renderer.getFontHeight() + 3), 1962934272);
        Phobos.moduleManager.getModuleByClass(HUD.class).renderer.drawString(this.text, scaledResolution.getScaledWidth() - this.width - 3.0f, (float)(y + 2), -1, true);
    }
}
