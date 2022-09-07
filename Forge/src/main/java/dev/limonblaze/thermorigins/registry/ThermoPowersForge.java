package dev.limonblaze.thermorigins.registry;

import dev.limonblaze.thermorigins.Thermorigins;
import dev.limonblaze.thermorigins.power.FurnacePower;
import dev.limonblaze.thermorigins.power.ReplaceSoundEventPower;
import dev.limonblaze.thermorigins.registry.services.RegistrationProvider;
import dev.limonblaze.thermorigins.registry.services.RegistryObject;
import io.github.edwinmindcraft.apoli.api.power.factory.PowerFactory;
import io.github.edwinmindcraft.apoli.api.registry.ApoliRegistries;

public class ThermoPowersForge {
    
    public static final RegistrationProvider<PowerFactory<?>> REGISTRY = RegistrationProvider.get(ApoliRegistries.POWER_FACTORY_KEY, Thermorigins.ID);
    
    public static final RegistryObject<FurnacePower> FURNACE = REGISTRY.register("furnace", () -> new FurnacePower());
    
    public static final RegistryObject<ReplaceSoundEventPower> CUSTOM_DEATH_SOUND = REGISTRY.register("custom_death_sound", () -> new ReplaceSoundEventPower());
    
    public static final RegistryObject<ReplaceSoundEventPower> CUSTOM_FOOTSTEP = REGISTRY.register("custom_footstep", () -> new ReplaceSoundEventPower());
    
    public static final RegistryObject<ReplaceSoundEventPower> CUSTOM_HURT_SOUND = REGISTRY.register("custom_hurt_sound", () -> new ReplaceSoundEventPower());
    
    public static void registerAll() {}
    
}
