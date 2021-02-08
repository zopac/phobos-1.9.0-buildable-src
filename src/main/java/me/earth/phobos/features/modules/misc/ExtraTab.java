//Deobfuscated with https://github.com/PetoPetko/Minecraft-Deobfuscator3000 using mappings "1.12 stable mappings"!

package me.earth.phobos.features.modules.misc;

import me.earth.phobos.features.modules.*;
import me.earth.phobos.features.setting.*;
import net.minecraft.client.network.*;
import net.minecraft.scoreboard.*;
import me.earth.phobos.*;

public class ExtraTab extends Module
{
    public Setting<Integer> size;
    private static ExtraTab INSTANCE;
    
    public ExtraTab() {
        super("ExtraTab", "Extends Tab.", Category.MISC, false, false, false);
        this.size = (Setting<Integer>)this.register(new Setting("Size", 250, 1, 1000));
        this.setInstance();
    }
    
    private void setInstance() {
        ExtraTab.INSTANCE = this;
    }
    
    public static String getPlayerName(final NetworkPlayerInfo networkPlayerInfoIn) {
        final String name = (networkPlayerInfoIn.getDisplayName() != null) ? networkPlayerInfoIn.getDisplayName().getFormattedText() : ScorePlayerTeam.formatPlayerName((Team)networkPlayerInfoIn.getPlayerTeam(), networkPlayerInfoIn.getGameProfile().getName());
        if (Phobos.friendManager.isFriend(name)) {
            return "�b" + name;
        }
        return name;
    }
    
    public static ExtraTab getINSTANCE() {
        if (ExtraTab.INSTANCE == null) {
            ExtraTab.INSTANCE = new ExtraTab();
        }
        return ExtraTab.INSTANCE;
    }
    
    static {
        ExtraTab.INSTANCE = new ExtraTab();
    }
}
