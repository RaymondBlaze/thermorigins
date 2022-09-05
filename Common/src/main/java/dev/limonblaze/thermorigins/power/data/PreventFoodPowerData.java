package dev.limonblaze.thermorigins.power.data;

import dev.limonblaze.thermorigins.power.PreventFoodUpdatePower;
import io.github.apace100.apoli.power.PowerType;
import io.github.apace100.calio.data.SerializableData;
import net.minecraft.world.entity.LivingEntity;

import java.util.function.BiFunction;

public class PreventFoodPowerData implements IPowerData<PreventFoodUpdatePower> {
    
    @Override
    public BiFunction<PowerType<PreventFoodUpdatePower>, LivingEntity, PreventFoodUpdatePower> getPowerConstructor(SerializableData.Instance data) {
        return PreventFoodUpdatePower::new;
    }
    
}
