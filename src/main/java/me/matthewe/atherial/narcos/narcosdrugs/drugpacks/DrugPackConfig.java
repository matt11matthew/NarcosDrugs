package me.matthewe.atherial.narcos.narcosdrugs.drugpacks;

import me.matthewe.atherial.narcos.narcosdrugs.NarcosDrugs;
import net.atherial.api.plugin.config.json.JsonConfig;
import net.atherial.api.plugin.item.AtherialDisplayItem;
import org.bukkit.Material;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


public class DrugPackConfig extends JsonConfig<NarcosDrugs, DrugPackConfig> {
    public Map<Integer, DrugPack> drugPacks = getDefaultDrugPacks();

    private Map<Integer, DrugPack> getDefaultDrugPacks() {
        Map<Integer, DrugPack> defaultDrugPacks = new ConcurrentHashMap<>();
        for (int i = 1; i <= 6; i++) {
            defaultDrugPacks.put(i, new DrugPack(i, i, AtherialDisplayItem.builder()
                    .amount(1)
                    .data((short) 0)
                    .type(Material.CHEST)
                    .displayName("&9Drug Pack &7(Right-Click)")
                    .lore(new String[]{"&7Tier &9\u00bb &3%tier%", "&7Space &9\u00bb &3%slots%"})
                    .build()));
        }
        return defaultDrugPacks;
    }

    public DrugPackConfig(NarcosDrugs plugin) {
        super(plugin, "drug_packs.json");
    }

    @Override
    public Class<DrugPackConfig> getConfigClass() {
        return DrugPackConfig.class;
    }

    public DrugPack getDrugPack(int tier) {
        return drugPacks.get(tier);
    }

    public boolean isDrugPack(int tier) {
        return drugPacks.containsKey(tier);
    }

}
