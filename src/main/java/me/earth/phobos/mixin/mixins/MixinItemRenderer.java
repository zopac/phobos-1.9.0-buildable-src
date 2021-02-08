//Deobfuscated with https://github.com/PetoPetko/Minecraft-Deobfuscator3000 using mappings "1.12 stable mappings"!

package me.earth.phobos.mixin.mixins;

import net.minecraft.client.entity.*;
import net.minecraft.util.*;
import net.minecraft.item.*;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.callback.*;
import net.minecraft.client.*;
import net.minecraft.client.renderer.*;
import org.spongepowered.asm.mixin.injection.*;
import me.earth.phobos.features.modules.render.*;

@Mixin({ ItemRenderer.class })
public abstract class MixinItemRenderer
{
    private boolean injection;
    
    public MixinItemRenderer() {
        this.injection = true;
    }
    
    @Shadow
    public abstract void renderItemInFirstPerson(final AbstractClientPlayer p0, final float p1, final float p2, final EnumHand p3, final float p4, final ItemStack p5, final float p6);
    
    @Inject(method = { "renderItemInFirstPerson(Lnet/minecraft/client/entity/AbstractClientPlayer;FFLnet/minecraft/util/EnumHand;FLnet/minecraft/item/ItemStack;F)V" }, at = { @At("HEAD") }, cancellable = true)
    public void renderItemInFirstPersonHook(final AbstractClientPlayer player, final float p_187457_2_, final float p_187457_3_, final EnumHand hand, final float p_187457_5_, final ItemStack stack, final float p_187457_7_, final CallbackInfo info) {
        if (this.injection) {
            info.cancel();
            final SmallShield offset = SmallShield.getINSTANCE();
            float xOffset = 0.0f;
            float yOffset = 0.0f;
            this.injection = false;
            if (hand == EnumHand.MAIN_HAND) {
                if (offset.isOn() && player.getHeldItemMainhand() != ItemStack.EMPTY) {
                    xOffset = offset.mainX.getValue();
                    yOffset = offset.mainY.getValue();
                }
            }
            else if (!offset.normalOffset.getValue() && offset.isOn() && player.getHeldItemOffhand() != ItemStack.EMPTY) {
                xOffset = offset.offX.getValue();
                yOffset = offset.offY.getValue();
            }
            this.renderItemInFirstPerson(player, p_187457_2_, p_187457_3_, hand, p_187457_5_ + xOffset, stack, p_187457_7_ + yOffset);
            this.injection = true;
        }
    }
    
    @Redirect(method = { "renderArmFirstPerson" }, at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/GlStateManager;translate(FFF)V", ordinal = 0))
    public void translateHook(final float x, final float y, final float z) {
        final SmallShield offset = SmallShield.getINSTANCE();
        final boolean shiftPos = Minecraft.instance.player != null && Minecraft.instance.player.getHeldItemMainhand() != ItemStack.EMPTY && offset.isOn();
        GlStateManager.translate(x + (shiftPos ? offset.mainX.getValue() : 0.0f), y + (shiftPos ? offset.mainY.getValue() : 0.0f), z);
    }
    
    @Inject(method = { "renderFireInFirstPerson" }, at = { @At("HEAD") }, cancellable = true)
    public void renderFireInFirstPersonHook(final CallbackInfo info) {
        if (NoRender.getInstance().isOn() && NoRender.getInstance().fire.getValue()) {
            info.cancel();
        }
    }
    
    @Inject(method = { "renderSuffocationOverlay" }, at = { @At("HEAD") }, cancellable = true)
    public void renderSuffocationOverlay(final CallbackInfo ci) {
        if (NoRender.getInstance().isOn() && NoRender.getInstance().blocks.getValue()) {
            ci.cancel();
        }
    }
}
