package me.matthewe.atherial.narcos.narcosdrugs.normal;

import org.bukkit.CropState;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.CocoaPlant;
import org.bukkit.material.Crops;

/**
 * Created by Matthew E on 8/10/2019 at 11:43 PM for the project NarcosDrugs
 */
public enum NormalType {
    PUMPKIN, MELON, WHEAT, CACTUS, SUGAR_CANE, RED_MUSHROOM, BROWN_MUSHROOM, NETHER_WART, COCO_BEANS;

    NormalType() {
    }

    public boolean isBlock(Block block) {
        switch (this) {

            case PUMPKIN:
                return block.getType() == Material.PUMPKIN;
            case MELON:
                return block.getType() == Material.MELON_BLOCK;
            case WHEAT:

                if (block.getType() == MaDterial.CROPS) {

                    Crops crops = (Crops) block.getState().getData();
                    return crops.getState() == CropState.RIPE;
                }
                return false;
            case CACTUS:
                return block.getType() == Material.CACTUS;
            case SUGAR_CANE:
                return block.getType() == Material.SUGAR_CANE_BLOCK;
            case RED_MUSHROOM:
                return block.getType() == Material.HUGE_MUSHROOM_2 || block.getType() == Material.RED_MUSHROOM;
            case BROWN_MUSHROOM:
                return block.getType() == Material.HUGE_MUSHROOM_1 || block.getType() == Material.BROWN_MUSHROOM;
            case NETHER_WART:
                if (block.getType() == Material.NETHER_WARTS) {

                    Crops crops = (Crops) block.getState().getData();
                    return crops.getState() == CropState.RIPE;
                }
                return false;
            case COCO_BEANS:
                if (block.getType() == Material.COCOA) {
                    CocoaPlant crops = (CocoaPlant) block.getState().getData();
                    return crops.getSize() == CocoaPlant.CocoaPlantSize.LARGE;
                }
                return false;
        }
        return false;
    }

    public double getPriceMulti(ItemStack itemStack) {
        if (is(itemStack)) {
            if (this == PUMPKIN && itemStack.getType() == Material.PUMPKIN_SEEDS) {
                return 0.5;
            }
            if (this == MELON && itemStack.getType() == Material.MELON_SEEDS) {
                return 0.5;
            }
            if (this == CACTUS) {
                if (((itemStack.getType() == Material.INK_SACK && itemStack.getDurability() == DyeColor.GREEN.getDyeData())) || itemStack.getType().toString().equalsIgnoreCase("GREEN_DYE")) {
                    return 1;
                }
                if (itemStack.getType() == Material.CACTUS) {
                    return 0.5;
                }
            }
            if (this == SUGAR_CANE) {
                if (itemStack.getType() == Material.PAPER) {
                    return 1.2;
                }
                if (itemStack.getType() == Material.SUGAR) {
                    return 0.9;
                }
            }

        }
        return 1;
    }

    public boolean is(ItemStack itemStack) {
        if (itemStack == null || itemStack.getType() == Material.AIR) {
            return false;
        }
        Material type = itemStack.getType();
        switch (this) {

            case PUMPKIN:
                return type == Material.PUMPKIN || type == Material.PUMPKIN_SEEDS;
            case MELON:
                return type == Material.MELON || type == Material.MELON_BLOCK || type == Material.MELON_SEEDS;
            case WHEAT:
                return type == Material.WHEAT;
            case CACTUS:
                return (type == Material.CACTUS || ((type == Material.INK_SACK && itemStack.getDurability() == DyeColor.GREEN.getDyeData())) || type.toString().equalsIgnoreCase("GREEN_DYE"));
            case SUGAR_CANE:
                return type == Material.SUGAR_CANE || type == Material.SUGAR || type == Material.PAPER;
            case RED_MUSHROOM:
                return type == Material.RED_MUSHROOM;
            case BROWN_MUSHROOM:
                return type == Material.BROWN_MUSHROOM;
            case NETHER_WART:
                return type == Material.NETHER_WARTS;
            case COCO_BEANS:
                return type == Material.COCOA;
        }
        return false;
    }
}
