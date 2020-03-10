package me.matthewe.atherial.narcos.narcosdrugs.commands.subcommands;

import me.matthewe.atherial.narcos.narcosdrugs.commands.NarcosDrugsCommand;
import me.matthewe.atherial.narcos.narcosdrugs.commands.NarcosDrugsSubCommand;
import me.matthewe.atherial.narcos.narcosdrugs.config.Messages;
import me.matthewe.atherial.narcos.narcosdrugs.wand.WandHandler;
import net.atherial.api.plugin.command.spigot.CommandUtils;
import net.atherial.api.plugin.command.spigot.HelpSubCommand;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.List;

/**
 * Created by Matthew E on 6/19/2019 at 5:33 PM for the project NarcosDrugs
 */
public class NarcosDrugsGiveWandSubCommand extends NarcosDrugsSubCommand {

    public NarcosDrugsGiveWandSubCommand(NarcosDrugsCommand narcosDrugsCommand) {
        super("givewand", narcosDrugsCommand);
        this.permission = Messages.PERMISSION_ADMIN;
    }

    @Override
    public void run(CommandSender sender, String[] args) {
        if (args.length != 1) {
            CommandUtils.sendCommandUsage(sender, "/" + this.narcosDrugsCommand.label + " givewand", "(player)");
            return;
        }
        Player player = Bukkit.getPlayer(args[0]);
        if (player==null||!player.isOnline()){
            message(sender, Messages.replacePrefix(Messages.ERRORS_CANT_FIND_PLAYER).replaceAll("%player%",args[0]));
            return;
        }
        WandHandler.get().giveWand(player);
    }

    @Override
    public List<HelpSubCommand> getHelp(String[] strings) {
        return Arrays.asList(HelpSubCommand.builder().command(this.narcosDrugsCommand.label + " givewand").permission(this.permission).arguments("(player)").description(Messages.HELP_GIVEWAND).build());
    }
}
