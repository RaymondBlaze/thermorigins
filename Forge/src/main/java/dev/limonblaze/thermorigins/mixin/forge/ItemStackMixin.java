package dev.limonblaze.thermorigins.mixin.forge;

import dev.limonblaze.thermorigins.access.ItemStackLevelAccess;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Implements;
import org.spongepowered.asm.mixin.Interface;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ItemStack.class)
@Implements(value = @Interface(iface = ItemStackLevelAccess.class, prefix = "thermorigins$"))
public class ItemStackMixin {
    
    private Level thermorigins$level;
    
    public Level thermorigins$getLevel() {
        return thermorigins$level;
    }
    
    public void thermorigins$setLevel(Level value) {
        thermorigins$level = value;
    }
    
    @Inject(method = "inventoryTick", at = @At("HEAD"))
    private void getLevelFromInventory(Level level, Entity entity, int slot, boolean selected, CallbackInfo ci) {
        thermorigins$level = level;
    }
    
}
