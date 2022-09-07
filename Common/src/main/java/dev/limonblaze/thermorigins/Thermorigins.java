package dev.limonblaze.thermorigins;

import dev.limonblaze.thermorigins.registry.ThermoPowers;
import net.minecraft.resources.ResourceLocation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Random;

public class Thermorigins {
    public static final String ID = "thermorigins";
    public static final String NAME = "Thermorigins";
    public static final Logger LOGGER = LoggerFactory.getLogger(NAME);
    public static Random RANDOM = new Random();
    public static final String[] ORIGIN_NAMES = new String[]{"Steampowered", "Magmaborn"};
    
    public static void init() {
        ThermoPowers.registerAll();
    }
    
    public static String randomOriginName() {
        return ORIGIN_NAMES[RANDOM.nextInt(ORIGIN_NAMES.length)];
    }
    
    public static ResourceLocation asResource(String name) {
        return new ResourceLocation(ID, name);
    }
    
}