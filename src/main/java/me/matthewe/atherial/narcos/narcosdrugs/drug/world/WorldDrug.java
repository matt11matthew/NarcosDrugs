package me.matthewe.atherial.narcos.narcosdrugs.drug.world;

import me.matthewe.atherial.narcos.narcosdrugs.drug.Drug;
import me.matthewe.atherial.narcos.narcosdrugs.drug.DrugHandler;
import me.matthewe.atherial.narcos.narcosdrugs.drug.GrowthStage;
import me.matthewe.atherial.narcos.narcosdrugs.drug.crop.CropBlock;
import me.matthewe.atherial.narcos.narcosdrugs.level.Level;
import me.matthewe.atherial.narcos.narcosdrugs.level.LevelHandler;
import me.matthewe.atherial.narcos.narcosdrugs.player.DrugPlayer;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by Matthew E on 6/10/2019 at 6:23 PM for the project NarcosDrugs
 */
public class WorldDrug {
    private WorldDrugLocation location;
    private UUID uuid;
    private String drug;
    private UUID planter;
    private int stage;

    private int growTime;
    private boolean drugPlanting = false;


    public WorldDrug(WorldDrugLocation location, UUID uuid, String drug, UUID planter, int stage, int growTime) {
        this.location = location;
        this.uuid = uuid;
        this.drug = drug;
        this.planter = planter;
        this.stage = stage;
        this.growTime = growTime;
    }

    public boolean isInBounds(Location location) {
        Drug drug = getDrugObject();
        for (GrowthStage stage : drug.getGrowthStages().values()) {
            List<Location> locations = new ArrayList<>();
            for (CropBlock value : stage.getBlocks().values()) {
                locations.add(value.getNewLocation(this.location.getLocation()));
            }
            if (locations.contains(location)) {
                return true;
            }
        }
        return false;
    }

    public int getGrowTime() {
        return growTime;
    }

    public Drug getDrugObject() {
        return Drug.get(drug);
    }

    public WorldDrugLocation getLocation() {
        return location;
    }

    public UUID getUuid() {
        return uuid;
    }

    public String getDrug() {
        return drug;
    }

    public UUID getPlanter() {
        return planter;
    }

    public int getMaxStage() {
        return getDrugObject().getMaxStage();
    }

    private boolean worldDrugHarvested = false;

    public void tick() {
        if (stage == getMaxStage()) {
            return;
        }
        if (getDrugObject() == null) {
            return;
        }
        if (growTime == -5) {
            return;
        }
        if (growTime > 0) {
            growTime--;
            GrowthStage stage = getDrugObject().getStage(this.stage);
            if (stage != null && !worldDrugHarvested) {
                stage.placeSoil(location.getLocation());
            }
        } else {
            Drug drugObject = getDrugObject();
            GrowthStage stage = drugObject.getStage(this.stage + 1);
            if (stage != null) {
                this.stage = stage.getStage();
                stage.place(location.getLocation());
                this.growTime = stage.getGrowTime();
            } else {

                growTime = -5;
            }
        }

    }

    public void harvest(Player player, boolean piston) {
        Drug drugObject = getDrugObject();
        worldDrugHarvested = true;
        GrowthStage stage = drugObject.getStage(this.stage);
        if (stage != null) {

            Level level1 = DrugPlayer.get(player).getLevel() == 0 ? null : Level.get(DrugPlayer.get(player).getLevel());
            stage.getHarvest().drop(getLocation().getLocation(), drugObject, level1);

            final boolean maxStage = this.stage == getMaxStage();
            this.stage = 0;
            this.growTime = drugObject.getStage(0).getGrowTime();

            stage.getBlocks().forEach((integer, cropBlock) -> cropBlock.getNewLocation(location.getLocation()).getBlock().setType(Material.AIR));
            drugPlanting = true;
            WorldDrugLocation worldDrugLocation = WorldDrugLocation.fromLocation(location.getLocation());
            worldDrugLocation.setY(worldDrugLocation.getY() - 1);
            worldDrugLocation.getLocation().getBlock().setType(drugObject.getRequiredSoil().getType(), true);
            worldDrugLocation.getLocation().getBlock().setData(drugObject.getRequiredSoil().getData(), true);
            drugPlanting = false;

            if (maxStage) {

                DrugPlayer drugPlayer = DrugPlayer.get(player);

                if (drugPlayer != null) {
                    int level = drugPlayer.getLevel();
                    int experienceToGain = LevelHandler.get().getExperienceToGain(drug, level, piston);
                    if (experienceToGain > 0) {
                        drugPlayer.addExperience(player, experienceToGain);
                    }
                }
            }
            DrugHandler.get().getWorldDrugConfig().removeDrug(this);

        }
    }

    public void plant() {
        drugPlanting = true;
        Drug drugObject = getDrugObject();
        drugObject.getStage(stage).place(location.getLocation());
        drugPlanting = false;
    }


    public int getStage() {

        return stage;
    }

    public boolean isPlanting() {
        return drugPlanting;
    }
}

