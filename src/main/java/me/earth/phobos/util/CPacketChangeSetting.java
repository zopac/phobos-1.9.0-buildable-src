package me.earth.phobos.util;

import net.minecraft.network.play.*;
import me.earth.phobos.features.modules.*;
import me.earth.phobos.features.setting.*;
import java.io.*;
import me.earth.phobos.*;
import net.minecraftforge.common.*;
import me.earth.phobos.event.events.*;
import net.minecraftforge.fml.common.eventhandler.*;
import net.minecraft.network.*;

public class CPacketChangeSetting implements Packet<INetHandlerPlayServer>
{
    public String setting;
    
    public CPacketChangeSetting(final String module, final String setting, final String value) {
        this.setting = setting + "-" + module + "-" + value;
    }
    
    public CPacketChangeSetting(final Module module, final Setting setting, final String value) {
        this.setting = setting.getName() + "-" + module.getName() + "-" + value;
    }
    
    public void readPacketData(final PacketBuffer buf) throws IOException {
        this.setting = buf.readString(256);
    }
    
    public void writePacketData(final PacketBuffer buf) throws IOException {
        buf.writeString(this.setting);
    }
    
    public void processPacket(final INetHandlerPlayServer handler) {
        final Module module = Phobos.moduleManager.getModuleByName(this.setting.split("-")[1]);
        final Setting setting1 = module.getSettingByName(this.setting.split("-")[0]);
        MinecraftForge.EVENT_BUS.post((Event)new ValueChangeEvent(setting1, this.setting.split("-")[2]));
    }
}
