package me.earth.phobos.mixin.mixins;

import net.minecraft.client.settings.*;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.callback.*;
import me.earth.phobos.event.events.*;
import net.minecraftforge.common.*;
import net.minecraftforge.fml.common.eventhandler.*;
import org.spongepowered.asm.mixin.injection.*;

@Mixin({ KeyBinding.class })
public class MixinKeyBinding
{
    @Shadow
    private boolean pressed;
    
    @Inject(method = { "isKeyDown" }, at = { @At("RETURN") }, cancellable = true)
    private void isKeyDown(final CallbackInfoReturnable<Boolean> info) {
        final KeyEvent event = new KeyEvent(0, info.getReturnValue(), this.pressed);
        MinecraftForge.EVENT_BUS.post((Event)event);
        info.setReturnValue(event.info);
    }
}
