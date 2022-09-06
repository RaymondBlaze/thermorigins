package dev.limonblaze.thermorigins.mixin;

import dev.limonblaze.thermorigins.access.FoodDataPlayerAccess;
import dev.limonblaze.thermorigins.platform.Services;
import dev.limonblaze.thermorigins.power.PreventFoodUpdatePower;
import dev.limonblaze.thermorigins.registry.ThermoPowers;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodData;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Implements;
import org.spongepowered.asm.mixin.Interface;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import javax.annotation.Nullable;

@Mixin(value = FoodData.class, priority = 2000)
@Implements(value = @Interface(iface = FoodDataPlayerAccess.class, prefix = "thermorigins$"))
public class FoodDataMixin {
    
    @Shadow private int foodLevel;
    @Shadow private int lastFoodLevel;
    @Shadow private float saturationLevel;
    @Shadow private float exhaustionLevel;
    @Shadow private int tickTimer;
    
    @Nullable
    private Player thermorigins$player;
    private boolean thermorigins$preventFoodUpdate;
    private int thermorigins$foodLevel;
    private int thermorigins$lastFoodLevel;
    private float thermorigins$saturationLevel;
    private float thermorigins$exhaustionLevel;
    private int thermorigins$tickTimer;
    private float thermorigins$steamPowerConsumption;
    
    @Nullable
    public Player thermorigins$getPlayer() {
        return thermorigins$player;
    }
    
    @Inject(method = "tick", at = @At("HEAD"), cancellable = true)
    private void lockFoodData(Player player, CallbackInfo ci) {
        thermorigins$player = player;
        boolean ignore = Services.PLATFORM.hasPower(player, PreventFoodUpdatePower.class, ThermoPowers.PREVENT_FOOD_UPDATE);
        if(ignore) {
            if(!thermorigins$preventFoodUpdate) {
                thermorigins$preventFoodUpdate = true;
                thermorigins$foodLevel = foodLevel;
                thermorigins$lastFoodLevel = lastFoodLevel;
                thermorigins$saturationLevel = saturationLevel;
                thermorigins$exhaustionLevel = exhaustionLevel;
                thermorigins$tickTimer = tickTimer;
                foodLevel = 20;
                lastFoodLevel = 20;
                saturationLevel = 0;
                exhaustionLevel = 0;
                tickTimer = 0;
            }
            ci.cancel();
        } else if(thermorigins$preventFoodUpdate) {
            thermorigins$preventFoodUpdate = false;
            foodLevel = thermorigins$foodLevel;
            lastFoodLevel = thermorigins$lastFoodLevel;
            saturationLevel = thermorigins$saturationLevel;
            exhaustionLevel = thermorigins$exhaustionLevel;
            tickTimer = thermorigins$tickTimer;
        }
    }
    
    @Inject(method = "eat(IF)V", at = @At("HEAD"), cancellable = true)
    private void preventEatNonItem(int nutrition, float saturation, CallbackInfo ci) {
        if(thermorigins$preventFoodUpdate) ci.cancel();
    }
    
    @Inject(method = "eat(Lnet/minecraft/world/item/Item;Lnet/minecraft/world/item/ItemStack;)V", at = @At("HEAD"), cancellable = true)
    private void preventEatItem(Item item, ItemStack stack, CallbackInfo ci) {
        if(thermorigins$preventFoodUpdate) ci.cancel();
    }
    
    @Inject(method = "needsFood", at = @At("HEAD"), cancellable = true)
    private void noNeedForFood(CallbackInfoReturnable<Boolean> cir) {
        if(thermorigins$preventFoodUpdate) cir.setReturnValue(false);
    }
    
    @Inject(method = "addExhaustion", at = @At("HEAD"), cancellable = true)
    private void preventAddExhaustion(float value, CallbackInfo ci) {
        if(thermorigins$preventFoodUpdate) {
            //Builtin mechanics for Steam Power
            if(Services.PLATFORM.hasPowerType(thermorigins$player, ThermoPowers.STEAM_POWER_RESOURCE)) {
                thermorigins$steamPowerConsumption += value * 100;
                int change = Mth.floor(thermorigins$steamPowerConsumption);
                if(change > 0) {
                    Services.PLATFORM.changeResource(thermorigins$player, ThermoPowers.STEAM_POWER_RESOURCE, -change);
                    thermorigins$steamPowerConsumption -= change;
                }
            }
            ci.cancel();
        }
    }
    
    @Inject(method = "setFoodLevel", at = @At("HEAD"), cancellable = true)
    private void preventSetFoodLevel(int value, CallbackInfo ci) {
        if(thermorigins$preventFoodUpdate) ci.cancel();
    }
    
    @Inject(method = "setSaturation", at = @At("HEAD"), cancellable = true)
    private void preventSetSaturation(float value, CallbackInfo ci) {
        if(thermorigins$preventFoodUpdate) ci.cancel();
    }
    
    @Inject(method = "setExhaustion", at = @At("HEAD"), cancellable = true)
    private void preventSetExhaustion(float value, CallbackInfo ci) {
        if(thermorigins$preventFoodUpdate) ci.cancel();
    }
    
}