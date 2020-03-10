package me.matthewe.atherial.narcos.narcosdrugs.commands.subcommands;

import me.matthewe.atherial.narcos.narcosdrugs.commands.NarcosDrugsCommand;
import me.matthewe.atherial.narcos.narcosdrugs.commands.NarcosDrugsSubCommand;
import me.matthewe.atherial.narcos.narcosdrugs.config.Messages;
import net.atherial.api.plugin.command.spigot.CommandUtils;
import org.bukkit.command.CommandSender;

import java.util.Arrays;
import java.util.List;


public class NarcosDrugsHelpSubCommand extends NarcosDrugsSubCommand {

    public NarcosDrugsHelpSubCommand(NarcosDrugsCommand narcosDrugsCommand) {
        super("help", narcosDrugsCommand);
        this.permission = Messages.PERMISSION_PLAYER;

    }

    @Override
    public void run(CommandSender sender, String[] args) {
        if (args.length != 0) {
            CommandUtils.sendCommandUsage(sender, "/" + this.narcosDrugsCommand.label + " help");
            return;
        }
        this.narcosDrugsCommand.sendHelp(sender,args);
    }

    @Override
    public List<net.atherial.api.plugin.command.spigot.HelpSubCommand> getHelp(String[] strings) {
        return Arrays.asList(net.atherial.api.plugin.command.spigot.HelpSubCommand.builder()
                .command(this.narcosDrugsCommand.label + " help")
                .permission(this.permission)
                .description(Messages.HELP_HELP)
                .build());
    }
}
