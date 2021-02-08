package me.earth.phobos.mixin.mixins;

import org.spongepowered.asm.mixin.*;
import net.minecraft.network.handshake.client.*;
import net.minecraft.network.*;
import me.earth.phobos.features.modules.client.*;
import org.spongepowered.asm.mixin.injection.*;

@Mixin({ C00Handshake.class })
public abstract class MixinC00Handshake
{
    @Redirect(method = { "writePacketData" }, at = @At(value = "INVOKE", target = "Lnet/minecraft/network/PacketBuffer;writeString(Ljava/lang/String;)Lnet/minecraft/network/PacketBuffer;"))
    public PacketBuffer writePacketDataHook(final PacketBuffer packetBuffer, final String string) {
        if (ServerModule.getInstance().noFML.getValue()) {
            final String ipNoFML = string.substring(0, string.length() - "\u0000FML\u0000".length());
            return packetBuffer.writeString(ipNoFML);
        }
        return packetBuffer.writeString(string);
    }
}
