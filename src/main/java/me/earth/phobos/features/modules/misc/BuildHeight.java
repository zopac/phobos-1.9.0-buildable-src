//Deobfuscated with https://github.com/PetoPetko/Minecraft-Deobfuscator3000 using mappings "1.12 stable mappings"!

package me.earth.phobos.features.modules.misc;

import me.earth.phobos.features.modules.*;
import me.earth.phobos.features.setting.*;
import me.earth.phobos.event.events.*;
import net.minecraft.network.play.client.*;
import net.minecraft.util.*;
import net.minecraftforge.fml.common.eventhandler.*;

public class BuildHeight extends Module
{
    private Setting<Integer> height;
    
    public BuildHeight() {
        super("BuildHeight", "Allows you to place at build height", Category.MISC, true, false, false);
        this.height = (Setting<Integer>)this.register(new Setting("Height", 255, 0, 255));
    }
    
    @SubscribeEvent
    public void onPacketSend(final PacketEvent.Send event) {
        if (event.getStage() == 0 && event.getPacket() instanceof CPacketPlayerTryUseItemOnBlock) {
            final CPacketPlayerTryUseItemOnBlock packet = event.getPacket();
            if (packet.getPos().getY() >= this.height.getValue() && packet.getDirection() == EnumFacing.UP) {
                packet.placedBlockDirection = EnumFacing.DOWN;
            }
        }
    }
}
