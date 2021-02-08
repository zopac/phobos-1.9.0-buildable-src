//Deobfuscated with https://github.com/PetoPetko/Minecraft-Deobfuscator3000 using mappings "1.12 stable mappings"!

package me.earth.phobos.features.modules.misc;

import me.earth.phobos.features.modules.*;
import me.earth.phobos.features.setting.*;
import net.minecraft.client.gui.inventory.*;
import org.lwjgl.input.*;
import me.earth.phobos.util.*;
import net.minecraft.inventory.*;

public class KitDelete extends Module
{
    private Setting<Bind> deleteKey;
    private boolean keyDown;
    
    public KitDelete() {
        super("KitDelete", "Automates /deleteukit", Category.MISC, false, false, false);
        this.deleteKey = (Setting<Bind>)this.register(new Setting("Key", new Bind(-1)));
    }
    
    @Override
    public void onTick() {
        if (this.deleteKey.getValue().getKey() != -1) {
            if (KitDelete.mc.currentScreen instanceof GuiContainer && Keyboard.isKeyDown(this.deleteKey.getValue().getKey())) {
                final Slot slot = ((GuiContainer)KitDelete.mc.currentScreen).getSlotUnderMouse();
                if (slot != null && !this.keyDown) {
                    KitDelete.mc.player.sendChatMessage("/deleteukit " + TextUtil.stripColor(slot.getStack().getDisplayName()));
                    this.keyDown = true;
                }
            }
            else if (this.keyDown) {
                this.keyDown = false;
            }
        }
    }
}
