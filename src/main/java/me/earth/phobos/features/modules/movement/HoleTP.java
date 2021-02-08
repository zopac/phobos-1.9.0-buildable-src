//Deobfuscated with https://github.com/PetoPetko/Minecraft-Deobfuscator3000 using mappings "1.12 stable mappings"!

package me.earth.phobos.features.modules.movement;

import me.earth.phobos.features.modules.*;
import me.earth.phobos.event.events.*;
import me.earth.phobos.util.*;
import net.minecraft.block.material.*;
import net.minecraft.util.math.*;
import net.minecraft.network.play.client.*;
import net.minecraft.network.*;
import net.minecraftforge.fml.common.eventhandler.*;

public class HoleTP extends Module
{
    private static HoleTP INSTANCE;
    private final double[] oneblockPositions;
    private int packets;
    private boolean jumped;
    
    public HoleTP() {
        super("HoleTP", "Teleports you in a hole.", Category.MOVEMENT, true, false, false);
        this.oneblockPositions = new double[] { 0.42, 0.75 };
        this.jumped = false;
        this.setInstance();
    }
    
    private void setInstance() {
        HoleTP.INSTANCE = this;
    }
    
    public static HoleTP getInstance() {
        if (HoleTP.INSTANCE == null) {
            HoleTP.INSTANCE = new HoleTP();
        }
        return HoleTP.INSTANCE;
    }
    
    @SubscribeEvent
    public void onUpdateWalkingPlayer(final UpdateWalkingPlayerEvent event) {
        if (event.getStage() == 1 && (Speed.getInstance().isOff() || Speed.getInstance().mode.getValue() == Speed.Mode.INSTANT) && Strafe.getInstance().isOff() && LagBlock.getInstance().isOff()) {
            if (!HoleTP.mc.player.onGround) {
                if (HoleTP.mc.gameSettings.keyBindJump.isKeyDown()) {
                    this.jumped = true;
                }
            }
            else {
                this.jumped = false;
            }
            if (!this.jumped && HoleTP.mc.player.fallDistance < 0.5 && BlockUtil.isInHole() && HoleTP.mc.player.posY - BlockUtil.getNearestBlockBelow() <= 1.125 && HoleTP.mc.player.posY - BlockUtil.getNearestBlockBelow() <= 0.95 && !EntityUtil.isOnLiquid() && !EntityUtil.isInLiquid()) {
                if (!HoleTP.mc.player.onGround) {
                    ++this.packets;
                }
                if (!HoleTP.mc.player.onGround && !HoleTP.mc.player.isInsideOfMaterial(Material.WATER) && !HoleTP.mc.player.isInsideOfMaterial(Material.LAVA) && !HoleTP.mc.gameSettings.keyBindJump.isKeyDown() && !HoleTP.mc.player.isOnLadder() && this.packets > 0) {
                    final BlockPos blockPos = new BlockPos(HoleTP.mc.player.posX, HoleTP.mc.player.posY, HoleTP.mc.player.posZ);
                    for (final double position : this.oneblockPositions) {
                        HoleTP.mc.player.connection.sendPacket((Packet)new CPacketPlayer.Position((double)(blockPos.getX() + 0.5f), HoleTP.mc.player.posY - position, (double)(blockPos.getZ() + 0.5f), true));
                    }
                    HoleTP.mc.player.setPosition((double)(blockPos.getX() + 0.5f), BlockUtil.getNearestBlockBelow() + 0.1, (double)(blockPos.getZ() + 0.5f));
                    this.packets = 0;
                }
            }
        }
    }
    
    static {
        HoleTP.INSTANCE = new HoleTP();
    }
}
