//Deobfuscated with https://github.com/PetoPetko/Minecraft-Deobfuscator3000 using mappings "1.12 stable mappings"!

package me.earth.phobos.mixin.mixins;

import org.spongepowered.asm.mixin.*;
import net.minecraft.block.state.*;
import net.minecraft.entity.player.*;
import me.earth.phobos.*;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.*;
import net.minecraftforge.common.*;
import net.minecraftforge.fml.common.eventhandler.*;
import net.minecraft.item.*;
import net.minecraft.init.*;
import net.minecraft.world.*;
import net.minecraft.block.*;
import net.minecraft.entity.*;
import me.earth.phobos.features.modules.player.*;
import net.minecraft.block.material.*;
import net.minecraft.client.entity.*;
import net.minecraft.client.multiplayer.*;
import net.minecraft.util.math.*;
import net.minecraft.util.*;
import net.minecraft.client.*;
import me.earth.phobos.event.events.*;

@Mixin({ PlayerControllerMP.class })
public class MixinPlayerControllerMP
{
    @Redirect(method = { "onPlayerDamageBlock" }, at = @At(value = "INVOKE", target = "Lnet/minecraft/block/state/IBlockState;getPlayerRelativeBlockHardness(Lnet/minecraft/entity/player/EntityPlayer;Lnet/minecraft/world/World;Lnet/minecraft/util/math/BlockPos;)F"))
    public float getPlayerRelativeBlockHardnessHook(final IBlockState state, final EntityPlayer player, final World worldIn, final BlockPos pos) {
        return state.getPlayerRelativeBlockHardness(player, worldIn, pos) * ((TpsSync.getInstance().isOn() && TpsSync.getInstance().mining.getValue()) ? (1.0f / Phobos.serverManager.getTpsFactor()) : 1.0f);
    }
    
    @Inject(method = { "resetBlockRemoving" }, at = { @At("HEAD") }, cancellable = true)
    public void resetBlockRemovingHook(final CallbackInfo info) {
        if (Speedmine.getInstance().isOn() && Speedmine.getInstance().reset.getValue()) {
            info.cancel();
        }
    }
    
    @Inject(method = { "clickBlock" }, at = { @At("HEAD") }, cancellable = true)
    private void clickBlockHook(final BlockPos pos, final EnumFacing face, final CallbackInfoReturnable<Boolean> info) {
        final BlockEvent event = new BlockEvent(3, pos, face);
        MinecraftForge.EVENT_BUS.post((Event)event);
    }
    
    @Inject(method = { "onPlayerDamageBlock" }, at = { @At("HEAD") }, cancellable = true)
    private void onPlayerDamageBlockHook(final BlockPos pos, final EnumFacing face, final CallbackInfoReturnable<Boolean> info) {
        final BlockEvent event = new BlockEvent(4, pos, face);
        MinecraftForge.EVENT_BUS.post((Event)event);
    }
    
    @Inject(method = { "getBlockReachDistance" }, at = { @At("RETURN") }, cancellable = true)
    private void getReachDistanceHook(final CallbackInfoReturnable<Float> distance) {
        if (Reach.getInstance().isOn()) {
            final float range = distance.getReturnValue();
            distance.setReturnValue(((boolean)Reach.getInstance().override.getValue()) ? Reach.getInstance().reach.getValue() : Float.valueOf(range + Reach.getInstance().add.getValue()));
        }
    }
    
    @Redirect(method = { "processRightClickBlock" }, at = @At(value = "INVOKE", target = "Lnet/minecraft/item/ItemBlock;canPlaceBlockOnSide(Lnet/minecraft/world/World;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/util/EnumFacing;Lnet/minecraft/entity/player/EntityPlayer;Lnet/minecraft/item/ItemStack;)Z"))
    public boolean canPlaceBlockOnSideHook(final ItemBlock itemBlock, final World worldIn, BlockPos pos, EnumFacing side, final EntityPlayer player, final ItemStack stack) {
        final Block block = worldIn.getBlockState(pos).getBlock();
        if (block == Blocks.SNOW_LAYER && block.isReplaceable((IBlockAccess)worldIn, pos)) {
            side = EnumFacing.UP;
        }
        else if (!block.isReplaceable((IBlockAccess)worldIn, pos)) {
            pos = pos.offset(side);
        }
        final IBlockState iblockstate1 = worldIn.getBlockState(pos);
        final AxisAlignedBB axisalignedbb = itemBlock.block.getDefaultState().getCollisionBoundingBox((IBlockAccess)worldIn, pos);
        if (axisalignedbb != Block.NULL_AABB && !worldIn.checkNoEntityCollision(axisalignedbb.offset(pos), (Entity)null)) {
            if (BlockTweaks.getINSTANCE().isOff() || !BlockTweaks.getINSTANCE().noBlock.getValue()) {
                return false;
            }
        }
        else if (iblockstate1.getMaterial() == Material.CIRCUITS && itemBlock.block == Blocks.ANVIL) {
            return true;
        }
        return iblockstate1.getBlock().isReplaceable((IBlockAccess)worldIn, pos) && itemBlock.block.canPlaceBlockOnSide(worldIn, pos, side);
    }
    
    @Inject(method = { "processRightClickBlock" }, at = { @At("HEAD") }, cancellable = true)
    public void processRightClickBlock(final EntityPlayerSP player, final WorldClient worldIn, final BlockPos pos, final EnumFacing direction, final Vec3d vec, final EnumHand hand, final CallbackInfoReturnable<EnumActionResult> cir) {
        final ProcessRightClickBlockEvent event = new ProcessRightClickBlockEvent(pos, hand, Minecraft.instance.player.getHeldItem(hand));
        MinecraftForge.EVENT_BUS.post((Event)event);
        if (event.isCanceled()) {
            cir.cancel();
        }
    }
}
