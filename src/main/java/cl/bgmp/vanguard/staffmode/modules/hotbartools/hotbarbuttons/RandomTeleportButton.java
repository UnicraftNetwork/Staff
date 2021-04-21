package cl.bgmp.vanguard.staffmode.modules.hotbartools.hotbarbuttons;

import cl.bgmp.butils.gui.GUIButton;
import cl.bgmp.butils.items.ItemBuilder;
import cl.bgmp.vanguard.Permissions;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import javax.annotation.Nullable;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;

public class RandomTeleportButton extends GUIButton {

  public RandomTeleportButton() {
    super(
        new ItemBuilder(Material.MUSIC_DISC_CAT)
            .setName("&c&lRandom Teleport")
            .setLore("&7Click derecho para teletransportarte", "&7a un jugador aleatorio.")
            .build(),
        8);
  }

  @Override
  public void clickBy(Player player) {
    Player randomPlayer = getRandomOnlinePlayer();
    if (randomPlayer == null) {
      player.sendMessage(ChatColor.RED + "No hay nadie a quien teletransportarse.");
    } else {
      player.teleport(randomPlayer);
    }
  }

  /**
   * Retrieves a random online player, excluding those ones who are allowed into vanguard mode
   *
   * @return The suitable online player, or null if not found
   */
  @Nullable
  private Player getRandomOnlinePlayer() {
    Player randomPlayer;

    List<String> teleportablePlayerNames =
        Bukkit.getOnlinePlayers().stream()
            .filter(onlinePlayer -> !(onlinePlayer.hasPermission(Permissions.STAFF_MODE_USE)))
            .map(HumanEntity::getName)
            .collect(Collectors.toList());

    if (teleportablePlayerNames.isEmpty()) return null;

    int random = new Random().nextInt(teleportablePlayerNames.size());
    randomPlayer = Bukkit.getPlayer(teleportablePlayerNames.get(random));

    return randomPlayer;
  }
}
