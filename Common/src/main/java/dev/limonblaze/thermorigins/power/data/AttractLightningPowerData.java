package dev.limonblaze.thermorigins.power.data;

import dev.limonblaze.thermorigins.power.AttractLightningPower;
import io.github.apace100.apoli.power.PowerType;
import io.github.apace100.calio.data.SerializableData;
import net.minecraft.world.entity.LivingEntity;

import java.util.function.BiFunction;

public class AttractLightningPowerData implements IPowerData<AttractLightningPower> {
    
    @Override
    public BiFunction<PowerType<AttractLightningPower>, LivingEntity, AttractLightningPower> getPowerConstructor(SerializableData.Instance data) {
        return AttractLightningPower::new;
    }
    
}
