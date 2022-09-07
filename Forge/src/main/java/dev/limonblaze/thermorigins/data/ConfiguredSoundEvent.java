package dev.limonblaze.thermorigins.data;

import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.apace100.calio.data.SerializableDataType;
import io.github.apace100.calio.data.SerializableDataTypes;
import io.github.edwinmindcraft.calio.api.network.CalioCodecHelper;
import net.minecraft.sounds.SoundEvent;

public record ConfiguredSoundEvent(SoundEvent sound, float pitch, float volume) {

    public static final Codec<ConfiguredSoundEvent> CODEC = Codec.either(
        SerializableDataType.wrap(
            ConfiguredSoundEvent.class,
            SerializableDataTypes.SOUND_EVENT,
            configured -> configured.sound(),
            soundEvent -> new ConfiguredSoundEvent(soundEvent, Float.NaN, Float.NaN)
        ),
        RecordCodecBuilder.<ConfiguredSoundEvent>create(instance -> instance.group(
            SerializableDataTypes.SOUND_EVENT.fieldOf("sound")
                .forGetter(ConfiguredSoundEvent::sound),
            CalioCodecHelper.FLOAT.optionalFieldOf("pitch", Float.NaN)
                .forGetter(ConfiguredSoundEvent::pitch),
            CalioCodecHelper.FLOAT.optionalFieldOf("volume", Float.NaN)
                .forGetter(ConfiguredSoundEvent::volume)
        ).apply(instance, ConfiguredSoundEvent::new))
    ).xmap(either -> either.map(x -> x, x -> x), instance -> Either.right(instance));

}