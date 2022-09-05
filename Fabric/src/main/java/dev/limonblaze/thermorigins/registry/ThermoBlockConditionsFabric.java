package dev.limonblaze.thermorigins.registry;

import dev.limonblaze.thermorigins.Thermorigins;
import dev.limonblaze.thermorigins.registry.services.RegistrationProvider;
import dev.limonblaze.thermorigins.registry.services.RegistryObject;
import io.github.apace100.apoli.power.factory.condition.ConditionFactory;
import io.github.apace100.apoli.registry.ApoliRegistries;
import io.github.apace100.calio.data.SerializableData;
import io.github.apace100.calio.data.SerializableDataTypes;
import net.fabricmc.fabric.api.mininglevel.v1.MiningLevelManager;
import net.minecraft.world.level.block.state.pattern.BlockInWorld;

import java.util.function.BiFunction;

public class ThermoBlockConditionsFabric {
    
    public static final RegistrationProvider<ConditionFactory<BlockInWorld>> REGISTRY = RegistrationProvider.get(ApoliRegistries.BLOCK_CONDITION, Thermorigins.ID);
    
    public static final RegistryObject<ConditionFactory<BlockInWorld>> TIER_HARVESTABLE = register("tier_harvestable", new SerializableData()
        .add("tier_level", SerializableDataTypes.INT),
        (data, block) -> MiningLevelManager.getRequiredMiningLevel(block.getState()) <= data.getInt("tier_level")
    );
    
    public static RegistryObject<ConditionFactory<BlockInWorld>> register(String name, SerializableData data, BiFunction<SerializableData.Instance, BlockInWorld, Boolean> condition) {
        return REGISTRY.register(name, () -> new ConditionFactory<>(Thermorigins.asResource(name), data, condition));
    }
    
    public static void registerAll() {}
    
}
