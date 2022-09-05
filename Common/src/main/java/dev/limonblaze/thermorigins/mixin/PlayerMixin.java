package dev.limonblaze.thermorigins.mixin;

import dev.limonblaze.thermorigins.power.ModifyToolPower;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Player.class)
public class PlayerMixin {
    
    @Inject(method = "hasCorrectToolForDrops", at = @At("RETURN"), cancellable = true)
    private void thermorigins$modifyTool(BlockState state, CallbackInfoReturnable<Boolean> cir) {
        if(ModifyToolPower.isCorrectTool(((Player)(Object)this), state)) {
            cir.setReturnValue(true);
        }
    }
    
}
