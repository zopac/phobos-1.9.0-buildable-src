package me.earth.phobos.mixin.mixins;

import org.spongepowered.asm.mixin.*;
import net.minecraft.network.*;
import me.earth.phobos.features.modules.misc.*;
import org.spongepowered.asm.mixin.injection.*;

@Mixin({ NettyCompressionDecoder.class })
public abstract class MixinNettyCompressionDecoder
{
    @ModifyConstant(method = { "decode" }, constant = { @Constant(intValue = 2097152) })
    private int decodeHook(final int n) {
        if (Bypass.getInstance().isOn() && Bypass.getInstance().packets.getValue() && Bypass.getInstance().noLimit.getValue()) {
            return Integer.MAX_VALUE;
        }
        return n;
    }
}
