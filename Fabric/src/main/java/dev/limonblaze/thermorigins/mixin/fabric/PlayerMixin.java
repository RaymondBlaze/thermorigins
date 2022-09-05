package dev.limonblaze.thermorigins.mixin.fabric;

import dev.limonblaze.thermorigins.data.FurnaceFactory;
import dev.limonblaze.thermorigins.power.FurnacePower;
import dev.limonblaze.thermorigins.power.ModifyToolPower;
import io.github.apace100.apoli.component.PowerHolderComponent;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import javax.annotation.Nullable;

@Mixin(Player.class)
public abstract class PlayerMixin extends LivingEntity {
    
    @Shadow @Nullable public abstract ItemEntity drop(ItemStack stack, boolean scatter, boolean setThrower);
    
    protected PlayerMixin(EntityType<? extends LivingEntity> entityType, Level level) {
        super(entityType, level);
    }
    
    @Inject(method = "dropEquipment", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/player/Inventory;dropAll()V"))
    private void dropAdditionalInventory(CallbackInfo ci) {
        for(FurnacePower power : PowerHolderComponent.getPowers(this, FurnacePower.class)) {
            if(power.shouldDropOnDeath()) {
                FurnaceFactory.Instance furnace = power.getFurnace();
                for(int i = 0; i < furnace.getContainerSize(); ++i) {
                    ItemStack itemStack = furnace.getItem(i);
                    if(power.shouldDropOnDeath(itemStack)) {
                        if(!itemStack.isEmpty() && EnchantmentHelper.hasVanishingCurse(itemStack)) {
                            furnace.removeItemNoUpdate(i);
                        } else {
                            this.drop(itemStack, true, false);
                            furnace.setItem(i, ItemStack.EMPTY);
                        }
                    }
                }
            }
        }
    }
    
}