package me.earth.phobos.features.modules.render;

import me.earth.phobos.features.modules.*;
import net.minecraft.util.math.*;
import java.util.*;
import net.minecraftforge.fml.common.eventhandler.*;
import me.earth.phobos.event.events.*;

public class BreakingESP extends Module
{
    private Map<BlockPos, Integer> breakingProgressMap;
    
    public BreakingESP() {
        super("BreakingESP", "Shows block breaking progress", Category.RENDER, true, false, false);
        this.breakingProgressMap = new HashMap<BlockPos, Integer>();
    }
    
    @SubscribeEvent
    public void onBlockBreak(final BlockBreakingEvent event) {
        this.breakingProgressMap.put(event.pos, event.breakStage);
    }
    
    @Override
    public void onRender3D(final Render3DEvent event) {
    }
    
    public enum Mode
    {
        BAR, 
        ALPHA, 
        WIDTH;
    }
}
