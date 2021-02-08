//Deobfuscated with https://github.com/PetoPetko/Minecraft-Deobfuscator3000 using mappings "1.12 stable mappings"!

package me.earth.phobos.mixin.mixins;

import org.spongepowered.asm.mixin.*;
import net.minecraft.client.renderer.*;
import me.earth.phobos.features.modules.movement.*;
import net.minecraft.client.renderer.entity.*;
import net.minecraft.client.renderer.tileentity.*;
import net.minecraft.util.math.*;
import org.spongepowered.asm.mixin.injection.callback.*;
import me.earth.phobos.event.events.*;
import net.minecraftforge.common.*;
import net.minecraftforge.fml.common.eventhandler.*;
import org.spongepowered.asm.mixin.injection.*;

@Mixin({ RenderGlobal.class })
public abstract class MixinRenderGlobal
{
    @Redirect(method = { "setupTerrain" }, at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/ChunkRenderContainer;initialize(DDD)V"))
    public void initializeHook(final ChunkRenderContainer chunkRenderContainer, final double viewEntityXIn, final double viewEntityYIn, final double viewEntityZIn) {
        double y = viewEntityYIn;
        if (Speed.getInstance().isOn() && Speed.getInstance().noShake.getValue() && Speed.getInstance().mode.getValue() != Speed.Mode.INSTANT && Speed.getInstance().antiShake) {
            y = Speed.getInstance().startY;
        }
        chunkRenderContainer.initialize(viewEntityXIn, y, viewEntityZIn);
    }
    
    @Redirect(method = { "renderEntities" }, at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/entity/RenderManager;setRenderPosition(DDD)V"))
    public void setRenderPositionHook(final RenderManager renderManager, final double renderPosXIn, final double renderPosYIn, final double renderPosZIn) {
        double y = renderPosYIn;
        if (Speed.getInstance().isOn() && Speed.getInstance().noShake.getValue() && Speed.getInstance().mode.getValue() != Speed.Mode.INSTANT && Speed.getInstance().antiShake) {
            y = Speed.getInstance().startY;
        }
        renderManager.setRenderPosition(renderPosXIn, TileEntityRendererDispatcher.staticPlayerY = y, renderPosZIn);
    }
    
    @Redirect(method = { "drawSelectionBox" }, at = @At(value = "INVOKE", target = "Lnet/minecraft/util/math/AxisAlignedBB;offset(DDD)Lnet/minecraft/util/math/AxisAlignedBB;"))
    public AxisAlignedBB offsetHook(final AxisAlignedBB axisAlignedBB, final double x, final double y, final double z) {
        double yIn = y;
        if (Speed.getInstance().isOn() && Speed.getInstance().noShake.getValue() && Speed.getInstance().mode.getValue() != Speed.Mode.INSTANT && Speed.getInstance().antiShake) {
            yIn = Speed.getInstance().startY;
        }
        return axisAlignedBB.offset(x, y, z);
    }
    
    @Inject(method = { "sendBlockBreakProgress" }, at = { @At("HEAD") })
    public void sendBlockBreakProgress(final int breakerId, final BlockPos pos, final int progress, final CallbackInfo ci) {
        final BlockBreakingEvent event = new BlockBreakingEvent(pos, breakerId, progress);
        MinecraftForge.EVENT_BUS.post((Event)event);
    }
}
