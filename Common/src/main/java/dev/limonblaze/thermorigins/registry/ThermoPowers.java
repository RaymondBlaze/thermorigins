package dev.limonblaze.thermorigins.registry;

import dev.limonblaze.thermorigins.Thermorigins;
import dev.limonblaze.thermorigins.platform.Services;
import dev.limonblaze.thermorigins.power.AttractLightningPower;
import dev.limonblaze.thermorigins.power.PreventDrowningPower;
import dev.limonblaze.thermorigins.power.PreventFoodUpdatePower;
import dev.limonblaze.thermorigins.power.ModifyToolPower;
import dev.limonblaze.thermorigins.power.data.AttractLightningPowerData;
import dev.limonblaze.thermorigins.power.data.IPowerData;
import dev.limonblaze.thermorigins.power.data.PreventDrowningPowerData;
import dev.limonblaze.thermorigins.power.data.PreventFoodPowerData;
import io.github.apace100.apoli.power.Power;
import io.github.apace100.apoli.power.factory.PowerFactory;
import net.minecraft.resources.ResourceLocation;

public class ThermoPowers {
    
    public static final PowerFactory<AttractLightningPower> ATTRACT_LIGHTNING = register("attract_lightning", new AttractLightningPowerData());
    
    public static final PowerFactory<ModifyToolPower> MODIFY_TOOL = register("modify_tool", Services.MODIFY_TOOL_POWER_DATA);
    
    public static final PowerFactory<PreventFoodUpdatePower> PREVENT_FOOD_UPDATE = register("prevent_food_update", new PreventFoodPowerData());
    
    public static final PowerFactory<PreventDrowningPower> PREVENT_DROWNING = register("prevent_drowning", new PreventDrowningPowerData());
    
    public static final ResourceLocation STEAM_POWER_RESOURCE = Thermorigins.asResource("steampowered/steam_power_resource");
    
    public static <P extends Power> PowerFactory<P> register(String name, IPowerData<P> data) {
        return Services.PLATFORM.registerPowerFactory(Thermorigins.asResource(name), data);
    }
    
    public static void registerAll() {}
    
}
