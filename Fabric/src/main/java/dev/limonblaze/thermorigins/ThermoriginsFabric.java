package dev.limonblaze.thermorigins;

import dev.limonblaze.thermorigins.registry.ThermoBlockConditionsFabric;
import dev.limonblaze.thermorigins.registry.ThermoEntityConditionsFabric;
import dev.limonblaze.thermorigins.registry.ThermoLootFunctions;
import dev.limonblaze.thermorigins.registry.ThermoPowersFabric;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.loader.api.FabricLoader;

public class ThermoriginsFabric implements ModInitializer {
    public static String APUGLI_ID = "apugli";
    public static String VERSION = "";
    public static int[] SEMVER;
    
    @Override
    public void onInitialize() {
        FabricLoader.getInstance().getModContainer(Thermorigins.ID).ifPresent(modContainer -> {
            VERSION = modContainer.getMetadata().getVersion().getFriendlyString();
            if(VERSION.contains("+")) {
                VERSION = VERSION.split("\\+")[0];
            }
            if(VERSION.contains("-")) {
                VERSION = VERSION.split("-")[0];
            }
            String[] splitVersion = VERSION.split("\\.");
            SEMVER = new int[splitVersion.length];
            for(int i = 0; i < SEMVER.length; i++) {
                SEMVER[i] = Integer.parseInt(splitVersion[i]);
            }
        });
        Thermorigins.init();
        ThermoLootFunctions.registerAll();
        ThermoBlockConditionsFabric.registerAll();
        ThermoEntityConditionsFabric.registerAll();
        ThermoPowersFabric.registerAll();
        Thermorigins.LOGGER.info(String.format("Thermorigins %1$S has initialized in Fabric. Have fun with the %2$s origin!", VERSION, Thermorigins.randomOriginName()));
    }
    
}
