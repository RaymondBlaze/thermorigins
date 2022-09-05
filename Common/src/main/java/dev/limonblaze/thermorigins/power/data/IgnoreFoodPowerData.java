package dev.limonblaze.thermorigins.power.data;

import dev.limonblaze.thermorigins.power.IgnoreFoodPower;
import io.github.apace100.apoli.power.PowerType;
import io.github.apace100.calio.data.SerializableData;
import net.minecraft.world.entity.LivingEntity;

import java.util.function.BiFunction;

public class IgnoreFoodPowerData implements IPowerData<IgnoreFoodPower> {
    
    @Override
    public BiFunction<PowerType<IgnoreFoodPower>, LivingEntity, IgnoreFoodPower> getPowerConstructor(SerializableData.Instance data) {
        return IgnoreFoodPower::new;
    }
    
}
