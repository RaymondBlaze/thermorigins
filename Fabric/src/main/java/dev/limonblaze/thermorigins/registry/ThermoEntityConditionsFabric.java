package dev.limonblaze.thermorigins.registry;

import dev.limonblaze.thermorigins.Thermorigins;
import dev.limonblaze.thermorigins.power.FurnacePower;
import dev.limonblaze.thermorigins.registry.services.RegistrationProvider;
import dev.limonblaze.thermorigins.registry.services.RegistryObject;
import io.github.apace100.apoli.data.ApoliDataTypes;
import io.github.apace100.apoli.power.PowerTypeReference;
import io.github.apace100.apoli.power.factory.condition.ConditionFactory;
import io.github.apace100.apoli.registry.ApoliRegistries;
import io.github.apace100.calio.data.SerializableData;
import net.minecraft.world.entity.Entity;

import java.util.function.BiFunction;

public class ThermoEntityConditionsFabric {
    
    public static final RegistrationProvider<ConditionFactory<Entity>> REGISTRY = RegistrationProvider.get(ApoliRegistries.ENTITY_CONDITION, Thermorigins.ID);
    
    public static final RegistryObject<ConditionFactory<Entity>> FURNACE_ACTIVE = register("furnace_active", new SerializableData()
        .add("power", ApoliDataTypes.POWER_TYPE),
        (data, entity) -> {
            PowerTypeReference<?> powerType = data.get("power");
            if(powerType.isActive(entity) && powerType.get(entity) instanceof FurnacePower furnacePower) {
                return furnacePower.getFurnace().isLit();
            }
            return false;
        }
    );
    
    public static RegistryObject<ConditionFactory<Entity>> register(String name, SerializableData data, BiFunction<SerializableData.Instance, Entity, Boolean> condition) {
        return REGISTRY.register(name, () -> new ConditionFactory<>(Thermorigins.asResource(name), data, condition));
    }
    
    public static void registerAll() {}
    
}
