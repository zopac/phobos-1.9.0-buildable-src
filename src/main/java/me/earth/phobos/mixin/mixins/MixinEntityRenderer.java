//Deobfuscated with https://github.com/PetoPetko/Minecraft-Deobfuscator3000 using mappings "1.12 stable mappings"!

package me.earth.phobos.mixin.mixins;

import net.minecraft.client.*;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.callback.*;
import me.earth.phobos.features.modules.client.*;
import net.minecraft.client.entity.*;
import net.minecraft.world.*;
import net.minecraft.entity.*;
import net.minecraft.block.state.*;
import net.minecraft.init.*;
import net.minecraft.client.renderer.*;
import net.minecraft.client.multiplayer.*;
import javax.annotation.*;
import net.minecraft.util.math.*;
import com.google.common.base.*;
import me.earth.phobos.features.modules.player.*;
import net.minecraft.item.*;
import java.util.*;
import me.earth.phobos.features.modules.render.*;
import org.spongepowered.asm.mixin.injection.*;

@Mixin({ EntityRenderer.class })
public abstract class MixinEntityRenderer
{
    private boolean injection;
    @Shadow
    public ItemStack itemActivationItem;
    @Shadow
    @Final
    public Minecraft mc;
    
    public MixinEntityRenderer() {
        this.injection = true;
    }
    
    @Shadow
    public abstract void getMouseOver(final float p0);
    
    @Inject(method = { "renderItemActivation" }, at = { @At("HEAD") }, cancellable = true)
    public void renderItemActivationHook(final CallbackInfo info) {
        if (this.itemActivationItem != null && NoRender.getInstance().isOn() && NoRender.getInstance().totemPops.getValue() && this.itemActivationItem.getItem() == Items.TOTEM_OF_UNDYING) {
            info.cancel();
        }
    }
    
    @Inject(method = { "updateLightmap" }, at = { @At("HEAD") }, cancellable = true)
    private void updateLightmap(final float partialTicks, final CallbackInfo info) {
        if (NoRender.getInstance().isOn() && (NoRender.getInstance().skylight.getValue() == NoRender.Skylight.ENTITY || NoRender.getInstance().skylight.getValue() == NoRender.Skylight.ALL)) {
            info.cancel();
        }
    }
    
    @Inject(method = { "getMouseOver(F)V" }, at = { @At("HEAD") }, cancellable = true)
    public void getMouseOverHook(final float partialTicks, final CallbackInfo info) {
        if (this.injection) {
            info.cancel();
            this.injection = false;
            try {
                this.getMouseOver(partialTicks);
            }
            catch (Exception e) {
                e.printStackTrace();
                if (Notifications.getInstance().isOn() && Notifications.getInstance().crash.getValue()) {
                    Notifications.displayCrash(e);
                }
            }
            this.injection = true;
        }
    }
    
    @Redirect(method = { "setupCameraTransform" }, at = @At(value = "FIELD", target = "Lnet/minecraft/client/entity/EntityPlayerSP;prevTimeInPortal:F"))
    public float prevTimeInPortalHook(final EntityPlayerSP entityPlayerSP) {
        if (NoRender.getInstance().isOn() && NoRender.getInstance().nausea.getValue()) {
            return -3.4028235E38f;
        }
        return entityPlayerSP.prevTimeInPortal;
    }
    
    @Inject(method = { "setupFog" }, at = { @At("HEAD") }, cancellable = true)
    public void setupFogHook(final int startCoords, final float partialTicks, final CallbackInfo info) {
        if (NoRender.getInstance().isOn() && NoRender.getInstance().fog.getValue() == NoRender.Fog.NOFOG) {
            info.cancel();
        }
    }
    
    @Redirect(method = { "setupFog" }, at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/ActiveRenderInfo;getBlockStateAtEntityViewpoint(Lnet/minecraft/world/World;Lnet/minecraft/entity/Entity;F)Lnet/minecraft/block/state/IBlockState;"))
    public IBlockState getBlockStateAtEntityViewpointHook(final World worldIn, final Entity entityIn, final float p_186703_2_) {
        if (NoRender.getInstance().isOn() && NoRender.getInstance().fog.getValue() == NoRender.Fog.AIR) {
            return Blocks.AIR.defaultBlockState;
        }
        return ActiveRenderInfo.getBlockStateAtEntityViewpoint(worldIn, entityIn, p_186703_2_);
    }
    
    @Inject(method = { "hurtCameraEffect" }, at = { @At("HEAD") }, cancellable = true)
    public void hurtCameraEffectHook(final float ticks, final CallbackInfo info) {
        if (NoRender.getInstance().isOn() && NoRender.getInstance().hurtcam.getValue()) {
            info.cancel();
        }
    }

    @Redirect(method={"getMouseOver"}, at=@At(value="INVOKE", target="Lnet/minecraft/client/multiplayer/WorldClient;getEntitiesInAABBexcluding(Lnet/minecraft/entity/Entity;Lnet/minecraft/util/math/AxisAlignedBB;Lcom/google/common/base/Predicate;)Ljava/util/List;"))
    public List<Entity> getEntitiesInAABBexcludingHook(WorldClient worldClient, @Nullable Entity entityIn, AxisAlignedBB boundingBox, @Nullable Predicate<? super Entity> predicate) {
        if (Speedmine.getInstance().isOn() && Speedmine.getInstance().noTrace.getValue().booleanValue() && (!Speedmine.getInstance().pickaxe.getValue().booleanValue() || this.mc.player.getHeldItemMainhand().getItem() instanceof ItemPickaxe)) {
            return new ArrayList<Entity>();
        }
        return worldClient.getEntitiesInAABBexcluding(entityIn, boundingBox, predicate);
    }
    
    @ModifyVariable(method = { "orientCamera" }, ordinal = 3, at = @At(value = "STORE", ordinal = 0), require = 1)
    public double changeCameraDistanceHook(final double range) {
        return (CameraClip.getInstance().isEnabled() && CameraClip.getInstance().extend.getValue()) ? CameraClip.getInstance().distance.getValue() : range;
    }
    
    @ModifyVariable(method = { "orientCamera" }, ordinal = 7, at = @At(value = "STORE", ordinal = 0), require = 1)
    public double orientCameraHook(final double range) {
        return (CameraClip.getInstance().isEnabled() && CameraClip.getInstance().extend.getValue()) ? CameraClip.getInstance().distance.getValue() : ((CameraClip.getInstance().isEnabled() && !CameraClip.getInstance().extend.getValue()) ? 4.0 : range);
    }
}
