package me.matthewe.atherial.narcos.narcosdrugs.gson;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import me.matthewe.atherial.narcos.narcosdrugs.NarcosDrugs;
import me.matthewe.atherial.narcos.narcosdrugs.drugpacks.DrugPackHandler;
import me.matthewe.atherial.narcos.narcosdrugs.drugpacks.player.DrugPackStorage;
import me.matthewe.atherial.narcos.narcosdrugs.drugpacks.player.PlayerDrugPack;
import net.atherial.api.plugin.serializers.AtherialJsonDeserializer;
import net.atherial.api.plugin.utilities.JsonUtils;

import java.lang.reflect.Type;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by Matthew E on 6/22/2019 at 6:55 PM for the project NarcosDrugs
 */
public class DrugPackStorageDeserializer extends AtherialJsonDeserializer<DrugPackStorage> {

    public DrugPackStorageDeserializer() {
        super(DrugPackStorage.class);
    }


    @Override
    public DrugPackStorage deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {

        System.out.println(jsonElement.toString());
        DrugPackStorage drugPackStorage = new DrugPackStorage(NarcosDrugs.getInstance(), DrugPackHandler.get());
        JsonObject jsonObject = (JsonObject) jsonElement;
        drugPackStorage.drugPacks = new ConcurrentHashMap<>();
        if (jsonObject.has("drugPacks")) {
            for (JsonElement drugPacks : jsonObject.getAsJsonArray("drugPacks")) {
                PlayerDrugPack playerDrugPack = JsonUtils.GSON_BUILDER.create().fromJson(drugPacks, PlayerDrugPack.class);
                drugPackStorage.drugPacks.put(playerDrugPack.getId(), playerDrugPack);
            }
        }
        return drugPackStorage;
    }
}
