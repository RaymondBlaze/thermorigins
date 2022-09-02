package dev.limonblaze.thermorigins.platform;

import dev.limonblaze.thermorigins.data.MultiloaderDataTypes;
import dev.limonblaze.thermorigins.platform.services.IPlatformHelper;
import dev.limonblaze.thermorigins.power.data.IPowerData;
import dev.limonblaze.thermorigins.registry.PowerFactoriesForge;
import com.google.auto.service.AutoService;
import io.github.apace100.apoli.power.Power;
import io.github.apace100.apoli.power.PowerType;
import io.github.apace100.apoli.power.factory.PowerFactory;
import io.github.apace100.calio.data.SerializableDataType;
import io.github.edwinmindcraft.apoli.api.component.IPowerContainer;
import io.github.edwinmindcraft.apoli.fabric.FabricPowerConfiguration;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.loading.FMLLoader;

import java.util.List;

@AutoService(IPlatformHelper.class)
public class ForgePlatformHelper implements IPlatformHelper {

    @Override
    public String getPlatformName() {

        return "Forge";
    }

    @Override
    public boolean isModLoaded(String modId) {

        return ModList.get().isLoaded(modId);
    }

    @Override
    public boolean isDevelopmentEnvironment() {

        return !FMLLoader.isProduction();
    }

    @Override
    public <P extends Power> PowerFactory<P> registerPowerFactory(ResourceLocation id, IPowerData power) {
        PowerFactory<P> powerFactory = new PowerFactory<>(id, power.getSerializableData(), power.getPowerConstructorForge());
        PowerFactoriesForge.POWER_FACTORY_REGISTRY.register(id.getPath(), powerFactory::getWrapped);
        return powerFactory;
    }

    @Override
    public <P extends Power> List<P> getPowers(LivingEntity entity, Class<P> powerClass, PowerFactory<P> powerFactory) {
        return IPowerContainer.getPowers(entity, powerFactory.getWrapped()).stream().map(configuredPower -> ((FabricPowerConfiguration<P>)configuredPower.getConfiguration()).power().apply((PowerType<P>) configuredPower.getPowerType(), entity)).toList();
    }

    @Override
    public <P extends Power> boolean hasPower(LivingEntity living, Class<P> powerClass, PowerFactory<P> powerFactory) {
        return IPowerContainer.hasPower(living, powerFactory.getWrapped());
    }

    @Override
    public SerializableDataType<?> getBiEntityConditionDataType() {
        return MultiloaderDataTypes.BIENTITY_CONDITION.get();
    }

    @Override
    public SerializableDataType<?> getBiomeConditionDataType() {
        return MultiloaderDataTypes.BIOME_CONDITION.get();
    }

    @Override
    public SerializableDataType<?> getBlockConditionDataType() {
        return MultiloaderDataTypes.BLOCK_CONDITION.get();
    }

    @Override
    public SerializableDataType<?> getDamageConditionDataType() {
        return MultiloaderDataTypes.DAMAGE_CONDITION.get();
    }

    @Override
    public SerializableDataType<?> getEntityConditionDataType() {
        return MultiloaderDataTypes.ENTITY_CONDITION.get();
    }

    @Override
    public SerializableDataType<?> getFluidConditionDataType() {
        return MultiloaderDataTypes.FLUID_CONDITION.get();
    }

    @Override
    public SerializableDataType<?> getItemConditionDataType() {
        return MultiloaderDataTypes.ITEM_CONDITION.get();
    }

    @Override
    public SerializableDataType<?> getBiEntityActionDataType() {
        return MultiloaderDataTypes.BIENTITY_ACTION.get();
    }

    @Override
    public SerializableDataType<?> getBlockActionDataType() {
        return MultiloaderDataTypes.BLOCK_ACTION.get();
    }

    @Override
    public SerializableDataType<?> getEntityActionDataType() {
        return MultiloaderDataTypes.ENTITY_ACTION.get();
    }

    @Override
    public SerializableDataType<?> getItemActionDataType() {
        return MultiloaderDataTypes.ITEM_ACTION.get();
    }
}
