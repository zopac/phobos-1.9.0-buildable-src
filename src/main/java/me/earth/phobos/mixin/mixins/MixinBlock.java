//Deobfuscated with https://github.com/PetoPetko/Minecraft-Deobfuscator3000 using mappings "1.12 stable mappings"!

package me.earth.phobos.mixin.mixins;

import net.minecraft.block.state.*;
import net.minecraft.world.*;
import org.spongepowered.asm.mixin.*;
import net.minecraft.util.math.*;
import java.util.*;
import net.minecraft.entity.*;
import javax.annotation.*;
import me.earth.phobos.features.modules.movement.*;
import me.earth.phobos.features.modules.player.*;
import net.minecraft.block.*;
import net.minecraft.entity.item.*;
import me.earth.phobos.util.*;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.*;
import me.earth.phobos.features.modules.render.*;

@Mixin({ Block.class })
public abstract class MixinBlock
{
    @Shadow
    @Deprecated
    public abstract float getBlockHardness(final IBlockState p0, final World p1, final BlockPos p2);
    
    @Inject(method = { "addCollisionBoxToList(Lnet/minecraft/block/state/IBlockState;Lnet/minecraft/world/World;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/util/math/AxisAlignedBB;Ljava/util/List;Lnet/minecraft/entity/Entity;Z)V" }, at = { @At("HEAD") }, cancellable = true)
    public void addCollisionBoxToListHook(final IBlockState state, final World worldIn, final BlockPos pos, final AxisAlignedBB entityBox, final List<AxisAlignedBB> collidingBoxes, @Nullable final Entity entityIn, final boolean isActualState, final CallbackInfo info) {
        if (entityIn != null && Util.mc.player != null && (entityIn.equals((Object)Util.mc.player) || (Util.mc.player.getRidingEntity() != null && entityIn.equals((Object)Util.mc.player.getRidingEntity()))) && ((Flight.getInstance().isOn() && ((Flight.getInstance().mode.getValue() == Flight.Mode.PACKET && Flight.getInstance().better.getValue() && Flight.getInstance().phase.getValue()) || (Flight.getInstance().mode.getValue() == Flight.Mode.DAMAGE && Flight.getInstance().noClip.getValue()))) || (Phase.getInstance().isOn() && Phase.getInstance().mode.getValue() == Phase.Mode.PACKETFLY && Phase.getInstance().type.getValue() == Phase.PacketFlyMode.SETBACK && Phase.getInstance().boundingBox.getValue()))) {
            info.cancel();
        }
        try {
            if ((Freecam.getInstance().isOff() && Jesus.getInstance().isOn() && Jesus.getInstance().mode.getValue() == Jesus.Mode.TRAMPOLINE && Util.mc.player != null && state != null && state.getBlock() instanceof BlockLiquid && !(entityIn instanceof EntityBoat) && !Util.mc.player.isSneaking() && Util.mc.player.fallDistance < 3.0f && !EntityUtil.isAboveLiquid((Entity)Util.mc.player) && EntityUtil.checkForLiquid((Entity)Util.mc.player, false)) || (EntityUtil.checkForLiquid((Entity)Util.mc.player, false) && Util.mc.player.getRidingEntity() != null && Util.mc.player.getRidingEntity().fallDistance < 3.0f && EntityUtil.isAboveBlock((Entity)Util.mc.player, pos))) {
                final AxisAlignedBB offset = Jesus.offset.offset(pos);
                if (entityBox.intersects(offset)) {
                    collidingBoxes.add(offset);
                }
                info.cancel();
            }
        }
        catch (Exception ex) {}
    }
    
    @Inject(method = { "isFullCube" }, at = { @At("HEAD") }, cancellable = true)
    public void isFullCubeHook(final IBlockState blockState, final CallbackInfoReturnable<Boolean> info) {
        try {
            if (XRay.getInstance().isOn()) {
                info.setReturnValue(XRay.getInstance().shouldRender(Block.class.cast(this)));
                info.cancel();
            }
        }
        catch (Exception ex) {}
    }
}
