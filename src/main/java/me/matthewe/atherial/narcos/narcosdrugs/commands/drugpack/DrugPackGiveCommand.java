package me.matthewe.atherial.narcos.narcosdrugs.commands.drugpack;

import me.matthewe.atherial.narcos.narcosdrugs.commands.NarcosDrugsCommand;
import me.matthewe.atherial.narcos.narcosdrugs.commands.NarcosDrugsSubCommand;
import me.matthewe.atherial.narcos.narcosdrugs.config.Messages;
import me.matthewe.atherial.narcos.narcosdrugs.drugpacks.DrugPackHandler;
import net.atherial.api.plugin.command.spigot.CommandUtils;
import net.atherial.api.plugin.command.spigot.HelpSubCommand;
import net.atherial.api.plugin.utilities.GenericUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.List;

/**
 * Created by Matthew E on 6/19/2019 at 2:06 PM for the project NarcosDrugs
 */
public class DrugPackGiveCommand extends NarcosDrugsSubCommand {
    private  DrugPackCommand drugPackCommand;

    public DrugPackGiveCommand(NarcosDrugsCommand narcosDrugsCommand, DrugPackCommand drugPackCommand) {
        super("give", narcosDrugsCommand);
        this.drugPackCommand = drugPackCommand;
        this.permission = Messages.PERMISSION_ADMIN;
    }

    @Override
    public void run(CommandSender sender, String[] args) {
        if (args.length < 2) {
            CommandUtils.sendCommandUsage(sender, "/" + this.narcosDrugsCommand.label + " drugpack give", "(player)", "(tier)", "(amount)");
            return;
        }
        int amount;
        int tier;
        Player player = Bukkit.getPlayer(args[0]);
        if ((player == null) || !player.isOnline()) {
            this.message(sender, Messages.replacePrefix(Messages.ERRORS_CANT_FIND_PLAYER).replaceAll("%player%", args[0]));
            return;
        }
        if (!GenericUtils.isInteger(args[1])) {
            this.message(sender, Messages.replacePrefix(Messages.ERRORS_INVALID_NUMBER).replaceAll("%number%", args[1]));
            return;
        }
        tier = GenericUtils.getInteger(args[1]);
        if (!DrugPackHandler.get().isDrugPack(tier)) {
            this.message(sender, Messages.replacePrefix(Messages.ERRORS_DRUGPACK_DOESNT_EXIST).replaceAll("%tier%", args[1]));
            return;
        }

        if (args.length == 3) {
            if (!GenericUtils.isInteger(args[2])) {
                this.message(sender, Messages.replacePrefix(Messages.ERRORS_INVALID_NUMBER).replaceAll("%number%", args[2]));
                return;
            }
            amount = GenericUtils.getInteger(args[2]);
            if (amount > 64) {
                this.message(sender, Messages.replacePrefix(Messages.ERRORS_NUMBER_TO_HIGH).replaceAll("%below%", "64").replaceAll("%number%", args[2]));
                return;
            }
            if (amount < 1) {
                this.message(sender, Messages.replacePrefix(Messages.ERRORS_NUMBER_TO_LOW).replaceAll("%above%", "1").replaceAll("%number%", args[2]));
                return;
            }
        } else {
            amount = 1;
        }
        DrugPackHandler.get().giveDrugPack(tier, player);
        message(sender, Messages.replacePrefix(Messages.MESSAGES_GIVE_DRUG_PACK)
                .replaceAll("%player%", player.getName())
                .replaceAll("%sender%", sender.getName())
                .replaceAll("%tier%", String.valueOf(tier))
                .replaceAll("%amount%", String.valueOf(amount)));

        message(player, Messages.replacePrefix(Messages.MESSAGES_GET_DRUG_PACK)
                .replaceAll("%player%", player.getName())
                .replaceAll("%sender%", sender.getName())
                .replaceAll("%tier%", String.valueOf(tier))
                .replaceAll("%amount%", String.valueOf(amount)));

    }

    @Override
    public List<HelpSubCommand> getHelp(String[] strings) {
        return Arrays.asList(HelpSubCommand.builder()
                .permission(this.permission)
                .description(Messages.HELP_DRUGPACK_GIVE)
                .command(this.narcosDrugsCommand.label + " drugpack give")
                .arguments("(player)", "(tier)", "(amount)")
                .build());
    }
}
