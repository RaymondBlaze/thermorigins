package dev.limonblaze.thermorigins.platform;

import dev.limonblaze.thermorigins.Thermorigins;
import dev.limonblaze.thermorigins.platform.services.IPlatformHelper;
import dev.limonblaze.thermorigins.power.data.IModifyToolPowerData;

import java.util.ServiceLoader;

public class Services {

    public static final IPlatformHelper PLATFORM = load(IPlatformHelper.class);
    public static final IModifyToolPowerData MODIFY_TOOL_POWER_DATA = load(IModifyToolPowerData.class);

    public static <T> T load(Class<T> clazz) {

        final T loadedService = ServiceLoader.load(clazz)
                .findFirst()
                .orElseThrow(() -> new NullPointerException("Failed to load service for " + clazz.getName()));
        Thermorigins.LOGGER.debug("Loaded {} for service {}", loadedService, clazz);
        return loadedService;
    }
    
}
