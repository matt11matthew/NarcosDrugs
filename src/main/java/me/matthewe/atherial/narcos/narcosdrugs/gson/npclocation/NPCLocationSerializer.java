package me.matthewe.atherial.narcos.narcosdrugs.gson.npclocation;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import me.matthewe.atherial.narcos.narcosdrugs.npc.NPCLocation;
import net.atherial.api.plugin.serializers.AtherialJsonSerializer;

import java.lang.reflect.Type;

/**
 * Created by Matthew E on 6/19/2019 at 4:30 PM for the project NarcosDrugs
 */
public class NPCLocationSerializer extends AtherialJsonSerializer<NPCLocation> {
    public NPCLocationSerializer() {
        super(NPCLocation.class);
    }

    @Override
    public JsonElement serialize(NPCLocation npcLocation, Type type, JsonSerializationContext jsonSerializationContext) {
        return new JsonPrimitive(npcLocation.toString());
    }
}
