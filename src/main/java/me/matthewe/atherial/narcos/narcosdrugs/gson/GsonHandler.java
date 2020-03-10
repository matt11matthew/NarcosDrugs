package me.matthewe.atherial.narcos.narcosdrugs.gson;

import me.matthewe.atherial.narcos.narcosdrugs.NarcosDrugs;
import me.matthewe.atherial.narcos.narcosdrugs.NarcosHandler;
import me.matthewe.atherial.narcos.narcosdrugs.gson.npclocation.NPCLocationDeserializer;
import me.matthewe.atherial.narcos.narcosdrugs.gson.npclocation.NPCLocationSerializer;
import net.atherial.api.plugin.utilities.JsonUtils;

/**
 * Created by Matthew E on 6/10/2019 at 7:40 PM for the project NarcosDrugs
 */
public class GsonHandler extends NarcosHandler {
    public GsonHandler(NarcosDrugs narcosDrugs) {
        super(narcosDrugs);
    }

    @Override
    public void onEnable() {
        new NPCLocationSerializer().register(JsonUtils.GSON_BUILDER);
        new NPCLocationDeserializer().register(JsonUtils.GSON_BUILDER);
        new ItemStackSerializer().register(JsonUtils.GSON_BUILDER);
        new ItemStackDeserializer().register(JsonUtils.GSON_BUILDER);
//        new DrugPackStorageSerializer().register(JsonUtils.GSON_BUILDER);
//        new DrugPackStorageDeserializer().register(JsonUtils.GSON_BUILDER);
//        new PlayerDrugPackSerializer().register(JsonUtils.GSON_BUILDER);
//        new PlayerDrugPackDeserializer().register(JsonUtils.GSON_BUILDER);
    }

    @Override
    public void reloadHandler() {

    }

    @Override
    public void onDisable() {

    }
}
