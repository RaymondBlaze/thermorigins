package dev.limonblaze.thermorigins.registry;

import dev.limonblaze.thermorigins.Thermorigins;
import dev.limonblaze.thermorigins.platform.Services;
import dev.limonblaze.thermorigins.power.IgnoreFoodPower;
import dev.limonblaze.thermorigins.power.ModifyToolPower;
import dev.limonblaze.thermorigins.power.data.IPowerData;
import dev.limonblaze.thermorigins.power.data.IgnoreFoodPowerData;
import io.github.apace100.apoli.power.Power;
import io.github.apace100.apoli.power.factory.PowerFactory;
import net.minecraft.resources.ResourceLocation;

public class ThermoPowers {
    
    public static final PowerFactory<IgnoreFoodPower> IGNORE_FOOD = register("ignore_food", new IgnoreFoodPowerData());
    
    public static final PowerFactory<ModifyToolPower> MODIFY_TOOL = register("modify_tool", Services.MODIFY_TOOL_POWER_DATA);
    
    public static final ResourceLocation STEAM_POWER_RESOURCE = Thermorigins.asResource("steampowered/steam_power_resource");
    
    public static <P extends Power> PowerFactory<P> register(String name, IPowerData<P> data) {
        return Services.PLATFORM.registerPowerFactory(Thermorigins.asResource(name), data);
    }
    
    public static void registerAll() {}
    
}
