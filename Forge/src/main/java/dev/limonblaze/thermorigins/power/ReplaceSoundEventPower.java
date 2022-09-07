package dev.limonblaze.thermorigins.power;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.limonblaze.thermorigins.data.ConfiguredSoundEvent;
import dev.limonblaze.thermorigins.data.WeightedConfiguredSoundEvent;
import io.github.edwinmindcraft.apoli.api.IDynamicFeatureConfiguration;
import io.github.edwinmindcraft.apoli.api.configuration.ListConfiguration;
import io.github.edwinmindcraft.apoli.api.power.configuration.ConfiguredPower;
import io.github.edwinmindcraft.apoli.api.power.factory.PowerFactory;
import io.github.edwinmindcraft.calio.api.network.CalioCodecHelper;
import net.minecraft.world.entity.LivingEntity;

import java.util.List;
import java.util.Random;

public class ReplaceSoundEventPower extends PowerFactory<ReplaceSoundEventPower.Configuration> {
    
    public static final Codec<Configuration> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        ListConfiguration.mapCodec(WeightedConfiguredSoundEvent.CODEC, "sound", "sounds")
            .forGetter(Configuration::sounds),
        CalioCodecHelper.FLOAT.optionalFieldOf("volume", 1.0F)
            .forGetter(Configuration::volume),
        CalioCodecHelper.FLOAT.optionalFieldOf("pitch", 1.0F)
            .forGetter(Configuration::pitch),
        CalioCodecHelper.BOOL.optionalFieldOf("muted", false)
            .forGetter(Configuration::muted)
    ).apply(instance, Configuration::new));
    
    public ReplaceSoundEventPower() {
        super(CODEC);
    }
    
    public static void playSound(ConfiguredPower<Configuration, ReplaceSoundEventPower> power, LivingEntity entity, boolean randomPitch) {
        Configuration config = power.getConfiguration();
        List<WeightedConfiguredSoundEvent> sounds = config.sounds.entries();
        int size = sounds.size();
        
        if(size < 1) return;
        
        WeightedConfiguredSoundEvent chosenSound;
        Random random = entity.getRandom();
        
        if(size == 1) {
            chosenSound = sounds.get(0);
        } else {
            int totalWeight = 0, index = 0;
            for(var sound : sounds)
                totalWeight += sound.weight();
            for(double r = random.nextDouble() * totalWeight; index < size - 1; ++index) {
                r -= sounds.get(index).weight();
                if(r <= 0.0) break;
            }
            chosenSound = sounds.get(index);
        }
        
        for(var sound : chosenSound.sounds().entries()) {
            float v = sound.volume(), p = sound.pitch();
            v = Float.isNaN(v) ? config.volume : v;
            p = Float.isNaN(p) ? config.pitch : p;
            if(randomPitch)
                p += (random.nextFloat() - random.nextFloat()) * 0.2F;
            entity.level.playSound(null, entity.getX(), entity.getY(), entity.getZ(), sound.sound(), entity.getSoundSource(), v, p);
        }
    }
    
    public record Configuration(ListConfiguration<WeightedConfiguredSoundEvent> sounds,
                                float volume, float pitch,
                                boolean muted) implements IDynamicFeatureConfiguration {
    }
    
}
