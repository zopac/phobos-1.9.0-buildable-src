//Deobfuscated with https://github.com/PetoPetko/Minecraft-Deobfuscator3000 using mappings "1.12 stable mappings"!

package me.earth.phobos.features.modules.player;

import me.earth.phobos.features.modules.*;
import me.earth.phobos.features.setting.*;
import net.minecraftforge.event.world.*;
import me.earth.phobos.features.*;
import net.minecraftforge.fml.common.eventhandler.*;
import net.minecraftforge.event.entity.player.*;
import me.earth.phobos.event.events.*;
import net.minecraft.network.play.client.*;
import net.minecraft.world.*;
import me.earth.phobos.*;
import net.minecraft.util.math.*;
import net.minecraft.util.*;
import net.minecraft.block.state.*;
import net.minecraft.init.*;
import net.minecraft.enchantment.*;
import me.earth.phobos.util.*;
import net.minecraft.entity.*;
import net.minecraft.item.*;
import me.earth.phobos.event.events.BlockEvent;

public class BlockTweaks extends Module
{
    public Setting<Boolean> autoTool;
    public Setting<Boolean> autoWeapon;
    public Setting<Boolean> noFriendAttack;
    public Setting<Boolean> noBlock;
    public Setting<Boolean> noGhost;
    public Setting<Boolean> destroy;
    private static BlockTweaks INSTANCE;
    private int lastHotbarSlot;
    private int currentTargetSlot;
    private boolean switched;
    
    public BlockTweaks() {
        super("BlockTweaks", "Some tweaks for blocks.", Category.PLAYER, true, false, false);
        this.autoTool = (Setting<Boolean>)this.register(new Setting("AutoTool", false));
        this.autoWeapon = (Setting<Boolean>)this.register(new Setting("AutoWeapon", false));
        this.noFriendAttack = (Setting<Boolean>)this.register(new Setting("NoFriendAttack", false));
        this.noBlock = (Setting<Boolean>)this.register(new Setting("NoHitboxBlock", true));
        this.noGhost = (Setting<Boolean>)this.register(new Setting("NoGlitchBlocks", false));
        this.destroy = (Setting<Boolean>)this.register(new Setting("Destroy", false, v -> this.noGhost.getValue()));
        this.lastHotbarSlot = -1;
        this.currentTargetSlot = -1;
        this.switched = false;
        this.setInstance();
    }
    
    private void setInstance() {
        BlockTweaks.INSTANCE = this;
    }
    
    public static BlockTweaks getINSTANCE() {
        if (BlockTweaks.INSTANCE == null) {
            BlockTweaks.INSTANCE = new BlockTweaks();
        }
        return BlockTweaks.INSTANCE;
    }
    
    @Override
    public void onDisable() {
        if (this.switched) {
            this.equip(this.lastHotbarSlot, false);
        }
        this.lastHotbarSlot = -1;
        this.currentTargetSlot = -1;
    }
    
    @SubscribeEvent
    public void onBreak(final BlockEvent event) {
        if (Feature.fullNullCheck() || !this.noGhost.getValue() || !this.destroy.getValue()) {
            return;
        }
        if (!(BlockTweaks.mc.player.getHeldItemMainhand().getItem() instanceof ItemBlock)) {
            final BlockPos pos = BlockTweaks.mc.player.getPosition();
            this.removeGlitchBlocks(pos);
        }
    }
    
    @SubscribeEvent
    public void onBlockInteract(final PlayerInteractEvent.LeftClickBlock event) {
        if (this.autoTool.getValue() && (Speedmine.getInstance().mode.getValue() != Speedmine.Mode.PACKET || Speedmine.getInstance().isOff() || !Speedmine.getInstance().tweaks.getValue()) && !Feature.fullNullCheck() && event.getPos() != null) {
            this.equipBestTool(BlockTweaks.mc.world.getBlockState(event.getPos()));
        }
    }
    
    @SubscribeEvent
    public void onAttack(final AttackEntityEvent event) {
        if (this.autoWeapon.getValue() && !Feature.fullNullCheck() && event.getTarget() != null) {
            this.equipBestWeapon(event.getTarget());
        }
    }
    
    @SubscribeEvent
    public void onPacketSend(final PacketEvent.Send event) {
        if (fullNullCheck()) {
            return;
        }
        if (this.noFriendAttack.getValue() && event.getPacket() instanceof CPacketUseEntity) {
            final CPacketUseEntity packet = event.getPacket();
            final Entity entity = packet.getEntityFromWorld((World)BlockTweaks.mc.world);
            if (entity != null && Phobos.friendManager.isFriend(entity.getName())) {
                event.setCanceled(true);
            }
        }
    }
    
    @Override
    public void onUpdate() {
        if (!Feature.fullNullCheck()) {
            if (BlockTweaks.mc.player.inventory.currentItem != this.lastHotbarSlot && BlockTweaks.mc.player.inventory.currentItem != this.currentTargetSlot) {
                this.lastHotbarSlot = BlockTweaks.mc.player.inventory.currentItem;
            }
            if (!BlockTweaks.mc.gameSettings.keyBindAttack.isKeyDown() && this.switched) {
                this.equip(this.lastHotbarSlot, false);
            }
        }
    }
    
    private void removeGlitchBlocks(final BlockPos pos) {
        for (int dx = -4; dx <= 4; ++dx) {
            for (int dy = -4; dy <= 4; ++dy) {
                for (int dz = -4; dz <= 4; ++dz) {
                    final BlockPos blockPos = new BlockPos(pos.getX() + dx, pos.getY() + dy, pos.getZ() + dz);
                    if (BlockTweaks.mc.world.getBlockState(blockPos).getBlock().equals(Blocks.AIR)) {
                        BlockTweaks.mc.playerController.processRightClickBlock(BlockTweaks.mc.player, BlockTweaks.mc.world, blockPos, EnumFacing.DOWN, new Vec3d(0.5, 0.5, 0.5), EnumHand.MAIN_HAND);
                    }
                }
            }
        }
    }
    
    private void equipBestTool(final IBlockState blockState) {
        int bestSlot = -1;
        double max = 0.0;
        for (int i = 0; i < 9; ++i) {
            final ItemStack stack = BlockTweaks.mc.player.inventory.getStackInSlot(i);
            if (!stack.isEmpty) {
                float speed = stack.getDestroySpeed(blockState);
                if (speed > 1.0f) {
                    final int eff;
                    speed += (float)(((eff = EnchantmentHelper.getEnchantmentLevel(Enchantments.EFFICIENCY, stack)) > 0) ? (Math.pow(eff, 2.0) + 1.0) : 0.0);
                    if (speed > max) {
                        max = speed;
                        bestSlot = i;
                    }
                }
            }
        }
        this.equip(bestSlot, true);
    }
    
    public void equipBestWeapon(final Entity entity) {
        int bestSlot = -1;
        double maxDamage = 0.0;
        EnumCreatureAttribute creatureAttribute = EnumCreatureAttribute.UNDEFINED;
        if (EntityUtil.isLiving(entity)) {
            final EntityLivingBase base = (EntityLivingBase)entity;
            creatureAttribute = base.getCreatureAttribute();
        }
        for (int i = 0; i < 9; ++i) {
            final ItemStack stack = BlockTweaks.mc.player.inventory.getStackInSlot(i);
            if (!stack.isEmpty) {
                if (stack.getItem() instanceof ItemTool) {
                    final double damage = ((ItemTool)stack.getItem()).attackDamage + (double)EnchantmentHelper.getModifierForCreature(stack, creatureAttribute);
                    if (damage > maxDamage) {
                        maxDamage = damage;
                        bestSlot = i;
                    }
                }
                else if (stack.getItem() instanceof ItemSword) {
                    final double damage = ((ItemSword)stack.getItem()).getAttackDamage() + (double)EnchantmentHelper.getModifierForCreature(stack, creatureAttribute);
                    if (damage > maxDamage) {
                        maxDamage = damage;
                        bestSlot = i;
                    }
                }
            }
        }
        this.equip(bestSlot, true);
    }
    
    private void equip(final int slot, final boolean equipTool) {
        if (slot != -1) {
            if (slot != BlockTweaks.mc.player.inventory.currentItem) {
                this.lastHotbarSlot = BlockTweaks.mc.player.inventory.currentItem;
            }
            this.currentTargetSlot = slot;
            BlockTweaks.mc.player.inventory.currentItem = slot;
            BlockTweaks.mc.playerController.syncCurrentPlayItem();
            this.switched = equipTool;
        }
    }
    
    static {
        BlockTweaks.INSTANCE = new BlockTweaks();
    }
}
