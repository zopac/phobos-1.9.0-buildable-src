package me.earth.phobos.mixin.mixins;

import org.spongepowered.asm.mixin.*;
import net.minecraft.client.network.*;
import net.minecraft.network.play.server.*;
import org.spongepowered.asm.mixin.injection.callback.*;
import me.earth.phobos.util.*;
import net.minecraft.entity.player.*;
import net.minecraftforge.common.*;
import me.earth.phobos.event.events.*;
import net.minecraftforge.fml.common.eventhandler.*;
import me.earth.phobos.*;
import net.minecraft.entity.*;
import org.spongepowered.asm.mixin.injection.*;

@Mixin({ NetHandlerPlayClient.class })
public class MixinNetHandlerPlayClient
{
    @Inject(method = { "handleEntityMetadata" }, at = { @At("RETURN") }, cancellable = true)
    private void handleEntityMetadataHook(final SPacketEntityMetadata packetIn, final CallbackInfo info) {
        if (Util.mc.world != null) {
            final Entity entity = Util.mc.world.getEntityByID(packetIn.getEntityId());
            if (entity instanceof EntityPlayer) {
                final EntityPlayer player = (EntityPlayer)entity;
                if (player.getHealth() <= 0.0f) {
                    MinecraftForge.EVENT_BUS.post((Event)new DeathEvent(player));
                    if (Phobos.totemPopManager != null) {
                        Phobos.totemPopManager.onDeath(player);
                    }
                }
            }
        }
    }
}
