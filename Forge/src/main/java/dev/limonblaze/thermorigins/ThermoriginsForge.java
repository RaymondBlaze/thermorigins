package dev.limonblaze.thermorigins;

import net.minecraftforge.fml.common.Mod;

@Mod(Thermorigins.ID)
public class ThermoriginsForge {
    
    public ThermoriginsForge() {
        Thermorigins.init();
        Thermorigins.LOGGER.info("Hello Forge world!");
    }
    
}