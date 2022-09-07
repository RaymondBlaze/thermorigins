package dev.limonblaze.thermorigins.mixin.forge;

import dev.limonblaze.thermorigins.power.ReplaceSoundEventPower;
import dev.limonblaze.thermorigins.registry.ThermoPowersForge;
import io.github.edwinmindcraft.apoli.api.component.IPowerContainer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.Slice;

@Mixin(LivingEntity.class)
public class LivingEntityMixin {
    
    @ModifyVariable(
        method = "handleEntityEvent",
        at = @At(
            value = "INVOKE_ASSIGN",
            target = "Lnet/minecraft/world/entity/LivingEntity;getDeathSound()Lnet/minecraft/sounds/SoundEvent;"
        ),
        ordinal = 1
    )
    @SuppressWarnings("InvalidInjectorMethodSignature")
    private SoundEvent modifyDeathSound(SoundEvent value) {
        var powers = IPowerContainer.getPowers((LivingEntity)(Object)this, ThermoPowersForge.CUSTOM_DEATH_SOUND.get());
        if(powers.isEmpty()) return value;
        if(powers.stream().noneMatch(power -> power.getConfiguration().muted())) {
            for(var power : powers) {
                ReplaceSoundEventPower.playSound(power, (LivingEntity)(Object)this, true);
            }
        }
        return null;
    }
    
    @ModifyVariable(
        method = "handleEntityEvent",
        slice = @Slice(
            from = @At(value = "FIELD", target = "Lnet/minecraft/world/entity/LivingEntity;hurtDir:F"),
            to = @At(value = "FIELD", target = "Lnet/minecraft/world/entity/LivingEntity;lastDamageSource:Lnet/minecraft/world/damagesource/DamageSource;")
        ),
        at = @At(
            value = "INVOKE_ASSIGN",
            target = "Lnet/minecraft/world/entity/LivingEntity;getHurtSound(Lnet/minecraft/world/damagesource/DamageSource;)Lnet/minecraft/sounds/SoundEvent;"
        ),
        ordinal = 0
    )
    @SuppressWarnings("InvalidInjectorMethodSignature")
    private SoundEvent modifyHurtSound(SoundEvent value) {
        var powers = IPowerContainer.getPowers((LivingEntity)(Object)this, ThermoPowersForge.CUSTOM_HURT_SOUND.get());
        if(powers.isEmpty()) return value;
        if(powers.stream().noneMatch(power -> power.getConfiguration().muted())) {
            for(var power : powers) {
                ReplaceSoundEventPower.playSound(power, (LivingEntity)(Object)this, true);
            }
        }
        return null;
    }
    
}
