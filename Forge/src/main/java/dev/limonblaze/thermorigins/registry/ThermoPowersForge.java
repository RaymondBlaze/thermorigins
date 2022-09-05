package dev.limonblaze.thermorigins.registry;

import dev.limonblaze.thermorigins.Thermorigins;
import dev.limonblaze.thermorigins.power.FurnacePower;
import dev.limonblaze.thermorigins.registry.services.RegistrationProvider;
import dev.limonblaze.thermorigins.registry.services.RegistryObject;
import io.github.edwinmindcraft.apoli.api.power.factory.PowerFactory;
import io.github.edwinmindcraft.apoli.api.registry.ApoliRegistries;

public class ThermoPowersForge {
    
    public static final RegistrationProvider<PowerFactory<?>> REGISTRY = RegistrationProvider.get(ApoliRegistries.POWER_FACTORY_KEY, Thermorigins.ID);
    
    public static final RegistryObject<FurnacePower> FURNACE = REGISTRY.register("furnace", () -> new FurnacePower());
    
    public static void registerAll() {}
    
}
