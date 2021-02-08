package me.earth.phobos.mixin.mixins.accessors;

import org.spongepowered.asm.mixin.*;
import net.minecraft.inventory.*;
import org.spongepowered.asm.mixin.gen.*;

@Mixin({ Container.class })
public interface IContainer
{
    @Accessor("transactionID")
    void setTransactionID(final short p0);
}
