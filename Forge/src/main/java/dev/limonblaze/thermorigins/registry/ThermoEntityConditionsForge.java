package dev.limonblaze.thermorigins.registry;

import com.mojang.serialization.MapCodec;
import dev.limonblaze.thermorigins.Thermorigins;
import dev.limonblaze.thermorigins.data.FurnaceFactory;
import dev.limonblaze.thermorigins.power.FurnacePower;
import dev.limonblaze.thermorigins.registry.services.RegistrationProvider;
import dev.limonblaze.thermorigins.registry.services.RegistryObject;
import io.github.apace100.calio.data.SerializableDataTypes;
import io.github.edwinmindcraft.apoli.api.ApoliAPI;
import io.github.edwinmindcraft.apoli.api.component.IPowerContainer;
import io.github.edwinmindcraft.apoli.api.power.configuration.ConfiguredPower;
import io.github.edwinmindcraft.apoli.api.power.factory.EntityCondition;
import io.github.edwinmindcraft.apoli.api.registry.ApoliRegistries;
import io.github.edwinmindcraft.apoli.common.condition.entity.FloatComparingEntityCondition;
import io.github.edwinmindcraft.apoli.common.condition.entity.IntComparingEntityCondition;
import io.github.edwinmindcraft.apoli.common.condition.entity.SimpleEntityCondition;
import io.github.edwinmindcraft.apoli.common.condition.entity.SingleFieldEntityCondition;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.fml.DistExecutor;

import java.util.function.BiPredicate;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

public class ThermoEntityConditionsForge {
    
    public static final RegistrationProvider<EntityCondition<?>> REGISTRY = RegistrationProvider.get(ApoliRegistries.ENTITY_CONDITION_KEY, Thermorigins.ID);
    
    public static final RegistryObject<SingleFieldEntityCondition<ResourceLocation>> FURNACE_ACTIVE = register("furnace_active", SerializableDataTypes.IDENTIFIER.fieldOf("power"),
        (entity, id) -> {
            IPowerContainer container = ApoliAPI.getPowerContainer(entity);
            if(container == null) return false;
            ConfiguredPower<?, ?> power = container.getPower(id);
            if(power != null && power.getFactory() == ThermoPowersForge.FURNACE.get() && power.isActive(entity)) {
                FurnaceFactory.Instance furnace = ThermoPowersForge.FURNACE.get().getInventory((ConfiguredPower<FurnacePower.Configuration, ?>) power, container);
                return furnace.isLit();
            }
            return false;
        });
    
    private static <T extends EntityCondition<?>> RegistryObject<T> register(String name, Supplier<T> factory) {
        return REGISTRY.register(name, factory);
    }
    
    private static RegistryObject<SimpleEntityCondition> register(String name, Predicate<Entity> factory) {
        return register(name, () -> new SimpleEntityCondition(factory));
    }
    
    private static <T> RegistryObject<SingleFieldEntityCondition<T>> register(String name, MapCodec<T> codec, BiPredicate<Entity, T> predicate) {
        return register(name, () -> new SingleFieldEntityCondition<>(codec, predicate));
    }
    
    private static RegistryObject<SimpleEntityCondition> registerLiving(String name, Predicate<LivingEntity> factory) {
        return register(name, () -> new SimpleEntityCondition((x) -> x instanceof LivingEntity living && factory.test(living)));
    }
    
    private static RegistryObject<SimpleEntityCondition> registerPlayer(String name, Predicate<Player> factory) {
        return register(name, () -> new SimpleEntityCondition((x) -> x instanceof Player player && factory.test(player)));
    }
    
    private static RegistryObject<IntComparingEntityCondition> registerInt(String name, Function<Entity, Integer> factory) {
        return register(name, () -> new IntComparingEntityCondition(factory));
    }
    
    private static RegistryObject<IntComparingEntityCondition> registerIntPlayer(String name, Function<Player, Integer> factory) {
        return registerInt(name, (living) -> living instanceof Player player ? factory.apply(player) : null);
    }
    
    private static RegistryObject<FloatComparingEntityCondition> registerFloat(String name, Function<Entity, Float> factory) {
        return register(name, () -> new FloatComparingEntityCondition(factory));
    }
    
    private static RegistryObject<FloatComparingEntityCondition> registerFloatLiving(String name, Function<LivingEntity, Float> factory) {
        return register(name, () -> new FloatComparingEntityCondition((x) -> x instanceof LivingEntity living ? factory.apply(living) : null));
    }
    
    private static <T extends EntityCondition<?>> RegistryObject<T> registerSided(String name, Supplier<Supplier<T>> client, Supplier<Supplier<T>> server) {
        return register(name, () -> DistExecutor.unsafeRunForDist(client, server));
    }
    
    public static void registerAll() {}
    
}
