package me.matthewe.atherial.narcos.narcosdrugs.commands.subcommands;

import me.matthewe.atherial.narcos.narcosdrugs.commands.NarcosDrugsCommand;
import me.matthewe.atherial.narcos.narcosdrugs.commands.NarcosDrugsSubCommand;
import me.matthewe.atherial.narcos.narcosdrugs.config.Messages;
import net.atherial.api.plugin.command.spigot.CommandUtils;
import net.atherial.api.plugin.command.spigot.HelpSubCommand;
import org.bukkit.command.CommandSender;

import java.util.Arrays;
import java.util.List;

import static me.matthewe.atherial.narcos.narcosdrugs.NarcosHandler.isReloading;

public class ReloadSubCommand extends NarcosDrugsSubCommand {

    public ReloadSubCommand(NarcosDrugsCommand narcosDrugsCommand) {
        super("reload", narcosDrugsCommand);
        this.permission = Messages.PERMISSION_ADMIN;
    }

    @Override
    public void run(CommandSender sender, String[] args) {
        if (args.length != 0) {
            CommandUtils.sendCommandUsage(sender, "/" + this.narcosDrugsCommand.label + " reload");
            return;
        }
        if (isReloading()){
            return;
        }
        this.narcosDrugs.reloadHandlers();
        message(sender, replacePrefix(Messages.MESSAGES_RELOAD));
    }

    @Override
    public List<HelpSubCommand> getHelp(String[] strings) {
        return Arrays.asList(HelpSubCommand.builder()
                .command(this.narcosDrugsCommand.label + " reload")
                .permission(this.permission)
                .description(Messages.HELP_RELOAD)
                .build());
    }
}
