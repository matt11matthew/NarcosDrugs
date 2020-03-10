package me.matthewe.atherial.narcos.narcosdrugs.gson;

import com.google.gson.*;
import net.atherial.api.plugin.serializers.AtherialJsonSerializer;
import org.bukkit.inventory.ItemStack;

import java.lang.reflect.Type;

/**
 * Created by Matthew E on 6/22/2019 at 6:55 PM for the project NarcosDrugs
 */
public class ItemStackSerializer extends AtherialJsonSerializer<ItemStack> {

    public ItemStackSerializer() {
        super(ItemStack.class);
    }

    @Override
    public JsonElement serialize(ItemStack crateDisplayItem, Type type, JsonSerializationContext jsonSerializationContext) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("material", crateDisplayItem.getType().toString());
        jsonObject.addProperty("data", crateDisplayItem.getData().getData());
        jsonObject.addProperty("amount", crateDisplayItem.getAmount());


        if (crateDisplayItem.hasItemMeta() && crateDisplayItem.getItemMeta().hasLore()) {
            JsonArray loreJsonElements = new JsonArray();

            for(String s:  crateDisplayItem.getItemMeta().getLore()) {
                loreJsonElements.add(new JsonPrimitive(s.replaceAll("§", "&")));
            }

            jsonObject.add("lore", loreJsonElements);
        }

        if (crateDisplayItem.hasItemMeta()&&crateDisplayItem.getItemMeta().hasDisplayName()) {
            jsonObject.addProperty("displayName", crateDisplayItem.getItemMeta().getDisplayName().replaceAll("§", "&"));
        }

        if (crateDisplayItem.hasItemMeta() &&!crateDisplayItem.getEnchantments().isEmpty()) {
            JsonObject enchants = new JsonObject();
            crateDisplayItem.getEnchantments().forEach((enchantment, level) -> {
                enchants.addProperty(enchantment.getName(), level);
            });
            jsonObject.add("enchants", enchants);
        }

        return jsonObject;
    }

}
