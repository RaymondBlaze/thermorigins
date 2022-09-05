package dev.limonblaze.thermorigins.mixin;

import dev.limonblaze.thermorigins.power.ModifyToolPower;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = Inventory.class, priority = 2000)
public class InventoryMixin {
    
    @Shadow @Final public Player player;
    
    @Inject(method = "getDestroySpeed", at = @At("RETURN"), cancellable = true)
    private void thermorigins$modifyDestroySpeed(BlockState state, CallbackInfoReturnable<Float> cir) {
        cir.setReturnValue(ModifyToolPower.getBreakSpeed(player, state, cir.getReturnValue()));
    }
    
}
