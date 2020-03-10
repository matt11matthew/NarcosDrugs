package me.matthewe.atherial.narcos.narcosdrugs.wand;

import me.matthewe.atherial.narcos.narcosdrugs.NarcosDrugs;
import net.atherial.api.plugin.config.json.JsonConfig;
import net.atherial.api.plugin.item.AtherialConfigItem;
import org.bukkit.Material;
import org.bukkit.inventory.ItemFlag;

/**
 * Created by Matthew E on 6/21/2019 at 3:44 PM for the project NarcosDrugs
 */
public class WandConfig extends JsonConfig<NarcosDrugs, WandConfig> {
    public AtherialConfigItem wandItem = AtherialConfigItem.builder()
            .type(Material.BLAZE_ROD)
            .amount(1)
            .data(0)
            .information(AtherialConfigItem.Information.builder()
                    .displayName("&3&lTransfer Wand")
                    .lore(new String[]{"&fShift-Right-Click &7on chests to store","&7drugs in your drug packs."})
                    .build())
            .itemFlags(ItemFlag.values())
            .build();
    public WandConfig(NarcosDrugs plugin) {
        super(plugin, "wand.json");
    }

    @Override
    public Class<WandConfig> getConfigClass() {
        return WandConfig.class;
    }
}
