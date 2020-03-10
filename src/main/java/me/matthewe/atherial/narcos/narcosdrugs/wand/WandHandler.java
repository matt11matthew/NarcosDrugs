package me.matthewe.atherial.narcos.narcosdrugs.wand;

import me.matthewe.atherial.narcos.narcosdrugs.NarcosDrugs;
import me.matthewe.atherial.narcos.narcosdrugs.NarcosHandler;
import me.matthewe.atherial.narcos.narcosdrugs.wand.listeners.WandListener;
import net.atherial.api.plugin.item.AtherialItem;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

/**
 * Created by Matthew E on 6/21/2019 at 3:43 PM for the project NarcosDrugs
 */
public class WandHandler extends NarcosHandler {

    private WandConfig wandConfig;

    public WandHandler(NarcosDrugs narcosDrugs) {
        super(narcosDrugs);
    }

    @Override
    public void onEnable() {
        this.wandConfig = new WandConfig(this.narcosDrugs).load();
        registerListener(new WandListener());

    }

    public static WandHandler get() {
        return NarcosDrugs.getInstance().getHandler(WandHandler.class);
    }

    public void giveWand(Player player) {
        ItemStack itemStack = wandConfig.wandItem.create().getItemStack();
        AtherialItem atherialItem = AtherialItem.of(itemStack);

        atherialItem.setData("narcos_wand", true);
        player.getInventory().addItem(atherialItem.getItemStack());
    }

    @Override
    public void onDisable() {

    }

    @Override
    public void reloadHandler() {
        this.wandConfig = new WandConfig(this.narcosDrugs).load();

    }
}
