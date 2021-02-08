//Deobfuscated with https://github.com/PetoPetko/Minecraft-Deobfuscator3000 using mappings "1.12 stable mappings"!

package me.earth.phobos.features.modules.player;

import me.earth.phobos.features.modules.*;
import me.earth.phobos.features.setting.*;
import me.earth.phobos.features.*;
import org.lwjgl.input.*;
import net.minecraft.util.math.*;
import net.minecraft.entity.player.*;
import net.minecraft.item.*;
import me.earth.phobos.util.*;
import net.minecraft.init.*;
import net.minecraft.util.*;
import net.minecraft.world.*;
import net.minecraft.entity.*;

public class MCP extends Module
{
    private Setting<Mode> mode;
    private Setting<Boolean> stopRotation;
    private Setting<Boolean> antiFriend;
    private Setting<Integer> rotation;
    private boolean clicked;
    
    public MCP() {
        super("MCP", "Throws a pearl", Category.PLAYER, false, false, false);
        this.mode = (Setting<Mode>)this.register(new Setting("Mode", Mode.MIDDLECLICK));
        this.stopRotation = (Setting<Boolean>)this.register(new Setting("Rotation", true));
        this.antiFriend = (Setting<Boolean>)this.register(new Setting("AntiFriend", true));
        this.rotation = (Setting<Integer>)this.register(new Setting("Delay", 10, 0, 100, v -> this.stopRotation.getValue()));
        this.clicked = false;
    }
    
    @Override
    public void onEnable() {
        if (!Feature.fullNullCheck() && this.mode.getValue() == Mode.TOGGLE) {
            this.throwPearl();
            this.disable();
        }
    }
    
    @Override
    public void onTick() {
        if (this.mode.getValue() == Mode.MIDDLECLICK) {
            if (Mouse.isButtonDown(2)) {
                if (!this.clicked) {
                    this.throwPearl();
                }
                this.clicked = true;
            }
            else {
                this.clicked = false;
            }
        }
    }
    
    private void throwPearl() {
        if (this.antiFriend.getValue()) {
            final RayTraceResult result = MCP.mc.objectMouseOver;
            if (result != null && result.typeOfHit == RayTraceResult.Type.ENTITY) {
                final Entity entity = result.entityHit;
                if (entity instanceof EntityPlayer) {
                    return;
                }
            }
        }
        final int pearlSlot = InventoryUtil.findHotbarBlock(ItemEnderPearl.class);
        final boolean offhand = MCP.mc.player.getHeldItemOffhand().getItem() == Items.ENDER_PEARL;
        if (pearlSlot != -1 || offhand) {
            final int oldslot = MCP.mc.player.inventory.currentItem;
            if (!offhand) {
                InventoryUtil.switchToHotbarSlot(pearlSlot, false);
            }
            MCP.mc.playerController.processRightClick((EntityPlayer)MCP.mc.player, (World)MCP.mc.world, offhand ? EnumHand.OFF_HAND : EnumHand.MAIN_HAND);
            if (!offhand) {
                InventoryUtil.switchToHotbarSlot(oldslot, false);
            }
        }
    }
    
    public enum Mode
    {
        TOGGLE, 
        MIDDLECLICK;
    }
}
