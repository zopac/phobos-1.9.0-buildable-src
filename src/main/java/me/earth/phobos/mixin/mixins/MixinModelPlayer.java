package me.earth.phobos.mixin.mixins;

import org.spongepowered.asm.mixin.*;
import net.minecraft.client.model.*;
import org.spongepowered.asm.mixin.injection.*;

@Mixin({ ModelPlayer.class })
public class MixinModelPlayer
{
    @Redirect(method = { "renderCape" }, at = @At("HEAD"))
    public void renderCape(final float scale) {
    }
}
