package me.matthewe.atherial.narcos.narcosdrugs.drug;

import me.matthewe.atherial.narcos.narcosdrugs.drug.crop.CropBlock;
import me.matthewe.atherial.narcos.narcosdrugs.drug.crop.CropDrop;
import me.matthewe.atherial.narcos.narcosdrugs.drug.world.WorldDrugLocation;
import me.matthewe.atherial.narcos.narcosdrugs.level.Level;
import org.bukkit.Location;
import org.bukkit.block.Block;

import java.util.List;
import java.util.Map;

/**
 * Created by Matthew E on 6/9/2019 at 1:35 PM for the project NarcosDrugs
 */
public class GrowthStage {
    private final String drugName;
    private final int stage;
    private Map<Integer, CropBlock> blocks;
    private boolean harvestable;
    private List<Integer> breakablePoints;
    private Harvest harvest;
    private int growTime;
    private CropBlock soil;

    public GrowthStage(String drugName, int stage, Map<Integer, CropBlock> blocks, boolean harvestable, List<Integer> breakablePoints, Harvest harvest, int growTime, CropBlock soil) {
        this.drugName = drugName;
        this.stage = stage;
        this.blocks = blocks;
        this.harvestable = harvestable;
        this.breakablePoints = breakablePoints;
        this.harvest = harvest;
        this.growTime = growTime;
        this.soil = soil;
    }

    public void setBlocks(Map<Integer, CropBlock> blocks) {
        this.blocks = blocks;
    }

    public void setHarvestable(boolean harvestable) {
        this.harvestable = harvestable;
    }

    public void setBreakablePoints(List<Integer> breakablePoints) {
        this.breakablePoints = breakablePoints;
    }

    public void setHarvest(Harvest harvest) {
        this.harvest = harvest;
    }

    public void setGrowTime(int growTime) {
        this.growTime = growTime;
    }

    public void setSoil(CropBlock soil) {
        this.soil = soil;
    }

    public CropBlock getSoil() {
        return soil;
    }

    public int getGrowTime() {
        return growTime;
    }

    public boolean isBreakable(int point) {
        return breakablePoints.contains(point);
    }

//    public boolean harvest(Player player, Location drugLocation, Block block) {
//
////        int breakablePoint = getBreakablePoint(drugLocation, block.getLocation());
////        if (isBreakable(breakablePoint) && harvestable && harvest != null) {
////            harvest.drop(block.getLocation());
////            blocks.values().forEach(value -> value.remove(drugLocation));
////            return true;
////        }
//        harvest.drop(block.getLocation());
//        blocks.values().forEach(value -> value.remove(drugLocation));
//        return false;
//    }

    public List<Integer> getBreakablePoints() {
        return breakablePoints;
    }

    public int getBreakablePoint(Location drugLocation, Location breakLocation) {
        double yDifference = breakLocation.getY() - drugLocation.getY();
        return (int) yDifference;
    }

    public void placeSoil(Location location) {
        WorldDrugLocation worldDrugLocation = WorldDrugLocation.fromLocation(location);
        worldDrugLocation.setY(worldDrugLocation.getY() - 1);
        Block block = worldDrugLocation.getLocation().getBlock();
        block.setType(soil.getType(), true);
        block.setData(soil.getData(), true);
    }

    public void place(Location location) {
        placeSoil(location);
        blocks.forEach((key, value) -> value.place(location));
    }


    public String getDrugName() {
        return drugName;
    }

    public int getStage() {
        return stage;
    }

    public Map<Integer, CropBlock> getBlocks() {
        return blocks;
    }

    public Harvest getHarvest() {
        return harvest;
    }

    public boolean isHarvestable() {
        return harvestable;
    }

    public static class Harvest {
        private List<CropDrop> drops;

        public List<CropDrop> getDrops() {
            return drops;
        }

        public Harvest(List<CropDrop> drops) {
            this.drops = drops;
        }

        public void drop(Location location, Drug drug, Level level) {
            for (CropDrop drop : drops) {
                drop.drop(location, drug,level);
            }
        }
    }
}
