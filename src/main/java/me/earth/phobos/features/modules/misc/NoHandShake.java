//Deobfuscated with https://github.com/PetoPetko/Minecraft-Deobfuscator3000 using mappings "1.12 stable mappings"!

package me.earth.phobos.features.modules.misc;

import me.earth.phobos.features.modules.*;
import me.earth.phobos.event.events.*;
import net.minecraftforge.fml.common.network.internal.*;
import net.minecraft.network.play.client.*;
import io.netty.buffer.*;
import net.minecraft.network.*;
import net.minecraftforge.fml.common.eventhandler.*;

public class NoHandShake extends Module
{
    public NoHandShake() {
        super("NoHandshake", "Doesnt send your modlist to the server.", Category.MISC, true, false, false);
    }
    
    @SubscribeEvent
    public void onPacketSend(final PacketEvent.Send event) {
        if (event.getPacket() instanceof FMLProxyPacket && !NoHandShake.mc.isSingleplayer()) {
            event.setCanceled(true);
        }
        if (event.getPacket() instanceof CPacketCustomPayload) {
            final CPacketCustomPayload packet = event.getPacket();
            if (packet.getChannelName().equals("MC|Brand")) {
                packet.data = new PacketBuffer(Unpooled.buffer()).writeString("vanilla");
            }
        }
    }
}
