package me.earth.phobos.manager;

import me.earth.phobos.util.*;
import net.minecraft.item.*;
import java.util.*;
import net.minecraft.network.play.client.*;
import net.minecraft.network.*;

public class InventoryManager implements Util
{
    public Map<String, List<ItemStack>> inventories;
    private int recoverySlot;
    
    public InventoryManager() {
        this.inventories = new HashMap<String, List<ItemStack>>();
        this.recoverySlot = -1;
    }
    
    public void update() {
        if (this.recoverySlot != -1) {
            InventoryManager.mc.player.connection.sendPacket((Packet)new CPacketHeldItemChange((this.recoverySlot == 8) ? 7 : (this.recoverySlot + 1)));
            InventoryManager.mc.player.connection.sendPacket((Packet)new CPacketHeldItemChange(this.recoverySlot));
            InventoryManager.mc.player.inventory.currentItem = this.recoverySlot;
            InventoryManager.mc.playerController.syncCurrentPlayItem();
            this.recoverySlot = -1;
        }
    }
    
    public void recoverSilent(final int slot) {
        this.recoverySlot = slot;
    }
}
