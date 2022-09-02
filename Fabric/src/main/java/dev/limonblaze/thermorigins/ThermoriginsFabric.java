package dev.limonblaze.thermorigins;

import dev.limonblaze.thermorigins.registry.ThermoPowersFabric;
import net.fabricmc.api.ModInitializer;

public class ThermoriginsFabric implements ModInitializer {
    
    @Override
    public void onInitialize() {
        Thermorigins.init();
        ThermoPowersFabric.registerAll();
        Thermorigins.LOGGER.info("Hello Fabric world!");
    }
    
}
