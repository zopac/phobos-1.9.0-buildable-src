package me.earth.phobos.mixin;

import net.minecraftforge.fml.relauncher.*;
import me.earth.phobos.*;
import org.spongepowered.asm.launch.*;
import org.spongepowered.asm.mixin.*;
import java.util.*;

public class PhobosMixinLoader implements IFMLLoadingPlugin
{
    private static boolean isObfuscatedEnvironment;
    
    public PhobosMixinLoader() {
        Phobos.LOGGER.info("Phobos mixins initialized");
        MixinBootstrap.init();
        Mixins.addConfiguration("mixins.phobos.json");
        MixinEnvironment.getDefaultEnvironment().setObfuscationContext("searge");
        Phobos.LOGGER.info(MixinEnvironment.getDefaultEnvironment().getObfuscationContext());
    }
    
    public String[] getASMTransformerClass() {
        return new String[0];
    }
    
    public String getModContainerClass() {
        return null;
    }
    
    public String getSetupClass() {
        return null;
    }

    public void injectData(Map<String, Object> data) {
        isObfuscatedEnvironment = (Boolean)data.get("runtimeDeobfuscationEnabled");
    }
    
    public String getAccessTransformerClass() {
        return null;
    }
    
    static {
        PhobosMixinLoader.isObfuscatedEnvironment = false;
    }
}
