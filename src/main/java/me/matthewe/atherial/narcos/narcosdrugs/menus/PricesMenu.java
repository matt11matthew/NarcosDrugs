package me.matthewe.atherial.narcos.narcosdrugs.menus;

import me.matthewe.atherial.narcos.narcosdrugs.config.MenuConfig;
import me.matthewe.atherial.narcos.narcosdrugs.config.Messages;
import me.matthewe.atherial.narcos.narcosdrugs.drug.Drug;
import me.matthewe.atherial.narcos.narcosdrugs.drug.DrugHandler;
import me.matthewe.atherial.narcos.narcosdrugs.level.Level;
import me.matthewe.atherial.narcos.narcosdrugs.player.DrugPlayer;
import me.matthewe.atherial.narcos.narcosdrugs.utilities.NarcosAtherialMenu;
import net.atherial.api.menu.item.MenuItem;
import org.bukkit.entity.Player;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import static net.atherial.api.plugin.utilities.MessageUtils.colorize;

/**
 * Created by Matthew E on 6/19/2019 at 5:33 PM for the project NarcosDrugs
 */
public class PricesMenu extends NarcosAtherialMenu {

    public PricesMenu(Player player) {
        super(player, colorize(MenuConfig.get().pricesMenu.title), MenuConfig.get().pricesMenu.rows);
        MenuConfig menuConfig = MenuConfig.get();
        MenuConfig.PricesMenu pricesMenu = menuConfig.pricesMenu;

        List<Integer> priceSlots = new ArrayList<>();
        for (int priceSlot : pricesMenu.priceSlots) {
            priceSlots.add(priceSlot);
        }
        if (pricesMenu.fillBackground) {
            for (int i = 0; i < (this.rows * 9); i++) {
                if (priceSlots.contains(i)) {
                    continue;
                }
                addItem(fixMenuItem(new MenuItem(i, pricesMenu.backgroundItem.create().getItemStack(), (player1, clickType) -> {
                })));
            }
        }
        List<Drug> drugs = new ArrayList<>(DrugHandler.get().getDrugConfig().getDrugs().values());
        for (int i = 0; i < pricesMenu.priceSlots.length; i++) {
            int slot = pricesMenu.priceSlots[i];

            Drug drug;
            try {
                drug = drugs.get(i);
            } catch (Exception ignored) {
                continue;
            }
            if (drug == null) {
                continue;
            }
            DrugPlayer drugPlayer = DrugPlayer.get(player);
            if (drugPlayer == null) {
                return;
            }
            String price = "";
            boolean foundPrice;
            int lvl = 0;
            if (drugPlayer.getLevel() == 0) {
                lvl = 1;
            } else {
                lvl = drugPlayer.getLevel();
            }
            Level level = Level.get(lvl);
            if (!level.prices.containsKey(drug.getName().toLowerCase())) {
                price = Messages.PRICE_UNKNOWN;
                foundPrice = false;
            } else {
                price = new DecimalFormat("#,###.##").format(level.prices.get(drug.getName().toLowerCase()));
                foundPrice = true;
            }
            final String finalPrice = price;
            addItem(fixMenuItem(new MenuItem(slot, pricesMenu.drugItem.create().type(drug.getDisplayType()).replace(s -> colorize(s)
                    .replaceAll("%drug%", drug.getName())
                    .replaceAll("%xp_gained%", new DecimalFormat("#,###").format(drugPlayer.getExperience()))
//                    .replaceAll("\\$%price%", foundPrice ? "$%price%" : "%price%")
                    .replaceAll("%price%", finalPrice)).getItemStack(), (player1, clickType) -> {

            })));
        }
    }


}
