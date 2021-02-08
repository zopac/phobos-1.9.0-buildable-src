package me.earth.phobos.mixin.mixins;

import org.spongepowered.asm.mixin.*;
import net.minecraft.entity.player.*;
import net.minecraft.world.*;
import com.mojang.authlib.*;
import org.spongepowered.asm.mixin.injection.callback.*;
import me.earth.phobos.features.modules.player.*;
import net.minecraft.entity.*;
import me.earth.phobos.*;
import me.earth.phobos.features.modules.misc.*;
import org.spongepowered.asm.mixin.injection.*;
import me.earth.phobos.features.modules.movement.*;

@Mixin({ EntityPlayer.class })
public abstract class MixinEntityPlayer extends EntityLivingBase
{
    public MixinEntityPlayer(final World worldIn, final GameProfile gameProfileIn) {
        super(worldIn);
    }

    @Inject(method={"getCooldownPeriod"}, at={@At(value="HEAD")}, cancellable=true)
    private void getCooldownPeriodHook(CallbackInfoReturnable<Float> callbackInfoReturnable) {
        if (TpsSync.getInstance().isOn() && TpsSync.getInstance().attack.getValue().booleanValue()) {
            callbackInfoReturnable.setReturnValue(Float.valueOf((float)(1.0 / ((EntityPlayer)EntityPlayer.class.cast((Object)this)).getEntityAttribute(SharedMonsterAttributes.ATTACK_SPEED).getAttributeValue() * 20.0 * (double)Phobos.serverManager.getTpsFactor())));
        }
    }
    
    @ModifyConstant(method = { "getPortalCooldown" }, constant = { @Constant(intValue = 10) })
    private int getPortalCooldownHook(final int cooldown) {
        int time = cooldown;
        if (BetterPortals.getInstance().isOn() && BetterPortals.getInstance().fastPortal.getValue()) {
            time = BetterPortals.getInstance().cooldown.getValue();
        }
        return time;
    }
    
    @Inject(method = { "isEntityInsideOpaqueBlock" }, at = { @At("HEAD") }, cancellable = true)
    private void isEntityInsideOpaqueBlockHook(final CallbackInfoReturnable<Boolean> info) {
        if (Phase.getInstance().isOn() && Phase.getInstance().type.getValue() != Phase.PacketFlyMode.NONE) {
            info.setReturnValue(false);
        }
        else if (TestPhase.getInstance().isOn()) {
            info.setReturnValue(false);
        }
    }
}
