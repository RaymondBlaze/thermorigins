package dev.limonblaze.thermorigins.power;

import dev.limonblaze.thermorigins.data.FurnaceFactory;
import dev.limonblaze.thermorigins.data.ThermoDataTypes;
import io.github.apace100.apoli.component.PowerHolderComponent;
import io.github.apace100.apoli.data.ApoliDataTypes;
import io.github.apace100.apoli.power.Active;
import io.github.apace100.apoli.power.Power;
import io.github.apace100.apoli.power.PowerType;
import io.github.apace100.apoli.power.factory.PowerFactory;
import io.github.apace100.calio.data.SerializableData;
import io.github.apace100.calio.data.SerializableDataTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.function.Consumer;
import java.util.function.Predicate;

@ParametersAreNonnullByDefault
public class FurnacePower extends Power implements Active {
    private final FurnaceFactory.Instance furnace;
    private final boolean shouldDropOnDeath;
    private final Predicate<ItemStack> dropOnDeathFilter;
    private Key key;
    @Nullable
    private final Consumer<Entity> activeAction;
    @Nullable
    private final Consumer<Entity> idleAction;
    
    public FurnacePower(PowerType<?> type, LivingEntity entity,
                        FurnaceFactory furnace, @Nullable String containerName,
                        boolean shouldDropOnDeath, Predicate<ItemStack> dropOnDeathFilter,
                        @Nullable Consumer<Entity> activeAction, @Nullable Consumer<Entity> idleAction
    ) {
        super(type, entity);
        this.furnace = furnace.createInstance(containerName);
        this.shouldDropOnDeath = shouldDropOnDeath;
        this.dropOnDeathFilter = dropOnDeathFilter;
        this.activeAction = activeAction;
        this.idleAction = idleAction;
        this.setTicking();
    }
    
    public FurnaceFactory.Instance getFurnace() {
        return furnace;
    }
    
    public boolean shouldDropOnDeath() {
        return shouldDropOnDeath;
    }
    
    public boolean shouldDropOnDeath(ItemStack stack) {
        return shouldDropOnDeath && dropOnDeathFilter.test(stack);
    }
    
    @Override
    public Key getKey() {
        return key;
    }
    
    @Override
    public void setKey(Key key) {
        this.key = key;
    }
    
    @Override
    public void onUse() {
        if(this.isActive()) {
            if (!this.entity.level.isClientSide && this.entity instanceof Player player) {
                player.openMenu(furnace);
            }
        }
    }
    
    @Override
    public CompoundTag toTag() {
        CompoundTag tag = new CompoundTag();
        furnace.toTag(tag);
        return tag;
    }
    
    @Override
    public void fromTag(Tag tag) {
        super.fromTag(tag);
        furnace.fromTag((CompoundTag) tag);
    }
    
    @Override
    public void tick() {
        if(!entity.level.isClientSide && this.isActive()) {
            if(furnace.tick(entity)) {
                if(activeAction != null) activeAction.accept(entity);
            } else {
                if(idleAction != null) idleAction.accept(entity);
            }
            if(furnace.getChanged()) PowerHolderComponent.syncPower(entity, type);
        }
    }
    
    public static PowerFactory<FurnacePower> createFactory(ResourceLocation id) {
        return new PowerFactory<FurnacePower>(id, new SerializableData()
            .add("furnace_type", ThermoDataTypes.FURNACE)
            .add("title", SerializableDataTypes.STRING, null)
            .add("drop_on_death", SerializableDataTypes.BOOLEAN, false)
            .add("drop_on_death_filter", ApoliDataTypes.ITEM_CONDITION, null)
            .add("active_action", ApoliDataTypes.ENTITY_ACTION, null)
            .add("idle_action", ApoliDataTypes.ENTITY_ACTION, null)
            .add("key", ApoliDataTypes.BACKWARDS_COMPATIBLE_KEY, new Key()),
            (data) -> (type, entity) -> {
                FurnacePower power = new FurnacePower(type, entity,
                    data.get("furnace_type"),
                    data.get("title"),
                    data.get("drop_on_death"),
                    data.get("drop_on_death_filter"),
                    data.get("active_action"),
                    data.get("idle_action")
                );
                power.setKey(data.get("key"));
                return power;
            }
        ).allowCondition();
    }
    
}
