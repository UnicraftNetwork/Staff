package cl.bgmp.vanguard.staffmode.modules;

import cl.bgmp.vanguard.Permissions;
import java.util.HashSet;
import java.util.Set;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

/**
 * Handles the vanish state the players making use of the {@link cl.bgmp.vanguard.staffmode.StaffMode}
 * are put into
 */
public class VanishModule extends StaffModeModule {
  private Set<String> vanishedPlayers = new HashSet<>();

  public boolean isEnabledFor(Player player) {
    return vanishedPlayers.contains(player.getName());
  }

  /**
   * Vanishes a player from the rest of the server, unless they have the permission
   * "vanguard.vanish.see".
   *
   * @param vanished The player being vanished.
   */
  public void enableFor(Player vanished) {
    for (Player playerFor : this.vanguard.getServer().getOnlinePlayers()) {
      if (playerFor.hasPermission(Permissions.VANISH_MODE_SEE)) continue;

      playerFor.hidePlayer(this.vanguard, vanished);
    }

    vanishedPlayers.add(vanished.getName());
  }

  /**
   * UnVanishes a player from the rest of the server.
   *
   * @param vanished The vanished player in question.
   */
  public void disableFor(Player vanished) {
    for (Player playerFor : this.vanguard.getServer().getOnlinePlayers()) {
      playerFor.showPlayer(this.vanguard, vanished);
    }

    vanishedPlayers.remove(vanished.getName());
  }

  /**
   * Vanishes a player for another player in particular.
   *
   * @param vanished The vanished player.
   * @param vanishedFor The player who the vanished player will effectively render vanished for.
   */
  public void enableForParticular(Player vanished, Player vanishedFor) {
    if (vanishedFor.hasPermission(Permissions.VANISH_MODE_SEE)) return;

    vanishedFor.hidePlayer(this.vanguard, vanished);
  }

  /**
   * Un-vanishes a player for another player in particular.
   *
   * @param unVanished The un-vanished player.
   * @param unVanishedFor The player who the un-vanished player will effectively render un-vanished
   *     for.
   */
  public void disableForParticular(Player unVanished, Player unVanishedFor) {
    unVanishedFor.showPlayer(this.vanguard, unVanished);
  }

  @EventHandler(priority = EventPriority.MONITOR)
  public void suppressJoinMessage(PlayerJoinEvent event) {
    if (!this.isEnabledFor(event.getPlayer())) return;

    event.setJoinMessage(null);
  }

  @EventHandler(priority = EventPriority.MONITOR)
  public void onVanishedPlayerQuit(PlayerQuitEvent event) {
    if (!this.isEnabledFor(event.getPlayer())) return;

    this.disableFor(event.getPlayer());
    event.setQuitMessage(null);
  }

  /**
   * Helps handling incoming players in relation to already vanished individuals in memory, as
   * Bukkit's hidePlayer() method isn't persistent through logoffs
   *
   * @param event The join event to hook onto
   */
  @EventHandler(priority = EventPriority.MONITOR)
  public void restoreVanishOnJoin(PlayerJoinEvent event) {
    for (String vanishedPlayerName : this.vanishedPlayers) {
      Player vanishedPlayer = Bukkit.getPlayer(vanishedPlayerName);
      this.enableForParticular(vanishedPlayer, event.getPlayer());
    }
  }

  @Override
  public boolean isEnabled() {
    return true;
  }
}
