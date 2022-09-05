package dev.limonblaze.thermorigins.power;

import io.github.apace100.apoli.power.Power;
import io.github.apace100.apoli.power.PowerType;
import net.minecraft.world.entity.LivingEntity;

public class AttractLightningPower extends Power {
    
    public AttractLightningPower(PowerType<?> type, LivingEntity entity) {
        super(type, entity);
    }
    
}
