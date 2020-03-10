package me.matthewe.atherial.narcos.narcosdrugs.gson;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import me.matthewe.atherial.narcos.narcosdrugs.drugpacks.player.PlayerDrugPack;
import net.atherial.api.plugin.serializers.AtherialJsonDeserializer;
import net.atherial.api.plugin.utilities.JsonUtils;
import org.bukkit.inventory.ItemStack;

import java.lang.reflect.Type;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by Matthew E on 6/22/2019 at 6:55 PM for the project NarcosDrugs
 */
public class PlayerDrugPackDeserializer extends AtherialJsonDeserializer<PlayerDrugPack> {

    public PlayerDrugPackDeserializer() {
        super(PlayerDrugPack.class);
    }

    @Override
    public PlayerDrugPack deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        JsonObject jsonObject = (JsonObject) jsonElement;

        Map<Integer, ItemStack> items = new ConcurrentHashMap<>();

        if (jsonObject.has("items")) {
            for (JsonElement element : jsonObject.getAsJsonArray("items")) {
                int slot = element.getAsJsonObject().get("slot").getAsInt();
                ItemStack itemStack = JsonUtils.GSON_BUILDER.create().fromJson(element, ItemStack.class);
                items.put(slot, itemStack);
            }
        }
        return new PlayerDrugPack(jsonObject.get("id").getAsString(), items, jsonObject.get("tier").getAsInt());

    }
}
