package me.matthewe.atherial.narcos.narcosdrugs.drug;

import me.matthewe.atherial.narcos.narcosdrugs.NarcosDrugs;
import me.matthewe.atherial.narcos.narcosdrugs.drug.crop.CropBlock;
import me.matthewe.atherial.narcos.narcosdrugs.drug.crop.CropDrop;
import net.atherial.api.plugin.utilities.logger.Logger;
import net.atherial.api.storage.YamlDataManager;
import org.bukkit.CropState;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static net.atherial.api.plugin.utilities.MessageUtils.formatEnum;

public class DrugConfig extends YamlDataManager<NarcosDrugs, String, Drug> {


    private Logger logger;

    public DrugConfig(NarcosDrugs plugin) {
        super(plugin, "drugs.yml");
        logger = new net.atherial.api.plugin.utilities.logger.AtherialLogger(this.getClass(), NarcosDrugs.getInstance());
    }

    @Override
    public void onDataCreate(Drug drug) {
        logger.info("Created drug %s", drug.getName());

    }

    @Override
    public void onDataSave(Drug drug) {
        logger.info("Saved drug %s", drug.getName());
    }

    @Override
    public void onDataLoad(Drug drug) {
        logger.info("Loaded drug %s", drug.getName());
    }

    @Override
    public String getKeyFromString(String s) {
        return new String(s);
    }

    @Override
    public String getStringFromKey(String s) {
        return new String(s);
    }

    public Drug getDrug(String name) {
        return map.get(name.toLowerCase());
    }

    public boolean isDrug(String name) {
        return map.containsKey(name.toLowerCase());
    }


    @Override
    public String getRootPath() {
        return "drugs";
    }

    @Override
    public Drug getTemplateData(String name) {
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
        return drug1;
    }

    @Override
    public ConfigurationSection saveToFile(Drug drug, String s, ConfigurationSection section) {
        section.set("name", drug.getName());

        drug.getGrowthStages().forEach((stage, growthStage) -> {
            section.set("stages." + stage + ".drugName", drug.getName());
            section.set("stages." + stage + ".stage", stage);
            section.set("stages." + stage + ".harvestable", growthStage.isHarvestable());
            section.set("stages." + stage + ".breakablePoints", growthStage.getBreakablePoints());
            section.set("stages." + stage + ".growTime", growthStage.getGrowTime());


            section.set("stages." + stage + ".soil.type", growthStage.getSoil().getType().toString());
            section.set("stages." + stage + ".soil.data", growthStage.getSoil().getData());

            List<CropDrop> drops = growthStage.getHarvest().getDrops();
            for (int i = 0; i < drops.size(); i++) {
                CropDrop cropDrop = drops.get(i);
                section.set("stages." + stage + ".harvest.drops." + i + ".type", cropDrop.getType().toString());
                section.set("stages." + stage + ".harvest.drops." + i + ".data", cropDrop.getData());
                section.set("stages." + stage + ".harvest.drops." + i + ".displayName", cropDrop.getDisplayName());
                section.set("stages." + stage + ".harvest.drops." + i + ".loreStringList", cropDrop.getLoreStringList());
                section.set("stages." + stage + ".harvest.drops." + i + ".minAmount", cropDrop.getMinAmount());
                section.set("stages." + stage + ".harvest.drops." + i + ".maxAmount", cropDrop.getMaxAmount());
                section.set("stages." + stage + ".harvest.drops." + i + ".harvestType", cropDrop.getHarvestType().toString());
            }

            growthStage.getBlocks().forEach((integer, cropBlock) -> {
                section.set("stages." + stage + ".blocks." + integer + ".type", cropBlock.getType().toString());
                section.set("stages." + stage + ".blocks." + integer + ".data", cropBlock.getData());
                section.set("stages." + stage + ".blocks." + integer + ".offset.up", cropBlock.getOffset().getUp());
                section.set("stages." + stage + ".blocks." + integer + ".offset.down", cropBlock.getOffset().getDown());

            });
        });
        section.set("maxStage", drug.getMaxStage());
        section.set("displayType", drug.getDisplayType().toString());
        section.set("requiredSoil.type", drug.getRequiredSoil().getType().toString());
        section.set("requiredSoil.data", drug.getRequiredSoil().getData());

        List<DrugEffect> effects = drug.getEffects();

        for (int i = 0; i < effects.size(); i++) {
            DrugEffect drugEffect = effects.get(i);
            section.set("effects." + i + ".name", drugEffect.getName());
            section.set("effects." + i + ".duration", drugEffect.getDuration());
            section.set("effects." + i + ".level", drugEffect.getLevel());

        }

        CropDrop seed = drug.getSeed();
        section.set("seed.type", seed.getType().toString());
        section.set("seed.data", seed.getData());
        section.set("seed.displayName", seed.getDisplayName());
        List<String> lore = seed.getLoreStringList()==null||seed.getLoreStringList().isEmpty()?new ArrayList<>():seed.getLoreStringList();
        section.set("seed.loreStringList",lore);
        section.set("seed.minAmount", seed.getMinAmount());
        section.set("seed.maxAmount", seed.getMaxAmount());
        section.set("seed.harvestType", seed.getHarvestType().toString());
        return section;
    }

    @Override
    public Drug loadFromFile(String s, String s2, ConfigurationSection section) {
        String name = section.getString("name");
        List<DrugEffect> drugEffects = new ArrayList<>();

        for (String effects : section.getConfigurationSection("effects").getKeys(false)) {
            ConfigurationSection effectSection = section.getConfigurationSection("effects." + effects);
            drugEffects.add(new DrugEffect(effectSection.getString("name"), effectSection.getInt("duration"), effectSection.getInt("level")));
        }

        CropDrop seed = getCropDrop("seed", section);

        Material displayType = Material.SUGAR;
        try {
            displayType = Material.getMaterial(section.getString("displayType"));
        } catch (Exception ignored) {

        }
        Map<Integer, GrowthStage> growthStageMap = new ConcurrentHashMap<>();


        CropBlock requiredSoil = new CropBlock(new CropBlock.Offset(0, 0), Material.valueOf(section.getString("requiredSoil.type")), (byte) section.getInt("requiredSoil.data"));


        for (String stageString : section.getConfigurationSection("stages").getKeys(false)) {
            ConfigurationSection stageSection = section.getConfigurationSection("stages." + stageString);

            int stage = stageSection.getInt("stage");
            boolean harvestable = stageSection.getBoolean("harvestable");
            String drugName = stageSection.getString("drugName");
            List<Integer> breakablePoints = stageSection.getIntegerList("breakablePoints");
            int growTime = stageSection.getInt("breakablePoints");

            CropBlock soil = new CropBlock(new CropBlock.Offset(0, 0), Material.valueOf(stageSection.getString("soil.type")), (byte) stageSection.getInt("soil.data"));

            List<CropDrop> harvest = new ArrayList<>();
            for (String key : stageSection.getConfigurationSection("harvest.drops").getKeys(false)) {
                CropDrop cropDrop = getCropDrop("harvest.drops." + key, stageSection);
                harvest.add(cropDrop);

            }

            Map<Integer, CropBlock> blockMap = new ConcurrentHashMap<>();

            for (String blocks : stageSection.getConfigurationSection("blocks").getKeys(false)) {
                int index = Integer.parseInt(blocks.trim());
                ConfigurationSection blocksSection = stageSection.getConfigurationSection("blocks." + blocks);
                CropBlock cropBlock = new CropBlock(new CropBlock.Offset(blocksSection.getInt("offset.up"), blocksSection.getInt("offset.down")), Material.valueOf(blocksSection.getString("type")), (byte) blocksSection.getInt("data"));
                blockMap.put(index, cropBlock);

            }
            growthStageMap.put(stage, new GrowthStage(drugName, stage, blockMap, harvestable, breakablePoints, new GrowthStage.Harvest(harvest), growTime, soil));
        }
        return new Drug(name, growthStageMap, seed, requiredSoil, drugEffects, displayType);
    }

    /*
     section.set("stages." + stage + ".drugName", drug.getName());
            section.set("stages." + stage + ".stage", stage);
            section.set("stages." + stage + ".harvestable", growthStage.isHarvestable());
            section.set("stages." + stage + ".breakablePoints", growthStage.getBreakablePoints());
            section.set("stages." + stage + ".growTime", growthStage.getGrowTime());


            section.set("stages." + stage + ".soil.type", growthStage.getSoil().getType().toString());
            section.set("stages." + stage + ".soil.data", growthStage.getSoil().getData());

            List<CropDrop> drops = growthStage.getHarvest().getDrops();
            for (int i = 0; i < drops.size(); i++) {
                CropDrop cropDrop = drops.get(i);
                section.set("stages." + stage + ".harvest.drops." + i + ".type", cropDrop.getType().toString());
                section.set("stages." + stage + ".harvest.drops." + i + ".data", cropDrop.getData());
                section.set("stages." + stage + ".harvest.drops." + i + ".displayName", cropDrop.getDisplayName());
                section.set("stages." + stage + ".harvest.drops." + i + ".loreStringList", cropDrop.getLoreStringList());
                section.set("stages." + stage + ".harvest.drops." + i + ".minAmount", cropDrop.getMinAmount());
                section.set("stages." + stage + ".harvest.drops." + i + ".maxAmount", cropDrop.getMaxAmount());
                section.set("stages." + stage + ".harvest.drops." + i + ".harvestType", cropDrop.getHarvestType().toString());
            }

            growthStage.getBlocks().forEach((integer, cropBlock) -> {
                section.set("stages." + stage + ".blocks." + integer + ".type", cropBlock.getType().toString());
                section.set("stages." + stage + ".blocks." + integer + ".data", cropBlock.getData());
                section.set("stages." + stage + ".blocks." + integer + ".offset.up", cropBlock.getOffset().getUp());
                section.set("stages." + stage + ".blocks." + integer + ".offset.down", cropBlock.getOffset().getDown());

            });
     */
    private CropDrop getCropDrop(String key, ConfigurationSection section) {
        Material material = Material.valueOf(section.getString(key + ".type"));
        byte data = (byte) section.getInt(key + ".data");

        int maxAmount = section.getInt(key + ".maxAmount", 1);
        int minAmount = section.getInt(key + ".minAmount", 1);

        CropDrop.Type type = CropDrop.Type.NONE;
        if (section.isSet(key + ".harvestType")) {
            type = CropDrop.Type.valueOf(section.getString(key + ".harvestType"));
        }

        String displayName = section.isSet(key + ".displayName") ? section.getString(key + ".displayName") : "";
        List<String> loreStringList = section.isSet(key + ".loreStringList") ? section.getStringList(key + ".loreStringList") : new ArrayList<>();

        return new CropDrop(material, displayName, loreStringList, data, minAmount, maxAmount, type);
    }

    @Override
    public String getSectionPath(String s) {
        return "drugs." + s;
    }

    public Map<String, Drug> getDrugs() {
        return map;
    }

    public void loadDrugs() {
        loadAll();
    }

    public void saveDrugs() {
        saveAll();
    }

    public void addDrug(Drug drug) {
        if (!exists(drug.getKey())) {
            create(drug);
        }
    }

    public Drug createNarcosDrug(Drug drug) {
        return create(drug);
    }

    public Drug createDrug(String name) {
        return create(getTemplateData(name));
    }
}