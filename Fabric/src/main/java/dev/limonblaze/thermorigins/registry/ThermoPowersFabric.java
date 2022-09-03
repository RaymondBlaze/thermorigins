package dev.limonblaze.thermorigins.registry;

import dev.limonblaze.thermorigins.Thermorigins;
import dev.limonblaze.thermorigins.power.FurnacePower;
import dev.limonblaze.thermorigins.registry.services.RegistrationProvider;
import dev.limonblaze.thermorigins.registry.services.RegistryObject;
import io.github.apace100.apoli.power.Power;
import io.github.apace100.apoli.power.factory.PowerFactory;
import io.github.apace100.apoli.registry.ApoliRegistries;
import net.minecraft.resources.ResourceLocation;

import java.util.function.Function;

@SuppressWarnings("rawtypes")
public class ThermoPowersFabric {
    public static final RegistrationProvider<PowerFactory> REGISTRY = RegistrationProvider.get(ApoliRegistries.POWER_FACTORY, Thermorigins.ID);
    
    public static final RegistryObject<PowerFactory<FurnacePower>> FURNACE_RESOURCE = register("furnace_resource", FurnacePower::createFactory);
    
    private static <P extends Power> RegistryObject<PowerFactory<P>> register(String name, Function<ResourceLocation, PowerFactory<P>> factory) {
        return REGISTRY.register(name, () -> factory.apply(Thermorigins.asResource(name)));
    }
    
    public static void registerAll() {}
    
}
