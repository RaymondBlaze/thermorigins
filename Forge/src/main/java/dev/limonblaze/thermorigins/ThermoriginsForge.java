package dev.limonblaze.thermorigins;

import dev.limonblaze.thermorigins.registry.ThermoPowersForge;
import net.minecraftforge.fml.common.Mod;

@Mod(Thermorigins.ID)
public class ThermoriginsForge {
    
    public ThermoriginsForge() {
        Thermorigins.init();
        ThermoPowersForge.registerAll();
        Thermorigins.LOGGER.info("Hello Forge world!");
    }
    
}