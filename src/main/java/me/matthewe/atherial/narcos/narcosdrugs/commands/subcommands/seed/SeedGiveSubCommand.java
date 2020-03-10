package me.matthewe.atherial.narcos.narcosdrugs.commands.subcommands.seed;

import me.matthewe.atherial.narcos.narcosdrugs.commands.NarcosDrugsCommand;
import me.matthewe.atherial.narcos.narcosdrugs.commands.NarcosDrugsSubCommand;
import me.matthewe.atherial.narcos.narcosdrugs.config.Messages;
import me.matthewe.atherial.narcos.narcosdrugs.drug.Drug;
import me.matthewe.atherial.narcos.narcosdrugs.drug.DrugHandler;
import net.atherial.api.plugin.command.spigot.CommandUtils;
import net.atherial.api.plugin.command.spigot.HelpSubCommand;
import net.atherial.api.plugin.utilities.GenericUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.List;

/**
 * Created by Matthew E on 6/10/2019 at 10:22 PM for the project NarcosDrugs
 */
public class SeedGiveSubCommand extends NarcosDrugsSubCommand {
    private SeedSubCommand seedSubCommand;

    public SeedGiveSubCommand(SeedSubCommand seedSubCommand, NarcosDrugsCommand narcosDrugsCommand) {
        super("give", narcosDrugsCommand);
        this.seedSubCommand = seedSubCommand;
        this.permission = Messages.PERMISSION_ADMIN;

    }

    @Override
    public void run(CommandSender sender, String[] args) {
        if (args.length != 3) {
            CommandUtils.sendCommandUsage(sender, "/" + seedSubCommand.narcosDrugsCommand.label + " seed give (player) (drug) (amount)");
            return;
        }
        Player player = Bukkit.getPlayer(args[0]);
        if ((player == null) || !player.isOnline()) {
            message(sender, colorize(Messages.ERRORS_CANT_FIND_PLAYER).replaceAll("%player%", args[0]));
            return;
        }
        Drug drug = DrugHandler.get().getDrugConfig().getDrug(args[1]);
        if (drug == null) {
            message(sender, colorize(Messages.ERRORS_DRUG_DOES_NOT_EXIST).replaceAll("%drug%", args[1]));
            return;
        }

        if (!GenericUtils.isInteger(args[2])) {
            message(sender, colorize(Messages.ERRORS_INVALID_NUMBER));
            return;
        }
        int amount = GenericUtils.getInteger(args[2]);
        if (amount < 1) {
            message(sender, colorize(Messages.ERRORS_NUMBER_TO_LOW).replaceAll("%number%", String.valueOf(amount)).replaceAll("%above%", "1"));
            return;
        }
        message(sender, colorize(Messages.MESSAGES_GIVE_DRUG_SEEDS).replaceAll("%player%",player.getName()).replaceAll("%amount%", String.valueOf(amount)).replaceAll("%drug%", drug.getName()));
        message(player, colorize(Messages.MESSAGES_GET_DRUG_SEEDS).replaceAll("%sender%",sender.getName()).replaceAll("%player%",player.getName()).replaceAll("%amount%", String.valueOf(amount)).replaceAll("%drug%", drug.getName()));
        DrugHandler.get().giveDrugSeed(player,drug,amount);
    }

    @Override
    public List<HelpSubCommand> getHelp(String[] strings) {
        return Arrays.asList(HelpSubCommand.builder()
                .permission(this.permission)
                .arguments("(player)", "(drug)", "(amount)")
                .command( seedSubCommand.narcosDrugsCommand.label +" seed give")
                .description(Messages.HELP_SEED_GIVE)
                .build());
    }
}
