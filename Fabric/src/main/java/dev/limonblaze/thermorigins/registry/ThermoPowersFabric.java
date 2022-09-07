package dev.limonblaze.thermorigins.registry;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import dev.limonblaze.thermorigins.Thermorigins;
import dev.limonblaze.thermorigins.power.FurnacePower;
import dev.limonblaze.thermorigins.registry.services.RegistrationProvider;
import dev.limonblaze.thermorigins.registry.services.RegistryObject;
import io.github.apace100.apoli.Apoli;
import io.github.apace100.apoli.integration.PrePowerLoadCallback;
import io.github.apace100.apoli.power.Power;
import io.github.apace100.apoli.power.factory.PowerFactory;
import io.github.apace100.apoli.registry.ApoliRegistries;
import io.github.apace100.apoli.util.NamespaceAlias;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import static dev.limonblaze.thermorigins.ThermoriginsFabric.APUGLI_ID;

@SuppressWarnings("rawtypes")
public class ThermoPowersFabric {
    
    public static final RegistrationProvider<PowerFactory> REGISTRY = RegistrationProvider.get(ApoliRegistries.POWER_FACTORY, Thermorigins.ID);
    /**
     * We can't use {@link NamespaceAlias} here because Apugli does not serve as fallback for all Thermorigins powers.
     */
    public static final Map<ResourceLocation, ResourceLocation> POWER_TYPE_ALIASES = new HashMap<>();
    public static final ResourceLocation MULTIPLE = Apoli.identifier("multiple");
    
    public static final RegistryObject<PowerFactory<FurnacePower>> FURNACE = register("furnace", FurnacePower::createFactory);
    
    private static <P extends Power> RegistryObject<PowerFactory<P>> register(String name, Function<ResourceLocation, PowerFactory<P>> factory) {
        return REGISTRY.register(name, () -> factory.apply(Thermorigins.asResource(name)));
    }
    
    public static void registerAll() {
        registerAlias(APUGLI_ID, "custom_death_sound");
        registerAlias(APUGLI_ID, "custom_footstep");
        registerAlias(APUGLI_ID, "custom_hurt_sound");
        PrePowerLoadCallback.EVENT.register((id, power) -> {
            if(power.has("type")) {
                ResourceLocation type = ResourceLocation.tryParse(GsonHelper.getAsString(power, "type"));
                if(type == null) return;
                if(isMultiple(type)) {
                    for(var entry : power.entrySet()) {
                        if(entry.getValue() instanceof JsonObject subPower && subPower.has("type")) {
                            ResourceLocation subType = ResourceLocation.tryParse(GsonHelper.getAsString(subPower, "type"));
                            if(subType == null) continue;
                            handleAlias(subPower, new ResourceLocation(id.getNamespace(), id.getPath() + "_" + entry.getKey()), subType);
                        }
                    }
                } else handleAlias(power, id, type);
            }
        });
    }

    private static void registerAlias(String namespace, String path) {
        POWER_TYPE_ALIASES.put(Thermorigins.asResource(path), new ResourceLocation(namespace, path));
    }
    
    private static void handleAlias(JsonObject power, ResourceLocation powerId, ResourceLocation type) {
        if(POWER_TYPE_ALIASES.containsKey(type)) {
            ResourceLocation newType = POWER_TYPE_ALIASES.get(type);
            power.addProperty("type", newType.toString());
            Thermorigins.LOGGER.info("Remapped power type [{}] into [{}] for power [{}].", type, newType, powerId);
        }
    }
    
    private static boolean isMultiple(ResourceLocation id) {
        if(MULTIPLE.equals(id)) {
            return true;
        } else {
            return NamespaceAlias.hasAlias(id) && MULTIPLE.equals(NamespaceAlias.resolveAlias(id));
        }
    }
    
}
