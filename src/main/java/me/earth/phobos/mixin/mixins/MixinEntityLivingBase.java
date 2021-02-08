package me.earth.phobos.mixin.mixins;

import org.spongepowered.asm.mixin.*;
import net.minecraft.entity.*;
import net.minecraft.world.*;
import org.spongepowered.asm.mixin.injection.callback.*;
import me.earth.phobos.util.*;
import me.earth.phobos.features.modules.movement.*;
import org.spongepowered.asm.mixin.injection.*;

@Mixin({ EntityLivingBase.class })
public abstract class MixinEntityLivingBase extends Entity
{
    public MixinEntityLivingBase(final World worldIn) {
        super(worldIn);
    }
    
    @Inject(method = { "isElytraFlying" }, at = { @At("HEAD") }, cancellable = true)
    private void isElytraFlyingHook(final CallbackInfoReturnable<Boolean> info) {
        if (Util.mc.player != null && Util.mc.player.equals((Object)this) && ElytraFlight.getInstance().isOn() && ElytraFlight.getInstance().mode.getValue() == ElytraFlight.Mode.BETTER) {
            info.setReturnValue(false);
        }
    }
}
