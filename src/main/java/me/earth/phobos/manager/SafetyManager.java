//Deobfuscated with https://github.com/PetoPetko/Minecraft-Deobfuscator3000 using mappings "1.12 stable mappings"!

package me.earth.phobos.manager;

import me.earth.phobos.features.*;

import java.util.Timer;
import java.util.concurrent.atomic.*;
import me.earth.phobos.features.modules.combat.*;
import me.earth.phobos.features.modules.client.*;
import net.minecraft.entity.*;
import net.minecraft.entity.item.*;
import me.earth.phobos.util.*;
import net.minecraft.util.math.*;
import net.minecraft.entity.player.*;
import java.util.*;
import java.util.concurrent.*;

public class SafetyManager extends Feature implements Runnable
{
    private final Timer syncTimer;
    private ScheduledExecutorService service;
    private final AtomicBoolean SAFE;
    
    public SafetyManager() {
        this.syncTimer = new Timer();
        this.SAFE = new AtomicBoolean(false);
    }
    
    @Override
    public void run() {
        if (AutoCrystal.getInstance().isOff() || AutoCrystal.getInstance().threadMode.getValue() == AutoCrystal.ThreadMode.NONE) {
            this.doSafetyCheck();
        }
    }
    
    public void doSafetyCheck() {
        if (!Feature.fullNullCheck()) {
            boolean safe = true;
            final EntityPlayer closest = Managers.getInstance().safety.getValue() ? EntityUtil.getClosestEnemy(18.0) : null;
            if (Managers.getInstance().safety.getValue() && closest == null) {
                this.SAFE.set(true);
                return;
            }
            final List<Entity> crystals = new ArrayList<Entity>(SafetyManager.mc.world.loadedEntityList);
            for (final Entity crystal : crystals) {
                if (crystal instanceof EntityEnderCrystal && DamageUtil.calculateDamage(crystal, (Entity)SafetyManager.mc.player) > 4.0 && (closest == null || closest.getDistanceSq(crystal) < 40.0)) {
                    safe = false;
                    break;
                }
            }
            if (safe) {
                for (final BlockPos pos : BlockUtil.possiblePlacePositions(4.0f, false, Managers.getInstance().oneDot15.getValue())) {
                    if (DamageUtil.calculateDamage(pos, (Entity)SafetyManager.mc.player) > 4.0 && (closest == null || closest.getDistanceSq(pos) < 40.0)) {
                        safe = false;
                        break;
                    }
                }
            }
            this.SAFE.set(safe);
        }
    }
    
    public void onUpdate() {
        this.run();
    }
    
    public String getSafetyString() {
        if (this.SAFE.get()) {
            return "�aSecure";
        }
        return "�cUnsafe";
    }
    
    public boolean isSafe() {
        return this.SAFE.get();
    }
    
    public ScheduledExecutorService getService() {
        final ScheduledExecutorService service = Executors.newSingleThreadScheduledExecutor();
        service.scheduleAtFixedRate(this, 0L, Managers.getInstance().safetyCheck.getValue(), TimeUnit.MILLISECONDS);
        return service;
    }
}
