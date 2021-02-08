package me.earth.phobos.mixin.mixins;

import org.spongepowered.asm.mixin.*;
import net.minecraft.block.*;
import net.minecraft.block.material.*;
import net.minecraft.world.*;
import net.minecraft.util.math.*;
import net.minecraft.block.state.*;
import net.minecraft.entity.*;
import org.spongepowered.asm.mixin.injection.callback.*;
import me.earth.phobos.features.modules.movement.*;
import org.spongepowered.asm.mixin.injection.*;

@Mixin({ BlockSoulSand.class })
public class MixinBlockSoulSand extends Block
{
    public MixinBlockSoulSand() {
        super(Material.SAND, MapColor.BROWN);
    }
    
    @Inject(method = { "onEntityCollision" }, at = { @At("HEAD") }, cancellable = true)
    public void onEntityCollisionHook(final World worldIn, final BlockPos pos, final IBlockState state, final Entity entityIn, final CallbackInfo info) {
        if (NoSlowDown.getInstance().isOn() && NoSlowDown.getInstance().soulSand.getValue()) {
            info.cancel();
        }
    }
}
