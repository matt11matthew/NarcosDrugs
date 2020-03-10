package me.matthewe.atherial.narcos.narcosdrugs.drugpacks;

import net.atherial.api.plugin.item.AtherialDisplayItem;

/**
 * Created by Matthew E on 6/10/2019 at 7:26 PM for the project NarcosDrugs
 */
public class DrugPack {
    private int tier;
    private int rows;
    private AtherialDisplayItem displayItem;

    public DrugPack(int tier, int rows, AtherialDisplayItem displayItem) {
        this.tier = tier;
        this.rows = rows;
        this.displayItem = displayItem;
    }




    public static DrugPack get(int tier) {
        return DrugPackHandler.get().getDrugPackConfig().getDrugPack(tier);
    }

    public int getTier() {
        return tier;
    }

    public int getRows() {
        return rows;
    }

    public AtherialDisplayItem getDisplayItem() {
        return displayItem;
    }
}
