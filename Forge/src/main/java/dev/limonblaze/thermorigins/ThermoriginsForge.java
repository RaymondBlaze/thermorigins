package dev.limonblaze.thermorigins;

import dev.limonblaze.thermorigins.registry.ThermoBlockConditionsForge;
import dev.limonblaze.thermorigins.registry.ThermoEntityConditionsForge;
import dev.limonblaze.thermorigins.registry.ThermoLootFunctions;
import dev.limonblaze.thermorigins.registry.ThermoPowersForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(Thermorigins.ID)
public class ThermoriginsForge {
    
    public ThermoriginsForge() {
        Thermorigins.init();
        ThermoBlockConditionsForge.registerAll();
        ThermoEntityConditionsForge.registerAll();
        ThermoPowersForge.registerAll();
        IEventBus modBus = FMLJavaModLoadingContext.get().getModEventBus();
        modBus.addListener(ThermoriginsForge::commonSetup);
        ModLoadingContext modLoadingContext = ModLoadingContext.get();
        Thermorigins.LOGGER.info(String.format("Thermorigins %1$S has initialized in Forge. Have fun with the %2$S origin!", modLoadingContext.getActiveContainer().getModInfo().getVersion(), Thermorigins.randomOriginName()));
    }
    
    public static void commonSetup(FMLCommonSetupEvent event) {
        event.enqueueWork(ThermoLootFunctions::registerAll);
    }
    
}