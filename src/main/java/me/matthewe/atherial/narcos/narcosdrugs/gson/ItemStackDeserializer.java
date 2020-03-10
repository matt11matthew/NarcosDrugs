package me.matthewe.atherial.narcos.narcosdrugs.gson;

import com.google.gson.*;
import net.atherial.api.plugin.item.AtherialDisplayItem;
import net.atherial.api.plugin.serializers.AtherialJsonDeserializer;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static net.atherial.api.plugin.utilities.MessageUtils.colorize;

/**
 * Created by Matthew E on 6/22/2019 at 6:55 PM for the project NarcosDrugs
 */
public class ItemStackDeserializer extends AtherialJsonDeserializer<ItemStack> {

    public ItemStackDeserializer() {
        super(ItemStack.class);
    }

    @Override
    public ItemStack deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        JsonObject jsonObject = (JsonObject) jsonElement;
        System.out.println(jsonElement.toString());

        if (jsonObject.has("material") && jsonObject.get("material").getAsString().equalsIgnoreCase("AIR")) {
            return new ItemStack(Material.AIR);
        }
        Material material = Material.valueOf(jsonObject.get("material").getAsString());
        short data = jsonObject.get("data").getAsShort();
        boolean glowing = false;
        String[] lore = null;
        String displayName = null;
        int amount = 1;
        if (jsonObject.has("amount")) {
            amount = jsonObject.get("amount").getAsInt();
        }

        Map<Enchantment, Integer> enchantMap = null;
        if (jsonObject.has("glowing")) {
            glowing = true;
        }

        if (jsonObject.has("displayName")) {
            displayName = jsonObject.get("displayName").getAsString();
        }

        if (jsonObject.has("lore")) {
            List<String> loreStringList = new ArrayList();
            JsonArray loreJsonElements = jsonObject.getAsJsonArray("lore");
            loreJsonElements.forEach((jsonElement1) -> {
                loreStringList.add(jsonElement1.getAsString());
            });
            lore = (String[]) loreStringList.toArray(new String[0]);
        }

        if (jsonObject.has("enchants")) {
            enchantMap = new ConcurrentHashMap();
            JsonObject enchants = jsonObject.getAsJsonObject("enchants");
            Iterator var18 = enchants.entrySet().iterator();

            while (var18.hasNext()) {
                Map.Entry<String, JsonElement> stringJsonElementEntry = (Map.Entry) var18.next();
                Enchantment enchantment = Enchantment.getByName((String) stringJsonElementEntry.getKey());
                int level = ((JsonElement) stringJsonElementEntry.getValue()).getAsInt();
                enchantMap.put(enchantment, level);
            }
        }

        return AtherialDisplayItem.builder().type(material).data(data).displayName(displayName).lore(lore).amount(amount).enchants(enchantMap).glowing(glowing).build().getItemStack(amount, s -> colorize(s));
    }
}
