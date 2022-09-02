package com.example.examplemod.registry;

import com.example.examplemod.Constants;
import com.example.examplemod.registry.services.RegistrationProvider;
import io.github.edwinmindcraft.apoli.api.power.factory.PowerFactory;
import io.github.edwinmindcraft.apoli.api.registry.ApoliRegistries;

public class PowerFactoriesForge {
    public static final RegistrationProvider<PowerFactory<?>> POWER_FACTORY_REGISTRY = RegistrationProvider.get(ApoliRegistries.POWER_FACTORY_KEY, Constants.MOD_ID);
}
