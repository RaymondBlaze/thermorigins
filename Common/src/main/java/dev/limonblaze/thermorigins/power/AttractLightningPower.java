package dev.limonblaze.thermorigins.power;

import dev.limonblaze.thermorigins.mixin.ServerLevelMixin;
import io.github.apace100.apoli.power.Power;
import io.github.apace100.apoli.power.PowerType;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.entity.LivingEntity;

/**
 * Power that makes <b>player</b> attract {@link LightningBolt}s within 128bl.
 * @see ServerLevelMixin
 */
public class AttractLightningPower extends Power {
    
    public AttractLightningPower(PowerType<?> type, LivingEntity entity) {
        super(type, entity);
    }
    
}
