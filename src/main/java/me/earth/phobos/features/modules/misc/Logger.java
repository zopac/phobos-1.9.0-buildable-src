package me.earth.phobos.features.modules.misc;

import me.earth.phobos.features.modules.*;
import me.earth.phobos.features.setting.*;
import me.earth.phobos.event.events.*;
import net.minecraft.client.gui.*;
import me.earth.phobos.features.command.*;
import net.minecraftforge.fml.common.eventhandler.*;
import net.minecraft.network.*;
import net.minecraft.util.*;
import java.lang.reflect.*;

public class Logger extends Module
{
    public Setting<Packets> packets;
    public Setting<Boolean> chat;
    public Setting<Boolean> fullInfo;
    public Setting<Boolean> noPing;
    
    public Logger() {
        super("Logger", "Logs stuff", Category.MISC, true, false, false);
        this.packets = (Setting<Packets>)this.register(new Setting("Packets", Packets.OUTGOING));
        this.chat = (Setting<Boolean>)this.register(new Setting("Chat", false));
        this.fullInfo = (Setting<Boolean>)this.register(new Setting("FullInfo", false));
        this.noPing = (Setting<Boolean>)this.register(new Setting("NoPing", false));
    }
    
    @SubscribeEvent(receiveCanceled = true)
    public void onPacketSend(final PacketEvent.Send event) {
        if (this.noPing.getValue() && Logger.mc.currentScreen instanceof GuiMultiplayer) {
            return;
        }
        if (this.packets.getValue() == Packets.OUTGOING || this.packets.getValue() == Packets.ALL) {
            if (this.chat.getValue()) {
                Command.sendMessage(event.getPacket().toString());
            }
            else {
                this.writePacketOnConsole(event.getPacket(), false);
            }
        }
    }
    
    @SubscribeEvent(receiveCanceled = true)
    public void onPacketReceive(final PacketEvent.Receive event) {
        if (this.noPing.getValue() && Logger.mc.currentScreen instanceof GuiMultiplayer) {
            return;
        }
        if (this.packets.getValue() == Packets.INCOMING || this.packets.getValue() == Packets.ALL) {
            if (this.chat.getValue()) {
                Command.sendMessage(event.getPacket().toString());
            }
            else {
                this.writePacketOnConsole(event.getPacket(), true);
            }
        }
    }
    
    private void writePacketOnConsole(final Packet<?> packet, final boolean in) {
        if (this.fullInfo.getValue()) {
            System.out.println((in ? "In: " : "Send: ") + packet.getClass().getSimpleName() + " {");
            try {
                for (Class clazz = packet.getClass(); clazz != Object.class; clazz = clazz.getSuperclass()) {
                    for (final Field field : clazz.getDeclaredFields()) {
                        if (field != null) {
                            if (!field.isAccessible()) {
                                field.setAccessible(true);
                            }
                            System.out.println(StringUtils.stripControlCodes("      " + field.getType().getSimpleName() + " " + field.getName() + " : " + field.get(packet)));
                        }
                    }
                }
            }
            catch (Exception e) {
                e.printStackTrace();
            }
            System.out.println("}");
        }
        else {
            System.out.println(packet.toString());
        }
    }
    
    public enum Packets
    {
        NONE, 
        INCOMING, 
        OUTGOING, 
        ALL;
    }
}
