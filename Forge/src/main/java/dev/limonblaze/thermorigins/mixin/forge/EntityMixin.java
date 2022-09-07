package dev.limonblaze.thermorigins.mixin.forge;

import dev.limonblaze.thermorigins.data.WeightedConfiguredSoundEvent;
import dev.limonblaze.thermorigins.power.ReplaceSoundEventPower;
import dev.limonblaze.thermorigins.registry.ThermoPowersForge;
import io.github.edwinmindcraft.apoli.api.component.IPowerContainer;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Entity.class)
public abstract class EntityMixin {
    
    @Shadow public Level level;
    
    @Shadow private Vec3 position;
    
    @Shadow public abstract SoundSource getSoundSource();
    
    @Inject(method = "playStepSound", at = @At("HEAD"), cancellable = true)
    private void modifyStepSound(BlockPos pos, BlockState state, CallbackInfo ci) {
        if(state.getMaterial().isLiquid() || !(((Object) this) instanceof LivingEntity living)) return;
        var powers = IPowerContainer.getPowers(living, ThermoPowersForge.CUSTOM_FOOTSTEP.get());
        if(powers.isEmpty()) return;
        if(powers.stream().noneMatch(power -> power.getConfiguration().muted())) {
            for(var power : powers) {
                ReplaceSoundEventPower.playSound(power, living, false);
            }
        }
        ci.cancel();
    }
    
}
