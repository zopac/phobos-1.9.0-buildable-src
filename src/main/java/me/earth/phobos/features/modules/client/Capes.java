package me.earth.phobos.features.modules.client;

import me.earth.phobos.features.modules.*;
import net.minecraft.util.*;
import net.minecraft.client.entity.*;
import java.util.*;

public class Capes extends Module
{
    public static Map<String, String[]> UUIDs;
    public static final ResourceLocation THREEVT_CAPE;
    public static final ResourceLocation ZBOB_CAPE;
    public static final ResourceLocation OHARE_CAPE;
    public static final ResourceLocation SQUID_CAPE;
    private static Capes instance;
    
    public Capes() {
        super("Capes", "Renders the client's capes", Category.CLIENT, false, false, false);
        Capes.UUIDs.put("Megyn", new String[] { "a5e36d37-5fbe-4481-b5be-1f06baee1f1c", "7de842e8-af08-49ed-9d0c-4071e2a99f00", "8ca55379-c872-4299-987d-d20962badd11", "e6e8bf7e-0b23-4d2e-b2ae-c40c5ff4eecc" });
        Capes.UUIDs.put("zb0b", new String[] { "0aa3b04f-786a-49c8-bea9-025ee0dd1e85" });
        Capes.UUIDs.put("3vt", new String[] { "19bf3f1f-fe06-4c86-bea5-3dad5df89714", "b0836db9-2472-4ba6-a1b7-92c605f5e80d" });
        Capes.UUIDs.put("oHare", new String[] { "453e38dd-f4a9-481f-8ebd-8339e89e5445" });
        Capes.UUIDs.put("Squid", new String[] { "811c9272-9793-4fdd-980d-778e8ad2e54c", "09410a87-dfc8-476c-9acb-04bd07126c6e", "2eb88d28-7a26-43ad-81aa-113bd818d977" });
        Capes.instance = this;
    }
    
    public static Capes getInstance() {
        if (Capes.instance == null) {
            Capes.instance = new Capes();
        }
        return Capes.instance;
    }
    
    public static ResourceLocation getCapeResource(final AbstractClientPlayer player) {
        for (final String name : Capes.UUIDs.keySet()) {
            for (final String uuid : Capes.UUIDs.get(name)) {
                if (name.equalsIgnoreCase("3vt") && player.getUniqueID().toString().equals(uuid)) {
                    return Capes.THREEVT_CAPE;
                }
                if (name.equalsIgnoreCase("Megyn") && player.getUniqueID().toString().equals(uuid)) {
                    return Capes.THREEVT_CAPE;
                }
                if (name.equalsIgnoreCase("oHare") && player.getUniqueID().toString().equals(uuid)) {
                    return Capes.OHARE_CAPE;
                }
            }
        }
        return null;
    }
    
    public static boolean hasCape(final UUID uuid) {
        final Iterator<String> iterator = Capes.UUIDs.keySet().iterator();
        if (iterator.hasNext()) {
            final String name = iterator.next();
            return Arrays.asList((String[])Capes.UUIDs.get(name)).contains(uuid.toString());
        }
        return false;
    }
    
    static {
        Capes.UUIDs = new HashMap<String, String[]>();
        THREEVT_CAPE = new ResourceLocation("textures/3vt2.png");
        ZBOB_CAPE = new ResourceLocation("textures/zb0b.png");
        OHARE_CAPE = new ResourceLocation("textures/ohare.png");
        SQUID_CAPE = new ResourceLocation("textures/squidcape.png");
    }
}
