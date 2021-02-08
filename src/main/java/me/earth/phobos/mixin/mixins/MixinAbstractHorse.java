package me.earth.phobos.mixin.mixins;

import org.spongepowered.asm.mixin.*;
import net.minecraft.entity.passive.*;
import org.spongepowered.asm.mixin.injection.callback.*;
import me.earth.phobos.features.modules.movement.*;
import org.spongepowered.asm.mixin.injection.*;

@Mixin({ AbstractHorse.class })
public class MixinAbstractHorse
{
    @Inject(method = { "isHorseSaddled" }, at = { @At("HEAD") }, cancellable = true)
    public void isHorseSaddled(final CallbackInfoReturnable<Boolean> cir) {
        if (EntityControl.INSTANCE.isEnabled()) {
            cir.setReturnValue(true);
        }
    }
}
