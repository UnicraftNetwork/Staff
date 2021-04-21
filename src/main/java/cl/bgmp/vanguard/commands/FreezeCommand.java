package cl.bgmp.vanguard.commands;

import cl.bgmp.minecraft.util.commands.CommandContext;
import cl.bgmp.minecraft.util.commands.annotations.Command;
import cl.bgmp.minecraft.util.commands.annotations.CommandPermissions;
import cl.bgmp.minecraft.util.commands.annotations.CommandScopes;
import cl.bgmp.minecraft.util.commands.exceptions.CommandUsageException;
import cl.bgmp.vanguard.Permissions;
import cl.bgmp.vanguard.staffmode.modules.FreezeModule;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class FreezeCommand {
    private final FreezeModule fzm;

    public FreezeCommand(FreezeModule fzm) {
        this.fzm = fzm;
    }

    @Command(
            aliases = {"freeze", "ss"},
            desc = "Freeze a player.",
            min = 1,
            max = 1)
    @CommandPermissions(Permissions.FREEZE_USE)
    @CommandScopes({"player", "console"})
    public void freeze(CommandContext args, CommandSender sender) throws CommandUsageException {
        Player target = Bukkit.getPlayer(args.getString(0));

        if (fzm.hasFreeze(target)) {
            fzm.removeFreeze(target);
        } else {
            new fzm.target.run();
        }

        if (FreezeModule.hasFreeze(target)) {
            FreezeModule.removeFreeze(target);
            sender.sendMessage("Player is now unfreeze");
        } else {
            new FreezeModule(target).run();
            Bukkit.broadcastMessage(target + "is now freeze");
            sender.sendMessage("Player is now freeze");
        }
    }
}
