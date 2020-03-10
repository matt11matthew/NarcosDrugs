package me.matthewe.atherial.narcos.narcosdrugs.drug.crop;

import me.matthewe.atherial.narcos.narcosdrugs.config.Messages;
import me.matthewe.atherial.narcos.narcosdrugs.drug.Drug;
import me.matthewe.atherial.narcos.narcosdrugs.level.Level;
import me.matthewe.atherial.narcos.narcosdrugs.quality.Quality;
import me.matthewe.atherial.narcos.narcosdrugs.quality.QualityHandler;
import net.atherial.api.item.builder.AtherialItem;
import net.atherial.api.plugin.utilities.MessageUtils;
import org.apache.commons.lang3.RandomUtils;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.stream.Collectors;

import static net.atherial.api.plugin.utilities.MessageUtils.colorize;

/**
 * Created by Matthew E on 6/9/2019 at 1:41 PM for the project NarcosDrugs
 */
public class CropDrop {
    private Material type;
    private String displayName;
    private List<String> loreStringList;
    private int data;
    private int minAmount;
    private int maxAmount;
    private Type harvestType = Type.NONE;

    public CropDrop(Material type, String displayName, List<String> loreStringList, int data, int amount, Type harvestType) {
        this.type = type;
        this.displayName = displayName;
        this.loreStringList = loreStringList;
        this.data = data;
        this.maxAmount = amount;
        this.minAmount = amount;
        this.harvestType = harvestType;
    }

    public ItemStack getItemStack(Drug drug, Level level) {

        int amount;
        if (minAmount == maxAmount) {
            amount = minAmount;
        } else {
            int randomAmount = RandomUtils.nextInt(minAmount, maxAmount);
            if (randomAmount < 1) randomAmount = 1;
            amount = randomAmount;
        }
        ItemStack itemStack = AtherialItem.builder()
                .type(type)
                .durability((short) data)
                .displayName(colorize(displayName))
                .itemFlags(ItemFlag.values())
                .amount(amount)
                .lore(loreStringList.stream().map(MessageUtils::colorize).collect(Collectors.toList()))
                .build().getItemStack();

        if ((harvestType != null) && (harvestType != Type.NONE)) {
            net.atherial.api.plugin.item.AtherialItem atherialItem = net.atherial.api.plugin.item.AtherialItem.of(itemStack);


            switch (harvestType) {
                case SEEDS:
                    atherialItem.setData("narcos_drug", drug.getName());
                    atherialItem.setData("narcos_drug_seed", true);
                    atherialItem.setData("narcosdrugs_drug", true);
                    break;
                case DRUG:
                    Quality quality;
                    if (level == null) {
                        quality = QualityHandler.get().getRandom();
                    } else {
                        quality = QualityHandler.get().getQualityMap().get(level.getQuality(drug.getName().toLowerCase()));
                    }

                    atherialItem.setData("narcos_drug", drug.getName());
                    atherialItem.setData("narcos_drug_quality", quality.getName());
                    atherialItem.setData("narcosdrugs_drug", true);
                    if (Messages.QUALITY_LORE) {
                        return AtherialItem.builder().itemStack(atherialItem.getItemStack()).appendLore(colorize(quality.getLore())).build().getItemStack();
                    }
                    break;
            }
            return atherialItem.getItemStack();
        }
        return itemStack;
    }

    public CropDrop(Material type, String displayName, List<String> loreStringList, int data, int minAmount, int maxAmount, Type harvestType) {
        this(type, displayName, loreStringList, data, maxAmount, harvestType);
        this.minAmount = minAmount;
        this.maxAmount = maxAmount;
    }

    public void drop(Location location, Drug drug, Level level) {
        location.getWorld().dropItem(location, getItemStack(drug, level));
    }

    public void setHarvestType(Type harvestType) {
        this.harvestType = harvestType;
    }

    public Type getHarvestType() {
        return harvestType;
    }

    public int getMaxAmount() {
        return maxAmount;
    }

    public int getMinAmount() {
        return minAmount;
    }

    public Material getType() {
        return type;
    }

    public String getDisplayName() {
        return displayName;
    }

    public List<String> getLoreStringList() {
        return loreStringList;
    }

    public int getData() {
        return data;
    }


//    public boolean isDrug() {
//        return drug;
//    }

//    public void setDrug(boolean drug) {
//        this.drug = drug;
//    }

    public static enum Type {
        NONE, SEEDS, DRUG;
    }
}
