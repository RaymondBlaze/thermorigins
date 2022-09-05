package dev.limonblaze.thermorigins.registry;

import dev.limonblaze.thermorigins.Thermorigins;
import dev.limonblaze.thermorigins.condition.block.TierHarvestableBlockCondition;
import dev.limonblaze.thermorigins.registry.services.RegistrationProvider;
import dev.limonblaze.thermorigins.registry.services.RegistryObject;
import io.github.edwinmindcraft.apoli.api.power.factory.BlockCondition;
import io.github.edwinmindcraft.apoli.api.registry.ApoliRegistries;

public class ThermoBlockConditionsForge {
    
    public static final RegistrationProvider<BlockCondition<?>> REGISTRY = RegistrationProvider.get(ApoliRegistries.BLOCK_CONDITION_KEY, Thermorigins.ID);
    
    public static final RegistryObject<BlockCondition<?>> TIER_HARVESTABLE = REGISTRY.register("tier_harvestable", TierHarvestableBlockCondition::new);
    
    public static void registerAll() {}
    
}
