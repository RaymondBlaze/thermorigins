package dev.limonblaze.thermorigins.mixin;

import dev.limonblaze.thermorigins.power.RedirectLightningPower;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.Optional;

@Mixin(ServerLevel.class)
public abstract class ServerLevelMixin {
    
    @Shadow protected abstract Optional<BlockPos> findLightningRod(BlockPos pos);
    
    @Redirect(method = "findLightningTargetAround", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/level/ServerLevel;findLightningRod(Lnet/minecraft/core/BlockPos;)Ljava/util/Optional;"))
    private Optional<BlockPos> redirectLightning(ServerLevel level, BlockPos lightningPos) {
        return RedirectLightningPower.redirectLightningPos(level, lightningPos, this.findLightningRod(lightningPos).orElse(null));
    }
    
}
