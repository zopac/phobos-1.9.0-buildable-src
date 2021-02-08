package me.earth.phobos.mixin.mixins;

import net.minecraft.client.entity.*;
import net.minecraft.client.network.*;
import org.spongepowered.asm.mixin.*;
import javax.annotation.*;
import org.spongepowered.asm.mixin.injection.callback.*;
import net.minecraft.util.*;
import me.earth.phobos.features.modules.render.*;
import org.spongepowered.asm.mixin.injection.*;
import me.earth.phobos.features.modules.client.*;
import java.util.*;

@Mixin({ AbstractClientPlayer.class })
public abstract class MixinAbstractClientPlayer
{
    @Shadow
    @Nullable
    protected abstract NetworkPlayerInfo getPlayerInfo();
    
    @Inject(method = { "getLocationSkin()Lnet/minecraft/util/ResourceLocation;" }, at = { @At("HEAD") }, cancellable = true)
    public void getLocationSkin(final CallbackInfoReturnable<ResourceLocation> callbackInfoReturnable) {
        if (Chams.getInstance().textured.getValue() && Chams.getInstance().isEnabled()) {
            callbackInfoReturnable.setReturnValue(new ResourceLocation("textures/shinechams3.png"));
        }
    }
    
    @Inject(method = { "getLocationCape" }, at = { @At("HEAD") }, cancellable = true)
    public void getLocationCape(final CallbackInfoReturnable<ResourceLocation> callbackInfoReturnable) {
        if (Capes.getInstance().isEnabled()) {
            final NetworkPlayerInfo info = this.getPlayerInfo();
            UUID uuid = null;
            if (info != null) {
                uuid = this.getPlayerInfo().getGameProfile().getId();
            }
            ResourceLocation cape = Capes.getCapeResource((AbstractClientPlayer)(Object)this);
            if (uuid != null && Capes.hasCape(uuid)) {
                callbackInfoReturnable.setReturnValue(cape);
            }
        }
    }
}
