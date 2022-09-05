package dev.limonblaze.thermorigins;

import dev.limonblaze.thermorigins.registry.ThermoBlockConditionsFabric;
import dev.limonblaze.thermorigins.registry.ThermoEntityConditionsFabric;
import dev.limonblaze.thermorigins.registry.ThermoLootFunctions;
import dev.limonblaze.thermorigins.registry.ThermoPowersFabric;
import net.fabricmc.api.ModInitializer;

public class ThermoriginsFabric implements ModInitializer {
    
    @Override
    public void onInitialize() {
        Thermorigins.init();
        ThermoLootFunctions.registerAll();
        ThermoBlockConditionsFabric.registerAll();
        ThermoEntityConditionsFabric.registerAll();
        ThermoPowersFabric.registerAll();
        Thermorigins.LOGGER.info("Hello Fabric world!");
    }
    
}
