package dev.limonblaze.thermorigins.mixin;

import dev.limonblaze.thermorigins.platform.Services;
import dev.limonblaze.thermorigins.power.PreventDrowningPower;
import dev.limonblaze.thermorigins.registry.ThermoPowers;
import net.minecraft.world.effect.MobEffectUtil;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(MobEffectUtil.class)
public class MobEffectUtilMixin {
    
    @Inject(method = "hasWaterBreathing", at = @At("HEAD"), cancellable = true)
    private static void preventDrowning(LivingEntity entity, CallbackInfoReturnable<Boolean> cir) {
        if(Services.PLATFORM.hasPower(entity, PreventDrowningPower.class, ThermoPowers.PREVENT_DROWNING)) {
            cir.setReturnValue(true);
        }
    }
    
}
