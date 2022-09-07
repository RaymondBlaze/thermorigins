package dev.limonblaze.thermorigins.power;

import dev.limonblaze.thermorigins.Thermorigins;
import dev.limonblaze.thermorigins.mixin.ServerLevelMixin;
import dev.limonblaze.thermorigins.platform.Services;
import dev.limonblaze.thermorigins.registry.ThermoPowers;
import io.github.apace100.apoli.power.Power;
import io.github.apace100.apoli.power.PowerType;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.entity.LivingEntity;

import javax.annotation.Nullable;
import java.lang.ref.WeakReference;
import java.util.*;

/**
 * Power that makes <b>player</b> attract {@link LightningBolt}s within certain range.
 * @see ServerLevelMixin
 */
public class RedirectLightningPower extends Power {
    /**
     * Shouldn't directly hold LivingEntity here, using map for general identity
     */
    private static final Map<UUID, WeakReference<LivingEntity>> POWER_HOLDERS = new HashMap<>();
    private final boolean ignoreLightningRod;
    private final double range;
    private final double chance;
    
    public RedirectLightningPower(PowerType<?> type, LivingEntity entity, boolean ignoreLightningRod, double range, double chance) {
        super(type, entity);
        this.ignoreLightningRod = ignoreLightningRod;
        this.range = range;
        this.chance = chance;
    }
    
    @Override
    public void onAdded() {
        if(!entity.level.isClientSide) {
            POWER_HOLDERS.put(entity.getUUID(), new WeakReference<>(entity));
        }
    }
    
    @Override
    public void onRemoved() {
        if(!entity.level.isClientSide) {
            POWER_HOLDERS.remove(entity.getUUID());
        }
    }
    
    public static Optional<BlockPos> redirectLightningPos(ServerLevel level, BlockPos lightningPos, @Nullable BlockPos rodPos) {
        boolean hasRod = rodPos != null;
        var entries = new ArrayList<>(POWER_HOLDERS.entrySet());
        Thermorigins.LOGGER.error(entries.toString());
        Collections.shuffle(entries);
        for(var entry : entries) {
            LivingEntity entity = entry.getValue().get();
            if(entity == null) {
                POWER_HOLDERS.remove(entry.getKey());
                continue;
            }
            BlockPos entityPos = entity.blockPosition();
            if(!(level.isPositionEntityTicking(entityPos) && level.isRainingAt(entityPos))) continue;
            float chance = 0F;
            for(var power : Services.PLATFORM.getPowers(entity, RedirectLightningPower.class, ThermoPowers.REDIRECT_LIGHTNING)) {
                if(hasRod && !power.ignoreLightningRod) continue;
                if(lightningPos.distSqr(entityPos) < power.range * power.range) {
                    chance += power.chance;
                }
            }
            if(entity.getRandom().nextDouble() < chance) {
                return Optional.of(entityPos);
            }
        }
        return Optional.ofNullable(rodPos);
    }
    
}
