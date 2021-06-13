package cl.bgmp.staff.commands;

import cl.bgmp.minecraft.util.commands.CommandContext;
import cl.bgmp.minecraft.util.commands.annotations.Command;
import cl.bgmp.minecraft.util.commands.annotations.CommandPermissions;
import cl.bgmp.minecraft.util.commands.annotations.CommandScopes;
import cl.bgmp.staff.Permissions;
import cl.bgmp.staff.staffmode.StaffMode;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class StaffModeCommand {
  private final StaffMode staffMode;

  public StaffModeCommand(StaffMode staffMode) {
    this.staffMode = staffMode;
  }

  @Command(
      aliases = {"staff", "mod"},
      desc = "Turn on staff mode.",
      max = 0)
  @CommandPermissions(Permissions.STAFF_MODE_USE)
  @CommandScopes("player")
  public void staffMode(CommandContext args, CommandSender sender) {
    Player player = (Player) sender;

    if (this.staffMode.isEnabledFor(player)) {
      this.staffMode.disableFor(player);
      player.sendMessage(ChatColor.RED + "You're no longer in staff mode.");
    } else {
      this.staffMode.enableFor(player);
      player.sendMessage(ChatColor.GREEN + "You're now in staff mode.");
    }
  }
}
