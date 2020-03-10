package me.matthewe.atherial.narcos.narcosdrugs.drug;

import me.matthewe.atherial.narcos.narcosdrugs.drug.crop.CropBlock;
import me.matthewe.atherial.narcos.narcosdrugs.drug.crop.CropDrop;
import net.atherial.api.storage.DataValue;
import org.bukkit.Material;

import java.util.List;
import java.util.Map;

/**
 * Created by Matthew E on 6/9/2019 at 1:33 PM for the project NarcosDrugs
 */
public class Drug implements DataValue<String, Drug> {
    private String name;
    private Map<Integer, GrowthStage> growthStages;
    private CropDrop seed;
    private int maxStage;
    private CropBlock requiredSoil;
    private List<DrugEffect> effects;

    private Material displayType = Material.SUGAR;


    public Material getDisplayType() {
        return displayType;
    }

    public Drug(String name, Map<Integer, GrowthStage> growthStages, CropDrop seed, CropBlock requiredSoil, List<DrugEffect> effects, Material displayType) {
        this.name = name;
        this.growthStages = growthStages;
        this.seed = seed;
        this.requiredSoil = requiredSoil;
        this.effects = effects;
        this.displayType = displayType;
        this.updateMaxStage();


    }

    public List<DrugEffect> getEffects() {
        return effects;
    }

    public CropBlock getRequiredSoil() {
        return requiredSoil;
    }

    public static Drug get(String drug) {
        return DrugHandler.get().getDrugConfig().getDrug(drug.toLowerCase());
    }


    public void updateMaxStage() {
        int max = 0;
        for (Integer integer : growthStages.keySet()) {
            if (integer > max) {
                max = integer;
            }
        }
        this.maxStage = max;
    }

    public int getMaxStage() {
        return maxStage;
    }

    public GrowthStage getStage(int stage) {
        return growthStages.get(stage);
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setGrowthStages(Map<Integer, GrowthStage> growthStages) {
        this.growthStages = growthStages;
    }

    public void setSeed(CropDrop seed) {
        this.seed = seed;
    }

    public void setMaxStage(int maxStage) {
        this.maxStage = maxStage;
    }

    public void setRequiredSoil(CropBlock requiredSoil) {
        this.requiredSoil = requiredSoil;
    }

    public void setEffects(List<DrugEffect> effects) {
        this.effects = effects;
    }

    public void setDisplayType(Material displayType) {
        this.displayType = displayType;
    }

    public Map<Integer, GrowthStage> getGrowthStages() {
        return growthStages;
    }

    public CropDrop getSeed() {
        return seed;
    }

    public String getName() {
        return name;
    }

    @Override
    public Class<Drug> getValueClass() {
        return Drug.class;
    }

    @Override
    public String getKey() {
        return getName() ;
    }
}
