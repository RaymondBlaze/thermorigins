package dev.limonblaze.thermorigins.mixin.multiloader;

import dev.limonblaze.thermorigins.access.ItemStackLevelAccess;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ItemStack.class)
public class ItemStackMixin implements ItemStackLevelAccess {
    private Level thermorigins$level;

    @Inject(method = "inventoryTick", at = @At("HEAD"))
    private void thermorigins$getLevelFromInventory(Level level, Entity entity, int slot, boolean selected, CallbackInfo ci) {
        thermorigins$level = level;
    }

    @Override
    public Level thermorigins$getLevel() {
        return thermorigins$level;
    }

    @Override
    public void thermorigins$setLevel(Level value) {
        thermorigins$level = value;
    }
}
