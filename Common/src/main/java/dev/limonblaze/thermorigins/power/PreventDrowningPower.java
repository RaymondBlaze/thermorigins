package dev.limonblaze.thermorigins.power;

import dev.limonblaze.thermorigins.mixin.MobEffectUtilMixin;
import io.github.apace100.apoli.power.Power;
import io.github.apace100.apoli.power.PowerType;
import net.minecraft.world.effect.MobEffectUtil;
import net.minecraft.world.entity.LivingEntity;

/**
 * Power that prevents entity from consuming air in water.
 * @see MobEffectUtilMixin
 */
public class PreventDrowningPower extends Power {
    
    public PreventDrowningPower(PowerType<?> type, LivingEntity entity) {
        super(type, entity);
    }
    
}
