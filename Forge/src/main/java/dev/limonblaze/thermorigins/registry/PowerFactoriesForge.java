package dev.limonblaze.thermorigins.registry;

import dev.limonblaze.thermorigins.Thermorigins;
import dev.limonblaze.thermorigins.registry.services.RegistrationProvider;
import io.github.edwinmindcraft.apoli.api.power.factory.PowerFactory;
import io.github.edwinmindcraft.apoli.api.registry.ApoliRegistries;

public class PowerFactoriesForge {
    public static final RegistrationProvider<PowerFactory<?>> POWER_FACTORY_REGISTRY = RegistrationProvider.get(ApoliRegistries.POWER_FACTORY_KEY, Thermorigins.ID);
}
