package com.example.examplemod.mixin.multiloader;

import com.example.examplemod.access.ItemStackLevelAccess;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ItemStack.class)
public class ItemStackMixin implements ItemStackLevelAccess {
    public Level yourmodid$level;

    @Inject(method = "inventoryTick", at = @At("HEAD"))
    private void originsmultiloader$getLevelFrOmInventory(Level level, Entity entity, int slot, boolean selected, CallbackInfo ci) {
        yourmodid$level = level;
    }

    @Override
    public Level yourmodid$getLevel() {
        return yourmodid$level;
    }

    @Override
    public void yourmodid$setLevel(Level value) {
        yourmodid$level = value;
    }
}
