package dev.limonblaze.thermorigins;

import dev.limonblaze.thermorigins.platform.Services;
import dev.limonblaze.thermorigins.registry.ThermoLootFunctions;
import dev.limonblaze.thermorigins.registry.ThermoPowers;
import net.minecraft.resources.ResourceLocation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Thermorigins {
    public static final String ID = "thermorigins";
    public static final String NAME = "Thermorigins";
    public static final Logger LOGGER = LoggerFactory.getLogger(NAME);
    
    public static void init() {
        ThermoPowers.registerAll();
        LOGGER.info("Hello from Common init on {}! we are currently in a {} environment!", Services.PLATFORM.getPlatformName(), Services.PLATFORM.isDevelopmentEnvironment() ? "development" : "production");
    }
    
    public static ResourceLocation asResource(String name) {
        return new ResourceLocation(ID, name);
    }
    
}