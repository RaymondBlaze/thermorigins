package dev.limonblaze.thermorigins;

import net.fabricmc.api.ModInitializer;

public class ThermoriginsFabric implements ModInitializer {
    
    @Override
    public void onInitialize() {
        Thermorigins.init();
        Thermorigins.LOGGER.info("Hello Fabric world!");
    }
    
}
