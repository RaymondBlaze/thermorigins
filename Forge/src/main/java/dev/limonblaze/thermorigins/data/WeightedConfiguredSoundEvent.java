package dev.limonblaze.thermorigins.data;

import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.apace100.calio.data.SerializableDataType;
import io.github.apace100.calio.data.SerializableDataTypes;
import io.github.edwinmindcraft.apoli.api.configuration.ListConfiguration;
import io.github.edwinmindcraft.calio.api.network.CalioCodecHelper;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.level.Level;

import java.util.List;
import java.util.Random;

public record WeightedConfiguredSoundEvent(ListConfiguration<ConfiguredSoundEvent> sounds, int weight) {
    
    public WeightedConfiguredSoundEvent(ConfiguredSoundEvent[] sounds, int weight) {
        this(ListConfiguration.of(sounds), weight);
    }
    
    public WeightedConfiguredSoundEvent(ConfiguredSoundEvent sound, int weight) {
        this(ListConfiguration.of(sound), weight);
    }
    
    public static final Codec<WeightedConfiguredSoundEvent> CODEC = Codec.either(
        SerializableDataType.wrap(
            WeightedConfiguredSoundEvent.class,
            SerializableDataTypes.SOUND_EVENT,
            wcSound -> wcSound.sounds.getContent().get(0).sound(),
            sound -> new WeightedConfiguredSoundEvent(new ConfiguredSoundEvent(sound, Float.NaN, Float.NaN), 1)
        ),
        RecordCodecBuilder.<WeightedConfiguredSoundEvent>create(instance -> instance.group(
            ListConfiguration.mapCodec(ConfiguredSoundEvent.CODEC, "sound", "sounds")
                .forGetter(WeightedConfiguredSoundEvent::sounds),
            CalioCodecHelper.INT.optionalFieldOf("weight", 1)
                .forGetter(WeightedConfiguredSoundEvent::weight)
        ).apply(instance, WeightedConfiguredSoundEvent::new))
    ).xmap(either -> either.map(x -> x, x -> x), instance -> Either.right(instance));
    
}
