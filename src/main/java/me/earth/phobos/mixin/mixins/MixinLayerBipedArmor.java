package me.earth.phobos.mixin.mixins;

import net.minecraft.client.model.*;
import org.spongepowered.asm.mixin.*;
import net.minecraft.client.renderer.entity.layers.*;
import net.minecraft.client.renderer.entity.*;

@Mixin({ LayerBipedArmor.class })
public abstract class MixinLayerBipedArmor extends LayerArmorBase<ModelBiped>
{
    public MixinLayerBipedArmor(final RenderLivingBase<?> rendererIn) {
        super((RenderLivingBase)rendererIn);
    }
}
