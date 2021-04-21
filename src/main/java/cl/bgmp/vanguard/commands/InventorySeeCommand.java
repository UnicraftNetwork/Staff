package cl.bgmp.vanguard.commands;

import cl.bgmp.minecraft.util.commands.CommandContext;
import cl.bgmp.minecraft.util.commands.annotations.Command;
import cl.bgmp.minecraft.util.commands.annotations.CommandPermissions;
import cl.bgmp.minecraft.util.commands.annotations.CommandScopes;
import cl.bgmp.vanguard.Permissions;
import cl.bgmp.vanguard.staffmode.modules.inventorysee.InventorySeeModule;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class InventorySeeCommand {
  private final InventorySeeModule ism;

  public InventorySeeCommand(InventorySeeModule ism) {
    this.ism = ism;
  }

  @Command(
      aliases = {"invsee"},
      desc = "Revisa el inventario de un jugador.",
      min = 1,
      max = 1)
  @CommandPermissions(Permissions.INVSEE)
  @CommandScopes("player")
  public void staffMode(CommandContext args, CommandSender sender) {
    final String viewedPlayerName = args.getString(0);
    final Player viewedPlayer = Bukkit.getPlayer(viewedPlayerName);

    if (viewedPlayer == null) {
      sender.sendMessage(ChatColor.RED + "Player not found.");
    } else {
      Player viewer = (Player) sender;
      this.ism.previewInventory(viewer, viewedPlayer.getInventory());
    }
  }
}
