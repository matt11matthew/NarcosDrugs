package me.matthewe.atherial.narcos.narcosdrugs.gson.npclocation;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import me.matthewe.atherial.narcos.narcosdrugs.npc.NPCLocation;
import net.atherial.api.plugin.serializers.AtherialJsonDeserializer;

import java.lang.reflect.Type;


/**
 * Created by Matthew E on 6/19/2019 at 4:30 PM for the project NarcosDrugs
 */
public class NPCLocationDeserializer extends AtherialJsonDeserializer<NPCLocation> {
    public NPCLocationDeserializer() {
        super(NPCLocation.class);
    }

    @Override
    public NPCLocation deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        return NPCLocation.fromString(jsonElement.getAsString());
    }
}
