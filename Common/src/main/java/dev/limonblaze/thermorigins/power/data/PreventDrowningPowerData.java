package dev.limonblaze.thermorigins.power.data;

import dev.limonblaze.thermorigins.power.PreventDrowningPower;
import io.github.apace100.apoli.power.PowerType;
import io.github.apace100.calio.data.SerializableData;
import net.minecraft.world.entity.LivingEntity;

import java.util.function.BiFunction;

public class PreventDrowningPowerData implements IPowerData<PreventDrowningPower> {
    
    @Override
    public BiFunction<PowerType<PreventDrowningPower>, LivingEntity, PreventDrowningPower> getPowerConstructor(SerializableData.Instance data) {
        return PreventDrowningPower::new;
    }
    
}
