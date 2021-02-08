//Deobfuscated with https://github.com/PetoPetko/Minecraft-Deobfuscator3000 using mappings "1.12 stable mappings"!

package me.earth.phobos.mixin.mixins;

import net.minecraft.item.*;
import net.minecraft.nbt.*;
import org.spongepowered.asm.mixin.injection.callback.*;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.*;
import me.earth.phobos.features.modules.player.*;

@Mixin({ ItemStack.class })
public abstract class MixinItemStack
{
    @Shadow
    private int itemDamage;
    
    @Inject(method = { "<init>(Lnet/minecraft/item/Item;IILnet/minecraft/nbt/NBTTagCompound;)V" }, at = { @At("RETURN") })
    @Dynamic
    private void initHook(final Item item, final int idkWhatDisIsIPastedThis, final int dura, final NBTTagCompound compound, final CallbackInfo info) {
        this.itemDamage = this.checkDurability(ItemStack.class.cast(this), this.itemDamage, dura);
    }
    
    @Inject(method = { "<init>(Lnet/minecraft/nbt/NBTTagCompound;)V" }, at = { @At("RETURN") })
    private void initHook2(final NBTTagCompound compound, final CallbackInfo info) {
        this.itemDamage = this.checkDurability(ItemStack.class.cast(this), this.itemDamage, compound.getShort("Damage"));
    }
    
    private int checkDurability(final ItemStack item, final int damage, final int dura) {
        int trueDura = damage;
        if (TrueDurability.getInstance().isOn() && dura < 0) {
            trueDura = dura;
        }
        return trueDura;
    }
}
