package dev.limonblaze.thermorigins.registry;

import dev.limonblaze.thermorigins.Thermorigins;
import dev.limonblaze.thermorigins.loot.function.ReplaceItemLootFunction;
import net.minecraft.core.Registry;
import net.minecraft.world.level.storage.loot.functions.LootItemConditionalFunction;
import net.minecraft.world.level.storage.loot.functions.LootItemFunctionType;

public class ThermoLootFunctions {
    
    public static final LootItemFunctionType REPLACE_ITEM = register("replace_item", new ReplaceItemLootFunction.Serializer());
    
    private static LootItemFunctionType register(String name, LootItemConditionalFunction.Serializer<?> serializer) {
        return Registry.register(Registry.LOOT_FUNCTION_TYPE, Thermorigins.asResource(name), new LootItemFunctionType(serializer));
    }
    
    public static void registerAll() {}
    
}