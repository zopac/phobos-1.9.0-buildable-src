//Deobfuscated with https://github.com/PetoPetko/Minecraft-Deobfuscator3000 using mappings "1.12 stable mappings"!

package me.earth.phobos.features.modules.combat;

import me.earth.phobos.features.modules.*;
import me.earth.phobos.features.setting.*;
import me.earth.phobos.features.*;
import net.minecraftforge.fml.common.gameevent.*;
import net.minecraftforge.fml.common.eventhandler.*;
import net.minecraft.init.*;
import me.earth.phobos.util.*;
import java.util.*;
import net.minecraft.util.*;
import net.minecraft.network.play.client.*;
import net.minecraft.network.*;
import net.minecraft.entity.item.*;
import net.minecraft.world.*;
import net.minecraft.entity.*;
import net.minecraft.util.math.*;
import me.earth.phobos.util.Timer;

public class Crasher extends Module
{
    private final Setting<Boolean> oneDot15;
    private final Setting<Float> placeRange;
    private final Setting<Integer> crystals;
    private final Setting<Integer> coolDown;
    private final Setting<InventoryUtil.Switch> switchMode;
    public Setting<Integer> sort;
    private boolean offhand;
    private boolean mainhand;
    private final Timer timer;
    private int lastHotbarSlot;
    private boolean switchedItem;
    private boolean chinese;
    private final List<Integer> entityIDs;
    private int currentID;
    
    public Crasher() {
        super("CrystalCrash", "Attempts to crash chinese AutoCrystals", Category.COMBAT, false, false, true);
        this.oneDot15 = (Setting<Boolean>)this.register(new Setting("1.15", false));
        this.placeRange = (Setting<Float>)this.register(new Setting("PlaceRange", 6.0f, 0.0f, 10.0f));
        this.crystals = (Setting<Integer>)this.register(new Setting("Packets", 25, 0, 100));
        this.coolDown = (Setting<Integer>)this.register(new Setting("CoolDown", 400, 0, 1000));
        this.switchMode = (Setting<InventoryUtil.Switch>)this.register(new Setting("Switch", InventoryUtil.Switch.NORMAL));
        this.sort = (Setting<Integer>)this.register(new Setting("Sort", 0, 0, 2));
        this.offhand = false;
        this.mainhand = false;
        this.timer = new Timer();
        this.lastHotbarSlot = -1;
        this.switchedItem = false;
        this.chinese = false;
        this.entityIDs = new ArrayList<Integer>();
        this.currentID = -1000;
    }
    
    @Override
    public void onEnable() {
        this.chinese = false;
        if (Feature.fullNullCheck() || !this.timer.passedMs(this.coolDown.getValue())) {
            this.disable();
            return;
        }
        this.lastHotbarSlot = Crasher.mc.player.inventory.currentItem;
        this.placeCrystals();
        this.disable();
    }
    
    @Override
    public void onDisable() {
        if (!Feature.fullNullCheck()) {
            for (final int i : this.entityIDs) {
                Crasher.mc.world.removeEntityFromWorld(i);
            }
        }
        this.entityIDs.clear();
        this.currentID = -1000;
        this.timer.reset();
    }
    
    @SubscribeEvent
    public void onTick(final TickEvent.ClientTickEvent event) {
        if (Feature.fullNullCheck() || event.phase == TickEvent.Phase.START || (this.isOff() && this.timer.passedMs(10L))) {
            return;
        }
        this.switchItem(true);
    }
    
    private void placeCrystals() {
        this.offhand = (Crasher.mc.player.getHeldItemOffhand().getItem() == Items.END_CRYSTAL);
        this.mainhand = (Crasher.mc.player.getHeldItemMainhand().getItem() == Items.END_CRYSTAL);
        int crystalcount = 0;
        final List<BlockPos> blocks = BlockUtil.possiblePlacePositions(this.placeRange.getValue(), false, this.oneDot15.getValue());
        if (this.sort.getValue() == 1) {
            blocks.sort(Comparator.comparingDouble(hole -> Crasher.mc.player.getDistanceSq(hole)));
        }
        else if (this.sort.getValue() == 2) {
            blocks.sort(Comparator.comparingDouble(hole -> -Crasher.mc.player.getDistanceSq(hole)));
        }
        for (final BlockPos pos : blocks) {
            if (this.isOff()) {
                break;
            }
            if (crystalcount >= this.crystals.getValue()) {
                break;
            }
            if (!BlockUtil.canPlaceCrystal(pos, false, this.oneDot15.getValue())) {
                continue;
            }
            this.placeCrystal(pos);
            ++crystalcount;
        }
    }
    
    private void placeCrystal(final BlockPos pos) {
        if (!this.chinese && !this.mainhand && !this.offhand && !this.switchItem(false)) {
            this.disable();
            return;
        }
        final RayTraceResult result = Crasher.mc.world.rayTraceBlocks(new Vec3d(Crasher.mc.player.posX, Crasher.mc.player.posY + Crasher.mc.player.getEyeHeight(), Crasher.mc.player.posZ), new Vec3d(pos.getX() + 0.5, pos.getY() - 0.5, pos.getZ() + 0.5));
        final EnumFacing facing = (result == null || result.sideHit == null) ? EnumFacing.UP : result.sideHit;
        Crasher.mc.player.connection.sendPacket((Packet)new CPacketPlayerTryUseItemOnBlock(pos, facing, this.offhand ? EnumHand.OFF_HAND : EnumHand.MAIN_HAND, 0.0f, 0.0f, 0.0f));
        Crasher.mc.player.swingArm(EnumHand.MAIN_HAND);
        final EntityEnderCrystal fakeCrystal = new EntityEnderCrystal((World)Crasher.mc.world, (double)(pos.getX() + 0.5f), (double)(pos.getY() + 1), (double)(pos.getZ() + 0.5f));
        final int newID = this.currentID--;
        this.entityIDs.add(newID);
        Crasher.mc.world.addEntityToWorld(newID, (Entity)fakeCrystal);
    }
    
    private boolean switchItem(final boolean back) {
        this.chinese = true;
        if (this.offhand) {
            return true;
        }
        final boolean[] value = InventoryUtil.switchItemToItem(back, this.lastHotbarSlot, this.switchedItem, this.switchMode.getValue(), Items.END_CRYSTAL);
        this.switchedItem = value[0];
        return value[1];
    }
}
