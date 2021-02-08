//Deobfuscated with https://github.com/PetoPetko/Minecraft-Deobfuscator3000 using mappings "1.12 stable mappings"!

package me.earth.phobos.features.modules.client;

import me.earth.phobos.features.modules.*;
import me.earth.phobos.features.setting.*;
import java.util.concurrent.atomic.*;
import me.earth.phobos.event.events.*;
import net.minecraftforge.fml.common.eventhandler.*;
import net.minecraft.network.*;
import net.minecraft.network.play.client.*;
import me.earth.phobos.util.*;
import net.minecraft.network.play.server.*;
import net.minecraft.network.handshake.client.*;
import me.earth.phobos.mixin.mixins.accessors.*;
import java.util.*;
import me.earth.phobos.util.Timer;

public class ServerModule extends Module
{
    public Setting<String> ip;
    public Setting<String> port;
    public Setting<String> serverIP;
    public Setting<Boolean> noFML;
    public Setting<Boolean> getName;
    public Setting<Boolean> average;
    public Setting<Boolean> clear;
    public Setting<Boolean> oneWay;
    public Setting<Integer> delay;
    private static ServerModule instance;
    private final AtomicBoolean connected;
    private final Timer pingTimer;
    private long currentPing;
    private long serverPing;
    private StringBuffer name;
    private long averagePing;
    private final List<Long> pingList;
    private String serverPrefix;
    
    public ServerModule() {
        super("PingBypass", "Manages Phobos`s internal Server", Category.CLIENT, false, false, true);
        this.ip = (Setting<String>)this.register(new Setting("PhobosIP", "0.0.0.0.0"));
        this.port = (Setting<String>)this.register(new Setting<String>("Port", "0").setRenderName(true));
        this.serverIP = (Setting<String>)this.register(new Setting("ServerIP", "AnarchyHvH.eu"));
        this.noFML = (Setting<Boolean>)this.register(new Setting("RemoveFML", false));
        this.getName = (Setting<Boolean>)this.register(new Setting("GetName", false));
        this.average = (Setting<Boolean>)this.register(new Setting("Average", false));
        this.clear = (Setting<Boolean>)this.register(new Setting("ClearPings", false));
        this.oneWay = (Setting<Boolean>)this.register(new Setting("OneWay", false));
        this.delay = (Setting<Integer>)this.register(new Setting("KeepAlives", 10, 1, 50));
        this.connected = new AtomicBoolean(false);
        this.pingTimer = new Timer();
        this.currentPing = 0L;
        this.serverPing = 0L;
        this.name = null;
        this.averagePing = 0L;
        this.pingList = new ArrayList<Long>();
        this.serverPrefix = "idk";
        ServerModule.instance = this;
    }
    
    public String getPlayerName() {
        if (this.name == null) {
            return null;
        }
        return this.name.toString();
    }
    
    public String getServerPrefix() {
        return this.serverPrefix;
    }
    
    public static ServerModule getInstance() {
        if (ServerModule.instance == null) {
            ServerModule.instance = new ServerModule();
        }
        return ServerModule.instance;
    }
    
    @Override
    public void onLogout() {
        this.averagePing = 0L;
        this.currentPing = 0L;
        this.serverPing = 0L;
        this.pingList.clear();
        this.connected.set(false);
        this.name = null;
    }
    
    @SubscribeEvent
    public void onReceivePacket(final PacketEvent.Receive event) {
        if (event.getPacket() instanceof SPacketChat) {
            final SPacketChat packet = event.getPacket();
            if (packet.chatComponent.getUnformattedText().startsWith("@Clientprefix")) {
                final String prefix = packet.chatComponent.getFormattedText().replace("@Clientprefix", "");
                this.serverPrefix = prefix;
            }
        }
    }
    
    @Override
    public void onTick() {
        if (ServerModule.mc.getConnection() != null && this.isConnected()) {
            if (this.getName.getValue()) {
                ServerModule.mc.getConnection().sendPacket((Packet)new CPacketChatMessage("@Servername"));
                this.getName.setValue(false);
            }
            if (this.serverPrefix.equalsIgnoreCase("idk") && ServerModule.mc.world != null) {
                ServerModule.mc.getConnection().sendPacket((Packet)new CPacketChatMessage("@Servergetprefix"));
            }
            if (this.pingTimer.passedMs(this.delay.getValue() * 1000)) {
                ServerModule.mc.getConnection().sendPacket((Packet)new CPacketKeepAlive(100L));
                this.pingTimer.reset();
            }
            if (this.clear.getValue()) {
                this.pingList.clear();
            }
        }
    }
    
    @SubscribeEvent
    public void onPacketReceive(final PacketEvent.Receive event) {
        if (event.getPacket() instanceof SPacketChat) {
            final SPacketChat packetChat = event.getPacket();
            if (packetChat.getChatComponent().getFormattedText().startsWith("@Client")) {
                this.name = new StringBuffer(TextUtil.stripColor(packetChat.getChatComponent().getFormattedText().replace("@Client", "")));
                event.setCanceled(true);
            }
        }
        else if (event.getPacket() instanceof SPacketKeepAlive) {
            final SPacketKeepAlive alive = event.getPacket();
            if (alive.getId() > 0L && alive.getId() < 1000L) {
                this.serverPing = alive.getId();
                if (this.oneWay.getValue()) {
                    this.currentPing = this.pingTimer.getPassedTimeMs() / 2L;
                }
                else {
                    this.currentPing = this.pingTimer.getPassedTimeMs();
                }
                this.pingList.add(this.currentPing);
                this.averagePing = this.getAveragePing();
            }
        }
    }
    
    @SubscribeEvent
    public void onPacketSend(final PacketEvent.Send event) {
        if (event.getPacket() instanceof C00Handshake) {
            final IC00Handshake packet = event.getPacket();
            final String ip = packet.getIp();
            if (ip.equals(this.ip.getValue())) {
                packet.setIp(this.serverIP.getValue());
                System.out.println(packet.getIp());
                this.connected.set(true);
            }
        }
    }
    
    @Override
    public String getDisplayInfo() {
        return this.averagePing + "ms";
    }
    
    private long getAveragePing() {
        if (!this.average.getValue() || this.pingList.isEmpty()) {
            return this.currentPing;
        }
        int full = 0;
        for (final long i : this.pingList) {
            full += (int)i;
        }
        return full / this.pingList.size();
    }
    
    public boolean isConnected() {
        return this.connected.get();
    }
    
    public int getPort() {
        int result;
        try {
            result = Integer.parseInt(this.port.getValue());
        }
        catch (NumberFormatException e) {
            return -1;
        }
        return result;
    }
    
    public long getServerPing() {
        return this.serverPing;
    }
}
