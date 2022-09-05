package dev.limonblaze.thermorigins.mixin.forge;

import dev.limonblaze.thermorigins.access.FoodDataPlayerAccess;
import dev.limonblaze.thermorigins.power.IgnoreFoodPower;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.food.FoodData;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = FoodData.class, priority = 2000)
public class FoodDataMixin {
    
    @Inject(
        method = "eat(Lnet/minecraft/world/item/Item;Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/entity/LivingEntity;)V",
        at = @At("HEAD"), cancellable = true
    )
    private void eatItemByEntity(Item item, ItemStack stack, LivingEntity entity, CallbackInfo ci) {
        if(IgnoreFoodPower.isActive((FoodDataPlayerAccess) this)) ci.cancel();
    }
    
}
