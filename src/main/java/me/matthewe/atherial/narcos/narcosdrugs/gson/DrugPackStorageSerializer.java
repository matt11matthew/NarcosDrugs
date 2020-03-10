package me.matthewe.atherial.narcos.narcosdrugs.gson;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import me.matthewe.atherial.narcos.narcosdrugs.drugpacks.player.DrugPackStorage;
import net.atherial.api.plugin.serializers.AtherialJsonSerializer;
import net.atherial.api.plugin.utilities.JsonUtils;

import java.lang.reflect.Type;

/**
 * Created by Matthew E on 6/22/2019 at 6:55 PM for the project NarcosDrugs
 */
public class DrugPackStorageSerializer extends AtherialJsonSerializer<DrugPackStorage> {

    public DrugPackStorageSerializer() {
        super(DrugPackStorage.class);
    }


    @Override
    public JsonElement serialize(DrugPackStorage drugPackStorage, Type type, JsonSerializationContext jsonSerializationContext) {
        JsonObject jsonObject = new JsonObject();
        JsonArray jsonElements = new JsonArray();

        drugPackStorage.drugPacks.forEach((s, playerDrugPack) -> {
            jsonElements.add( JsonUtils.GSON_BUILDER.create().toJsonTree(playerDrugPack));
        });
        jsonObject.add("drugPacks", jsonElements);
        return jsonObject;
    }
}
