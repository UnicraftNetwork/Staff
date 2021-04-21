package cl.bgmp.vanguard.staffmode.modules;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.player.PlayerBedEnterEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.vehicle.VehicleEnterEvent;

public class EnvironmentControlModule extends StaffModeModule {

  @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
  public void onBlockPlace(BlockPlaceEvent event) {
    event.setCancelled(this.staffMode.isEnabledFor(event.getPlayer()));
  }

  @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
  public void onBlockBreak(BlockBreakEvent event) {
    event.setCancelled(this.staffMode.isEnabledFor(event.getPlayer()));
  }

  @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
  public void onPlayerInteract(PlayerInteractEvent event) {
    event.setCancelled(this.staffMode.isEnabledFor(event.getPlayer()));
  }

  @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
  public void onItemDrop(PlayerDropItemEvent event) {
    event.setCancelled(this.staffMode.isEnabledFor(event.getPlayer()));
  }

  @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
  public void onPlayerEnterBed(PlayerBedEnterEvent event) {
    event.setCancelled(this.staffMode.isEnabledFor(event.getPlayer()));
  }

  @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
  public void onPlayerRideVehicle(VehicleEnterEvent event) {
    Entity entity = event.getEntered();
    if (!(entity instanceof Player)) return;

    Player player = (Player) entity;
    event.setCancelled(this.staffMode.isEnabledFor(player));
  }

  @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
  public void onEntityDamagedByPlayer(EntityDamageByEntityEvent event) {
    Entity damager = event.getDamager();
    if (!(damager instanceof Player)) return;

    Player player = (Player) damager;
    event.setCancelled(staffMode.isEnabledFor(player));
  }

  @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
  public void onItemPickupByPlayer(EntityPickupItemEvent event) {
    Entity entity = event.getEntity();
    if (!(entity instanceof Player)) return;

    Player picker = (Player) entity;
    event.setCancelled(this.staffMode.isEnabledFor(picker));
  }

  @Override
  public boolean isEnabled() {
    return true;
  }
}
