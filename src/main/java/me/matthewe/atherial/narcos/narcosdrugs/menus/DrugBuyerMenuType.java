package me.matthewe.atherial.narcos.narcosdrugs.menus;

import me.matthewe.atherial.narcos.narcosdrugs.config.MenuConfig;

/**
 * Created by Matthew E on 6/20/2019 at 7:37 PM for the project NarcosDrugs
 */
public enum DrugBuyerMenuType {
    MAIN, SELLALL, SELL_IN_SLOT;


    public String getTitle() {
        switch (this) {
            case MAIN:
                return MenuConfig.get().drugBuyerMenu.mainMenu.title;
            case SELLALL:
                break;
            case SELL_IN_SLOT:
                return MenuConfig.get().drugBuyerMenu.sellDrugMenu.title;
        }
        return "&cNo title";
    }

    public int getRows() {
        switch (this) {
            case MAIN:
                return MenuConfig.get().drugBuyerMenu.mainMenu.rows;
            case SELLALL:
                break;
            case SELL_IN_SLOT:
                return MenuConfig.get().drugBuyerMenu.sellDrugMenu.rows;
        }
        return 4;

    }
}
