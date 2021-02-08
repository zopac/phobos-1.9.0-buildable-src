package me.earth.phobos.features.modules.player;

import me.earth.phobos.features.modules.*;
import me.earth.phobos.features.setting.*;
import me.earth.phobos.event.events.*;
import net.minecraft.network.play.client.*;
import net.minecraftforge.fml.common.eventhandler.*;

public class NoHunger extends Module
{
    public Setting<Boolean> cancelSprint;
    
    public NoHunger() {
        super("NoHunger", "Prevents you from getting Hungry", Category.PLAYER, true, false, false);
        this.cancelSprint = (Setting<Boolean>)this.register(new Setting("CancelSprint", true));
    }
    
    @SubscribeEvent
    public void onPacketSend(final PacketEvent.Send event) {
        if (event.getPacket() instanceof CPacketPlayer) {
            final CPacketPlayer packet = event.getPacket();
            packet.onGround = (NoHunger.mc.player.fallDistance >= 0.0f || NoHunger.mc.playerController.isHittingBlock);
        }
        if (this.cancelSprint.getValue() && event.getPacket() instanceof CPacketEntityAction) {
            final CPacketEntityAction packet2 = event.getPacket();
            if (packet2.getAction() == CPacketEntityAction.Action.START_SPRINTING || packet2.getAction() == CPacketEntityAction.Action.STOP_SPRINTING) {
                event.setCanceled(true);
            }
        }
    }
}
