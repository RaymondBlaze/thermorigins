package dev.limonblaze.thermorigins.power;

import dev.limonblaze.thermorigins.Thermorigins;
import dev.limonblaze.thermorigins.data.FurnaceFactory;
import dev.limonblaze.thermorigins.registry.ThermoPowersForge;
import io.github.edwinmindcraft.apoli.api.component.IPowerContainer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.GameRules;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = Thermorigins.ID)
public class PowerForgeEventHandler {
    
    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void onLivingDeath(LivingDeathEvent event) {
        if(event.getEntityLiving() instanceof Player player) {
            if(!player.level.getGameRules().getBoolean(GameRules.RULE_KEEPINVENTORY)) {
                IPowerContainer.getPowers(player, ThermoPowersForge.FURNACE.get()).forEach((inventory) -> {
                    if(inventory.getFactory().shouldDropOnDeath(inventory, player)) {
                        FurnaceFactory.Instance furnace = inventory.getFactory().getInventory(inventory, player);
                        furnace.clearData();
                        for(int i = 0; i < furnace.getContainerSize(); ++i) {
                            ItemStack itemStack = furnace.getItem(i);
                            if(inventory.getFactory().shouldDropOnDeath(inventory, player, itemStack)) {
                                if(!itemStack.isEmpty() && EnchantmentHelper.hasVanishingCurse(itemStack)) {
                                    furnace.removeItemNoUpdate(i);
                                } else {
                                    player.drop(itemStack, true, false);
                                    furnace.setItem(i, ItemStack.EMPTY);
                                }
                            }
                        }
                    }
                });
            }
        }
    }
    
}
