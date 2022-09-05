package dev.limonblaze.thermorigins.mixin;

import dev.limonblaze.thermorigins.platform.Services;
import dev.limonblaze.thermorigins.power.AttractLightningPower;
import dev.limonblaze.thermorigins.registry.ThermoPowers;
import net.minecraft.core.BlockPos;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.BlockAndTintGetter;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Comparator;
import java.util.List;

@Mixin(ServerLevel.class)
public abstract class ServerLevelMixin implements BlockAndTintGetter {
    
    @Shadow @Final private MinecraftServer server;
    
    @Shadow public abstract List<ServerPlayer> players();
    
    @Inject(method = "findLightningTargetAround", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/phys/AABB;<init>(Lnet/minecraft/core/BlockPos;Lnet/minecraft/core/BlockPos;)V"), cancellable = true)
    private void findAttractLightningPlayer(BlockPos pos, CallbackInfoReturnable<BlockPos> cir) {
        this.players().stream()
            .filter(player -> Services.PLATFORM.hasPower(player, AttractLightningPower.class, ThermoPowers.ATTRACT_LIGHTNING))
            .map(Entity::blockPosition)
            .filter(this::canSeeSky)
            .min(Comparator.comparing(foundPos -> foundPos.distSqr(pos)))
            .ifPresent(foundPos -> {
                if(foundPos.distSqr(pos) < 128 * 128) {
                    cir.setReturnValue(foundPos);
                }
            });
    }
    
}
