package me.earth.phobos.mixin.mixins.accessors;

import org.spongepowered.asm.mixin.*;
import net.minecraft.entity.*;
import org.spongepowered.asm.mixin.gen.*;

@Mixin({ EntityLivingBase.class })
public interface IEntityLivingBase
{
    @Invoker("getArmSwingAnimationEnd")
    int getArmSwingAnimationEnd();
}
