package me.matthewe.atherial.narcos.narcosdrugs.commands;

import me.matthewe.atherial.narcos.narcosdrugs.NarcosDrugs;
import me.matthewe.atherial.narcos.narcosdrugs.config.Messages;
import net.atherial.api.plugin.command.spigot.SpigotCommand;
import net.atherial.api.plugin.utilities.MessageUtils;
import org.bukkit.command.CommandSender;


public abstract class NarcosDrugsSubCommand extends SpigotCommand {
    public NarcosDrugsCommand narcosDrugsCommand;
    public NarcosDrugs narcosDrugs;


    public NarcosDrugsSubCommand(String name,NarcosDrugsCommand narcosDrugsCommand) {
        super(name);
        this.narcosDrugsCommand = narcosDrugsCommand;
        this.narcosDrugs = NarcosDrugs.getInstance();
    }

    public String colorize(String message) {
        return Messages.replacePrefix(message);
    }

    public String replacePrefix(String message) {
        return colorize(message);
    }

    public void message(CommandSender sender, String message) {
        MessageUtils.message(sender, message);
    }
}
