package dev.limonblaze.thermorigins.access;

import net.minecraft.world.entity.player.Player;

import javax.annotation.Nullable;

public interface FoodDataPlayerAccess {
    
    @Nullable Player getPlayer();
    
}
