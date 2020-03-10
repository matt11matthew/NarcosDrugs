package me.matthewe.atherial.narcos.narcosdrugs.commands.subcommands.seed;

import me.matthewe.atherial.narcos.narcosdrugs.commands.NarcosDrugsCommand;
import me.matthewe.atherial.narcos.narcosdrugs.commands.NarcosDrugsSubCommand;
import net.atherial.api.plugin.command.spigot.HelpSubCommand;
import org.bukkit.command.CommandSender;

import java.util.List;

/**
 * Created by Matthew E on 6/10/2019 at 10:20 PM for the project NarcosDrugs
 */
public class SeedSubCommand extends NarcosDrugsSubCommand {
    public SeedSubCommand(NarcosDrugsCommand narcosDrugsCommand) {
        super("seed",narcosDrugsCommand);
        addSubCommand(new SeedGiveSubCommand(this,narcosDrugsCommand));
    }

    @Override
    public void run(CommandSender commandSender, String[] strings) {

    }

    @Override
    public List<HelpSubCommand> getHelp(String[] strings) {
        return narcosDrugsCommand.getHelp(strings,getSubCommandMap()) ;
    }
}
