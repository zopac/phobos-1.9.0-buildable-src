package me.earth.phobos.mixin.mixins.accessors;

import org.spongepowered.asm.mixin.*;
import net.minecraft.network.play.server.*;
import org.spongepowered.asm.mixin.gen.*;

@Mixin({ SPacketSetSlot.class })
public interface ISPacketSetSlot
{
    @Accessor("windowId")
    int getId();
    
    @Accessor("windowId")
    void setWindowId(final int p0);
}
