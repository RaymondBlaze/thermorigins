package dev.limonblaze.thermorigins.loot.function;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import dev.limonblaze.thermorigins.registry.ThermoLootFunctions;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.Registry;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.ShapedRecipe;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.functions.LootItemConditionalFunction;
import net.minecraft.world.level.storage.loot.functions.LootItemFunctionType;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;

import javax.annotation.ParametersAreNonnullByDefault;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public class ReplaceItemLootFunction extends LootItemConditionalFunction {
    private final ItemStack item;
    private final boolean copyNbt;
    
    protected ReplaceItemLootFunction(ItemStack item, boolean copyNbt, LootItemCondition[] conditions) {
        super(conditions);
        this.item = item;
        this.copyNbt = copyNbt;
    }
    
    @Override
    protected ItemStack run(ItemStack stack, LootContext context) {
        ItemStack result = item.copy();
        if(copyNbt) result.setTag(stack.getTag());
        return result;
    }
    
    @Override
    public LootItemFunctionType getType() {
        return ThermoLootFunctions.REPLACE_ITEM;
    }
    
    public static class Serializer extends LootItemConditionalFunction.Serializer<ReplaceItemLootFunction> {
    
        @Override
        public void serialize(JsonObject json, ReplaceItemLootFunction function, JsonSerializationContext context) {
            super.serialize(json, function, context);
            JsonObject item = new JsonObject();
            item.addProperty("item", Registry.ITEM.getKey(function.item.getItem()).toString());
            int count = function.item.getCount();
            if(count > 1) item.addProperty("count", count);
            json.add("item", item);
            json.addProperty("copy_nbt", function.copyNbt);
        }
        
        @Override
        public ReplaceItemLootFunction deserialize(JsonObject json, JsonDeserializationContext context, LootItemCondition[] conditions) {
            ItemStack item = ShapedRecipe.itemStackFromJson(GsonHelper.getAsJsonObject(json, "item"));
            boolean copyNbt = GsonHelper.getAsBoolean(json, "copy_nbt", false);
            return new ReplaceItemLootFunction(item, copyNbt, conditions);
        }
        
    }
    
}
