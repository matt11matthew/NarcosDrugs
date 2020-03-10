package me.matthewe.atherial.narcos.narcosdrugs.drug;

import me.matthewe.atherial.narcos.narcosdrugs.NarcosDrugs;
import me.matthewe.atherial.narcos.narcosdrugs.drug.crop.CropBlock;
import me.matthewe.atherial.narcos.narcosdrugs.drug.crop.CropDrop;
import me.matthewe.atherial.narcos.narcosdrugs.drug.events.DrugCreationEvent;
import net.atherial.api.plugin.config.json.JsonConfig;
import org.bukkit.Bukkit;
import org.bukkit.CropState;
import org.bukkit.Material;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static net.atherial.api.plugin.utilities.MessageUtils.formatEnum;

public class DrugConfigOld extends JsonConfig<NarcosDrugs, DrugConfigOld> {
    public Map<String, Drug> drugs = new ConcurrentHashMap<>();

    static {
        skipFields.add("worldDrugHarvested");
        skipFields.add("key");
    }

    public DrugConfigOld(NarcosDrugs plugin) {
        super(plugin, "drugs.json");
    }

    public Drug getDrug(String name) {
        return drugs.get(name.toLowerCase());
    }

    public boolean isDrug(String name) {
        return drugs.containsKey(name.toLowerCase());
    }

    public Drug createDrug(String name) {
        if (!isDrug(name)) {

            Map<Integer, GrowthStage> defaultStages = new ConcurrentHashMap<>();
            Map<Integer, CropBlock> defaultBlocks0 = new ConcurrentHashMap<>();
            Map<Integer, CropBlock> defaultBlocks1 = new ConcurrentHashMap<>();
            Map<Integer, CropBlock> defaultBlocks2 = new ConcurrentHashMap<>();
            defaultBlocks0.put(1, new CropBlock(new CropBlock.Offset(0, 0), Material.CROPS, CropState.SEEDED.getData()));
            defaultBlocks1.put(1, new CropBlock(new CropBlock.Offset(0, 0), Material.CROPS, CropState.MEDIUM.getData()));

            defaultBlocks2.put(1, new CropBlock(new CropBlock.Offset(0, 0), Material.STONE, (byte) 0));
            defaultBlocks2.put(2, new CropBlock(new CropBlock.Offset(1, 0), Material.YELLOW_FLOWER, (byte) 0));

            CropDrop defaultSeed = new CropDrop(Material.SEEDS, "&a" + formatEnum(name) + " Seeds", Arrays.asList("&7Seeds"), 0, 1, CropDrop.Type.SEEDS);
            GrowthStage.Harvest defaultHarvest0 = new GrowthStage.Harvest(Arrays.asList(defaultSeed));
            GrowthStage.Harvest defaultHarvest1 = new GrowthStage.Harvest(Arrays.asList(defaultSeed, defaultSeed));
            GrowthStage.Harvest defaultHarvest2 = new GrowthStage.Harvest(Arrays.asList(defaultSeed, new CropDrop(Material.DOUBLE_PLANT, "&b" + name, new ArrayList<>(), 1, 2, CropDrop.Type.DRUG)));

            defaultStages.put(0, new GrowthStage(name.toLowerCase(), 0, defaultBlocks0, true, Arrays.asList(1), defaultHarvest0, 10, new CropBlock(null, Material.SOIL, (byte) 1)));
            defaultStages.put(1, new GrowthStage(name.toLowerCase(), 1, defaultBlocks1, true, Arrays.asList(1), defaultHarvest1, 10, new CropBlock(null, Material.SOIL, (byte) 1)));
            defaultStages.put(2, new GrowthStage(name.toLowerCase(), 2, defaultBlocks2, true, Arrays.asList(2), defaultHarvest2, 10, new CropBlock(null, Material.SPONGE, (byte) 0)));

            Map<Integer, Double> defaultPrices = new ConcurrentHashMap<>();

            for (int i = 1; i <= 10; i++) {
                defaultPrices.put(i, (double) (100 * i));
            }
            List<DrugEffect> effects = new ArrayList<>();
            effects.add(new DrugEffect("SPEED", 200, 1));
            Drug drug1 = new Drug(name.toLowerCase(), defaultStages, defaultSeed, new CropBlock(null, Material.DIAMOND_BLOCK, (byte) 0), effects, Material.DOUBLE_PLANT);
            this.drugs.put(name.toLowerCase(), drug1);
            Drug drug = this.drugs.get(name.toLowerCase());
            DrugCreationEvent drugCreationEvent = new DrugCreationEvent(drug);
            Bukkit.getPluginManager().callEvent(drugCreationEvent);
            save();

            return drug;
        }
        return null;
    }

    @Override
    public Class<DrugConfigOld> getConfigClass() {
        return DrugConfigOld.class;
    }


}