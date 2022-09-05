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
import net.minecraft.util.Tuple;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.function.Consumer;
import java.util.function.Predicate;

/**
 * Power that grants player a workable furnace. <br>
 * {@link FurnacePower#furnace}: A furnace instance, can have type of furnace, blast furnace and smoker. <br>
 * {@link FurnacePower#shouldDropOnDeath}: If the contents in the furnace should drop on death. <br>
 * {@link FurnacePower#dropOnDeathFilter}: Item Condition to check if a certain stack should drop on death. <br>
 * {@link FurnacePower#key}: Key to open the furnace menu. <br>
 * {@link FurnacePower#openEntityCondition}: Entity Condition to check if the player can open the furnace menu. <br>
 * {@link FurnacePower#activeEntityAction}: Entity Action which performs every tick when the furnace is "running". <br>
 * {@link FurnacePower#idleEntityAction}: Entity Action which performs every tick when the furnace is not "running". <br>
 * {@link FurnacePower#smeltedEntityAction}: Entity Action which performs when the furnace smelted an item. <br>
 * {@link FurnacePower#smeltedInputCondition}: Item Condition to check on the input item if the furnace should perform actions on smelted. <br>
 * {@link FurnacePower#smeltedOutputCondition}: Item Condition to check on the output item if the furnace should perform actions on smelted. <br>
 * {@link FurnacePower#smeltedOutputAction}: Item Action which performs on the output item, mostly for modify usage. <b>Don't use random-based item action, this action will be called for simulation, and mismatched results may encounter unexpected behaviors.</b> <br>
 * @see FurnaceFactory
 */
@ParametersAreNonnullByDefault
public class FurnacePower extends Power implements Active {
    private final FurnaceFactory.Instance furnace;
    private final boolean shouldDropOnDeath;
    @Nullable private final Predicate<ItemStack> dropOnDeathFilter;
    @Nullable private final Predicate<Entity> openEntityCondition;
    @Nullable private final Consumer<Entity> activeEntityAction;
    @Nullable private final Consumer<Entity> idleEntityAction;
    @Nullable private final Consumer<Entity> smeltedEntityAction;
    @Nullable private final Predicate<ItemStack> smeltedInputCondition;
    @Nullable private final Predicate<ItemStack> smeltedOutputCondition;
    @Nullable private final Consumer<Tuple<Level, ItemStack>> smeltedOutputAction;
    private Key key;
    
    public FurnacePower(PowerType<?> type,
                        LivingEntity entity,
                        FurnaceFactory furnace,
                        @Nullable String containerName,
                        boolean shouldDropOnDeath,
                        @Nullable Predicate<ItemStack> dropOnDeathFilter,
                        @Nullable Predicate<Entity> openEntityCondition,
                        @Nullable Consumer<Entity> activeAction,
                        @Nullable Consumer<Entity> idleEntityAction,
                        @Nullable Consumer<Entity> smeltedEntityAction,
                        @Nullable Predicate<ItemStack> smeltedInputCondition,
                        @Nullable Predicate<ItemStack> smeltedOutputCondition,
                        @Nullable Consumer<Tuple<Level, ItemStack>> smeltedOutputAction
    ) {
        super(type, entity);
        this.shouldDropOnDeath = shouldDropOnDeath;
        this.dropOnDeathFilter = dropOnDeathFilter;
        this.openEntityCondition = openEntityCondition;
        this.activeEntityAction = activeAction;
        this.idleEntityAction = idleEntityAction;
        this.smeltedEntityAction = smeltedEntityAction;
        this.smeltedInputCondition = smeltedInputCondition;
        this.smeltedOutputCondition = smeltedOutputCondition;
        this.smeltedOutputAction = smeltedOutputAction;
        this.furnace = furnace.createInstance(containerName, this::onItemSmelted);
        setTicking();
    }
    
    public FurnaceFactory.Instance getFurnace() {
        return furnace;
    }
    
    public boolean shouldDropOnDeath() {
        return shouldDropOnDeath;
    }
    
    public boolean shouldDropOnDeath(ItemStack stack) {
        return shouldDropOnDeath && (dropOnDeathFilter == null || dropOnDeathFilter.test(stack));
    }
    
    public ItemStack onItemSmelted(ItemStack input, ItemStack result, LivingEntity entity, boolean simulate) {
        if((smeltedInputCondition == null || smeltedInputCondition.test(input)) &&
           (smeltedOutputCondition == null || smeltedOutputCondition.test(input))
        ) {
            if(!simulate && smeltedEntityAction != null) {
                smeltedEntityAction.accept(entity);
            }
            if(smeltedOutputAction != null) {
                ItemStack newResult = result.copy();
                smeltedOutputAction.accept(new Tuple<>(entity.level, newResult));
                return newResult;
            }
        }
        return result;
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
        if(isActive() && (openEntityCondition == null || openEntityCondition.test(entity))) {
            if(!entity.level.isClientSide && entity instanceof Player player) {
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
        if(!entity.level.isClientSide && isActive()) {
            if(furnace.tick(entity)) {
                if(activeEntityAction != null) activeEntityAction.accept(entity);
            } else {
                if(idleEntityAction != null) idleEntityAction.accept(entity);
            }
            if(furnace.getChanged()) PowerHolderComponent.syncPower(entity, type);
        }
    }
    
    public static PowerFactory<FurnacePower> createFactory(ResourceLocation id) {
        return new PowerFactory<FurnacePower>(id, new SerializableData()
            .add("furnace_type", ThermoDataTypes.FURNACE)
            .add("title", SerializableDataTypes.STRING, null)
            .add("drop_on_death", SerializableDataTypes.BOOLEAN, true)
            .add("drop_on_death_filter", ApoliDataTypes.ITEM_CONDITION, null)
            .add("open_entity_condition", ApoliDataTypes.ENTITY_CONDITION, null)
            .add("active_entity_action", ApoliDataTypes.ENTITY_ACTION, null)
            .add("idle_entity_action", ApoliDataTypes.ENTITY_ACTION, null)
            .add("smelted_entity_action", ApoliDataTypes.ENTITY_CONDITION, null)
            .add("smelted_input_condition", ApoliDataTypes.ITEM_CONDITION, null)
            .add("smelted_output_condition", ApoliDataTypes.ITEM_CONDITION, null)
            .add("smelted_output_action", ApoliDataTypes.ITEM_ACTION, null)
            .add("key", ApoliDataTypes.BACKWARDS_COMPATIBLE_KEY, new Key()),
            (data) -> (type, entity) -> {
                FurnacePower power = new FurnacePower(type, entity,
                    data.get("furnace_type"),
                    data.get("title"),
                    data.get("drop_on_death"),
                    data.get("drop_on_death_filter"),
                    data.get("open_entity_condition"),
                    data.get("active_entity_action"),
                    data.get("idle_entity_action"),
                    data.get("smelted_entity_action"),
                    data.get("smelted_input_condition"),
                    data.get("smelted_output_condition"),
                    data.get("smelted_output_action")
                );
                power.setKey(data.get("key"));
                return power;
            }
        ).allowCondition();
    }
    
}
