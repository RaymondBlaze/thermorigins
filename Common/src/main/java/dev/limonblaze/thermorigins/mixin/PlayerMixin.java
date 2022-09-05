package dev.limonblaze.thermorigins.mixin;

import dev.limonblaze.thermorigins.power.ModifyToolPower;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Player.class)
public abstract class PlayerMixin extends LivingEntity {
    
    protected PlayerMixin(EntityType<? extends LivingEntity> type, Level level) {
        super(type, level);
    }
    
    @Inject(method = "hasCorrectToolForDrops", at = @At("HEAD"), cancellable = true)
    private void modifyTool(BlockState state, CallbackInfoReturnable<Boolean> cir) {
        if(ModifyToolPower.isCorrectTool(this, state)) {
            cir.setReturnValue(true);
        }
    }
    
}
