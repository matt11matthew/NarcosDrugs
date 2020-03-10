package me.matthewe.atherial.narcos.narcosdrugs.drug.world;

import me.matthewe.atherial.narcos.narcosdrugs.NarcosDrugs;
import me.matthewe.atherial.narcos.narcosdrugs.drug.Drug;
import net.atherial.api.plugin.config.json.JsonConfig;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class WorldDrugConfig extends JsonConfig<NarcosDrugs, WorldDrugConfig> {
    public Map<String, WorldDrug> drugs = new ConcurrentHashMap<>();

    static {
        skipFields.add("drugPlanting");
    }

    public WorldDrugConfig(NarcosDrugs plugin) {
        super(plugin, "world_drugs.json");
    }

    public WorldDrug getDrug(Location location) {
        if (drugs.containsKey(new WorldDrugLocation(location.getWorld().getName(), location.getBlockX(), location.getBlockY(), location.getBlockZ()).toString())) {
            return drugs.get(new WorldDrugLocation(location.getWorld().getName(), location.getBlockX(), location.getBlockY(), location.getBlockZ()).toString());
        }
        for (int i = location.getBlockY() - 2; i < location.getBlockY() + 2; i++) {
            if (drugs.containsKey(new WorldDrugLocation(location.getWorld().getName(), location.getBlockX(), i, location.getBlockZ()).toString())) {
                return drugs.get(new WorldDrugLocation(location.getWorld().getName(), location.getBlockX(), i, location.getBlockZ()).toString());
            }
        }
        return null;
    }

    public boolean isDrug(Location location) {
        if (drugs.containsKey(new WorldDrugLocation(location.getWorld().getName(), location.getBlockX(), location.getBlockY(), location.getBlockZ()).toString())) {
            return true;
        }
        for (int i = location.getBlockY() - 2; i < location.getBlockY() + 2; i++) {
            if (drugs.containsKey(new WorldDrugLocation(location.getWorld().getName(), location.getBlockX(), i, location.getBlockZ()).toString())) {
                return true;
            }
        }
        return false;
    }


    public WorldDrug createDrug(Location location, Drug drug, Player planter) {
        if (!isDrug(location)) {

            WorldDrug worldDrug = new WorldDrug(WorldDrugLocation.fromLocation(location), UUID.randomUUID(), drug.getName(), planter.getUniqueId(), 0, drug.getStage(1).getGrowTime());

            drugs.put(worldDrug.getLocation().toString(), worldDrug);
            save();

            return drugs.get(worldDrug.getLocation().toString());
        }
        return null;
    }

    @Override
    public Class<WorldDrugConfig> getConfigClass() {
        return WorldDrugConfig.class;
    }

    public void removeDrug(WorldDrug worldDrug) {

        if (drugs.containsKey(worldDrug.getLocation().toString())) {
            drugs.remove(worldDrug.getLocation().toString());
            save();
        }
    }
}