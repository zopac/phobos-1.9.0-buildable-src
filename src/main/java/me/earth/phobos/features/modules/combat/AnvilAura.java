//Deobfuscated with https://github.com/PetoPetko/Minecraft-Deobfuscator3000 using mappings "1.12 stable mappings"!

package me.earth.phobos.features.modules.combat;

import me.earth.phobos.features.modules.*;
import me.earth.phobos.features.setting.*;
import net.minecraft.entity.player.*;
import me.earth.phobos.event.events.*;
import net.minecraft.network.play.client.*;
import net.minecraftforge.fml.common.eventhandler.*;
import net.minecraft.entity.*;
import net.minecraft.util.*;
import net.minecraft.item.*;
import net.minecraft.block.*;
import me.earth.phobos.util.*;
import java.util.*;
import net.minecraft.util.math.*;
import java.util.Timer;

public class AnvilAura extends Module
{
    public Setting<Float> range;
    public Setting<Float> wallsRange;
    public Setting<Integer> placeDelay;
    public Setting<Boolean> rotate;
    public Setting<Boolean> packet;
    public Setting<Boolean> switcher;
    public Setting<Integer> rotations;
    private float yaw;
    private float pitch;
    private boolean rotating;
    private int rotationPacketsSpoofed;
    private EntityPlayer finalTarget;
    private BlockPos placeTarget;
    
    public AnvilAura() {
        super("AnvilAura", "Useless", Category.COMBAT, true, false, false);
        this.range = (Setting<Float>)this.register(new Setting("Range", 6.0f, 0.0f, 10.0f));
        this.wallsRange = (Setting<Float>)this.register(new Setting("WallsRange", 3.5f, 0.0f, 10.0f));
        this.placeDelay = (Setting<Integer>)this.register(new Setting("PlaceDelay", 0, 0, 500));
        this.rotate = (Setting<Boolean>)this.register(new Setting("Rotate", true));
        this.packet = (Setting<Boolean>)this.register(new Setting("Packet", true));
        this.switcher = (Setting<Boolean>)this.register(new Setting("Switch", true));
        this.rotations = (Setting<Integer>)this.register(new Setting("Spoofs", 1, 1, 20));
        this.yaw = 0.0f;
        this.pitch = 0.0f;
        this.rotating = false;
        this.rotationPacketsSpoofed = 0;
    }
    
    @Override
    public void onTick() {
        this.doAnvilAura();
    }
    
    @SubscribeEvent
    public void onPacketSend(final PacketEvent.Send event) {
        if (event.getStage() == 0 && this.rotate.getValue() && this.rotating) {
            if (event.getPacket() instanceof CPacketPlayer) {
                final CPacketPlayer packet = event.getPacket();
                packet.yaw = this.yaw;
                packet.pitch = this.pitch;
            }
            ++this.rotationPacketsSpoofed;
            if (this.rotationPacketsSpoofed >= this.rotations.getValue()) {
                this.rotating = false;
                this.rotationPacketsSpoofed = 0;
            }
        }
    }
    
    public void doAnvilAura() {
        this.finalTarget = this.getTarget();
        if (this.finalTarget != null) {
            this.placeTarget = this.getTargetPos((Entity)this.finalTarget);
        }
        if (this.placeTarget != null && this.finalTarget != null) {
            this.placeAnvil(this.placeTarget);
        }
    }
    
    public void placeAnvil(final BlockPos pos) {
        if (this.rotate.getValue()) {
            this.rotateToPos(pos);
        }
        if (this.switcher.getValue() && !this.isHoldingAnvil()) {
            this.doSwitch();
        }
        BlockUtil.placeBlock(pos, EnumHand.MAIN_HAND, false, this.packet.getValue(), AnvilAura.mc.player.isSneaking());
    }
    
    public boolean isHoldingAnvil() {
        final int obbySlot = InventoryUtil.findHotbarBlock(BlockObsidian.class);
        return (AnvilAura.mc.player.getHeldItemMainhand().getItem() instanceof ItemBlock && ((ItemBlock)AnvilAura.mc.player.getHeldItemMainhand().getItem()).getBlock() instanceof BlockAnvil) || (AnvilAura.mc.player.getHeldItemOffhand().getItem() instanceof ItemBlock && ((ItemBlock)AnvilAura.mc.player.getHeldItemOffhand().getItem()).getBlock() instanceof BlockAnvil);
    }
    
    public void doSwitch() {
        int obbySlot = InventoryUtil.findHotbarBlock(BlockObsidian.class);
        if (obbySlot == -1) {
            for (int l = 0; l < 9; ++l) {
                final ItemStack stack = AnvilAura.mc.player.inventory.getStackInSlot(l);
                final Block block = ((ItemBlock)stack.getItem()).getBlock();
                if (block instanceof BlockObsidian) {
                    obbySlot = l;
                }
            }
        }
        if (obbySlot != -1) {
            AnvilAura.mc.player.inventory.currentItem = obbySlot;
        }
    }
    
    public EntityPlayer getTarget() {
        double shortestDistance = -1.0;
        EntityPlayer target = null;
        for (final EntityPlayer player : AnvilAura.mc.world.playerEntities) {
            if (!this.getPlaceableBlocksAboveEntity((Entity)player).isEmpty() && (shortestDistance == -1.0 || AnvilAura.mc.player.getDistanceSq((Entity)player) < MathUtil.square(shortestDistance))) {
                shortestDistance = AnvilAura.mc.player.getDistance((Entity)player);
                target = player;
            }
        }
        return target;
    }
    
    public BlockPos getTargetPos(final Entity target) {
        double distance = -1.0;
        BlockPos finalPos = null;
        for (final BlockPos pos : this.getPlaceableBlocksAboveEntity(target)) {
            if (distance == -1.0 || AnvilAura.mc.player.getDistanceSq(pos) < MathUtil.square(distance)) {
                finalPos = pos;
                distance = AnvilAura.mc.player.getDistance((double)pos.getX(), (double)pos.getY(), (double)pos.getZ());
            }
        }
        return finalPos;
    }
    
    public List<BlockPos> getPlaceableBlocksAboveEntity(final Entity target) {
        final BlockPos playerPos = new BlockPos(Math.floor(AnvilAura.mc.player.posX), Math.floor(AnvilAura.mc.player.posY), Math.floor(AnvilAura.mc.player.posZ));
        final List<BlockPos> positions = new ArrayList<BlockPos>();
        for (int i = (int)Math.floor(AnvilAura.mc.player.posY + 2.0); i <= 256; ++i) {
            final BlockPos pos = new BlockPos(Math.floor(AnvilAura.mc.player.posX), (double)i, Math.floor(AnvilAura.mc.player.posZ));
            if (BlockUtil.isPositionPlaceable(pos, false) == 0 || BlockUtil.isPositionPlaceable(pos, false) == -1) {
                break;
            }
            if (BlockUtil.isPositionPlaceable(pos, false) == 2) {
                break;
            }
            positions.add(pos);
        }
        return positions;
    }

    private void rotateToPos(BlockPos pos) {
        if (this.rotate.getValue().booleanValue()) {
            float[] angle = MathUtil.calcAngle(AnvilAura.mc.player.getPositionEyes(mc.getRenderPartialTicks()), new Vec3d((float) pos.getX() + 0.5f, (float) pos.getY() - 0.5f, (float) pos.getZ() + 0.5f));
            this.yaw = angle[0];
            this.pitch = angle[1];
            this.rotating = true;
        }
    }
}
