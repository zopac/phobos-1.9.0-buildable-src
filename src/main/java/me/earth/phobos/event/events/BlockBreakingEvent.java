package me.earth.phobos.event.events;

import me.earth.phobos.event.*;
import net.minecraft.util.math.*;

public class BlockBreakingEvent extends EventStage
{
    public BlockPos pos;
    public int breakingID;
    public int breakStage;
    
    public BlockBreakingEvent(final BlockPos pos, final int breakingID, final int breakStage) {
        this.pos = pos;
        this.breakingID = breakingID;
        this.breakStage = breakStage;
    }
}
