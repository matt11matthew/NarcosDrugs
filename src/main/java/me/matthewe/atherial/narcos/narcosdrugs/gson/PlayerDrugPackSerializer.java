package me.matthewe.atherial.narcos.narcosdrugs.gson;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import me.matthewe.atherial.narcos.narcosdrugs.drugpacks.player.PlayerDrugPack;
import net.atherial.api.plugin.serializers.AtherialJsonSerializer;
import net.atherial.api.plugin.utilities.JsonUtils;

import java.lang.reflect.Type;

/**
 * Created by Matthew E on 6/22/2019 at 6:55 PM for the project NarcosDrugs
 */
public class PlayerDrugPackSerializer extends AtherialJsonSerializer<PlayerDrugPack> {

    public PlayerDrugPackSerializer() {
        super(PlayerDrugPack.class);
    }

    @Override
    public JsonElement serialize(PlayerDrugPack playerDrugPack, Type type, JsonSerializationContext jsonSerializationContext) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("tier", playerDrugPack.getTier());
        jsonObject.addProperty("id", playerDrugPack.getId());

        JsonArray items = new JsonArray();

        playerDrugPack.getItems().forEach((slot, itemStack) -> {
            String json = JsonUtils.GSON_BUILDER.create().toJson(itemStack);
            JsonObject itemJsonObject = JsonUtils.GSON_BUILDER.create().fromJson(json, JsonObject.class);
            itemJsonObject.addProperty("slot",slot);
            items.add(itemJsonObject);
        });

        jsonObject.add("items", items);
        return jsonObject;
    }
}
