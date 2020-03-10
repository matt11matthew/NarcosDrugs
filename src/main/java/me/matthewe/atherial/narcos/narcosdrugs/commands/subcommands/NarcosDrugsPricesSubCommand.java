package me.matthewe.atherial.narcos.narcosdrugs.commands.subcommands;

import me.matthewe.atherial.narcos.narcosdrugs.commands.NarcosDrugsCommand;
import me.matthewe.atherial.narcos.narcosdrugs.commands.NarcosDrugsSubCommand;
import me.matthewe.atherial.narcos.narcosdrugs.config.Messages;
import me.matthewe.atherial.narcos.narcosdrugs.menus.PricesMenu;
import net.atherial.api.plugin.command.spigot.HelpSubCommand;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.List;

/**
 * Created by Matthew E on 6/19/2019 at 5:33 PM for the project NarcosDrugs
 */
public class NarcosDrugsPricesSubCommand extends NarcosDrugsSubCommand {

    public NarcosDrugsPricesSubCommand(NarcosDrugsCommand narcosDrugsCommand) {
        super("prices", narcosDrugsCommand);
        this.permission = Messages.PERMISSION_PLAYER;
        this.playerOnly = true;
    }

    @Override
    public void run(CommandSender sender, String[] args) {
        new PricesMenu((Player) sender).open();
    }

    @Override
    public List<HelpSubCommand> getHelp(String[] strings) {
        return Arrays.asList(HelpSubCommand.builder().command(this.narcosDrugsCommand.label + " prices").permission(this.permission).description(Messages.HELP_PRICES).build());
    }
}
