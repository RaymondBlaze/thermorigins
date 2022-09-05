package dev.limonblaze.thermorigins;

import dev.limonblaze.thermorigins.registry.ThermoBlockConditionsForge;
import dev.limonblaze.thermorigins.registry.ThermoEntityConditionsForge;
import dev.limonblaze.thermorigins.registry.ThermoLootFunctions;
import dev.limonblaze.thermorigins.registry.ThermoPowersForge;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
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
        Thermorigins.LOGGER.info("Hello Forge world!");
    }
    
    public static void commonSetup(FMLCommonSetupEvent event) {
        event.enqueueWork(ThermoLootFunctions::registerAll);
    }
    
}