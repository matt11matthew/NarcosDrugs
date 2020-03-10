package me.matthewe.atherial.narcos.narcosdrugs.normal;

import me.matthewe.atherial.narcos.narcosdrugs.utilities.IntegerRange;
import org.bukkit.inventory.ItemStack;

import java.util.Map;

/**
 * Created by Matthew E on 8/10/2019 at 11:47 PM for the project NarcosDrugs
 */
public class Normal {
    private NormalType normalType;


    private Map<Integer, Double> sellMap;
    private Map<Integer, IntegerRange> xpMap;

    public Normal(NormalType normalType, Map<Integer, Double> sellMap, Map<Integer, IntegerRange> xpMap) {
        this.normalType = normalType;
        this.sellMap = sellMap;
        this.xpMap = xpMap;
    }

    public NormalType getNormalType() {
        return normalType;
    }

    public Map<Integer, IntegerRange> getXpMap() {
        return xpMap;

    }

    public Map<Integer, Double> getSellMap() {
        return sellMap;
    }

    public IntegerRange getXPToGive(int level) {
        if (level == 0) {
            level = 1;
        }
        return xpMap.getOrDefault(level, null);
    }

    public double getSellPrice(int level, ItemStack itemStack) {
        double sellPrice = getSellPrice(level);
        if (sellPrice == -1.0D) {
            return -1.0D;
        }
        double priceMulti = normalType.getPriceMulti(itemStack);
        sellPrice *= priceMulti;
        return sellPrice;
    }

    public double getSellPrice(int level) {
        if (level == 0) {
            level = 1;
        }
        return sellMap.getOrDefault(level, -1.0D);
    }
}
