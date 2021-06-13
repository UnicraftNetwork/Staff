package cl.bgmp.staff.staffmode.modules;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.player.PlayerBedEnterEvent;
import org.bukkit.event.player.PlayerBucketEmptyEvent;
import org.bukkit.event.player.PlayerBucketFillEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.vehicle.VehicleDamageEvent;
import org.bukkit.event.vehicle.VehicleEnterEvent;

public class EnvironmentControlModule extends StaffModeModule {

  @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
  public void onBlockPlace(BlockPlaceEvent event) {
    if (!this.staffMode.isEnabledFor(event.getPlayer())) return;

    event.setCancelled(true);
  }

  @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
  public void onBlockBreak(BlockBreakEvent event) {
    if (!this.staffMode.isEnabledFor(event.getPlayer())) return;

    event.setCancelled(true);
  }

  @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
  public void onPlayerInteract(PlayerInteractEvent event) {
    if (!this.staffMode.isEnabledFor(event.getPlayer())) return;

    event.setCancelled(true);
  }

  @EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
  public void onPlayerInteractEntity(PlayerInteractEntityEvent event) {
    Player player = event.getPlayer();
    if (!this.staffMode.isEnabledFor(player)) return;

    event.setCancelled(true);
  }

  @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
  public void onItemDrop(PlayerDropItemEvent event) {
    if (!this.staffMode.isEnabledFor(event.getPlayer())) return;

    event.setCancelled(true);
  }

  @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
  public void onPlayerEnterBed(PlayerBedEnterEvent event) {
    if (!this.staffMode.isEnabledFor(event.getPlayer())) return;

    event.setCancelled(true);
  }

  @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
  public void onPlayerRideVehicle(VehicleEnterEvent event) {
    Entity entity = event.getEntered();
    if (!(entity instanceof Player)) return;

    Player player = (Player) entity;
    if (!this.staffMode.isEnabledFor(player)) return;

    event.setCancelled(true);
  }

  @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
  public void onVehicleDamage(VehicleDamageEvent event) {
    Entity entity = event.getAttacker();
    if (!(entity instanceof Player)) return;

    Player player = (Player) entity;
    if (!this.staffMode.isEnabledFor(player)) return;

    event.setCancelled(true);
  }

  @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
  public void onEntityDamagedByPlayer(EntityDamageByEntityEvent event) {
    Entity damager = event.getDamager();
    if (!(damager instanceof Player)) return;

    Player player = (Player) damager;
    if (!this.staffMode.isEnabledFor(player)) return;

    event.setCancelled(true);
  }

  @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
  public void onEntityPickupItem(EntityPickupItemEvent event) {
    Entity entity = event.getEntity();
    if (!(entity instanceof Player)) return;

    Player picker = (Player) entity;
    if (!this.staffMode.isEnabledFor(picker)) return;

    event.setCancelled(true);
  }

  @EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
  public void onPlayerBucketFill(PlayerBucketFillEvent event) {
    Player player = event.getPlayer();
    if (!this.staffMode.isEnabledFor(player)) return;

    event.setCancelled(true);
  }

  @EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
  public void onPlayerBucketEmpty(PlayerBucketEmptyEvent event) {
    Player player = event.getPlayer();
    if (!this.staffMode.isEnabledFor(player)) return;

    event.setCancelled(true);
  }

  @EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
  public void onVehicleEnter(VehicleEnterEvent event) {
    Entity entity = event.getEntered();
    if (!(entity instanceof Player)) return;

    Player player = (Player) entity;
    if (this.staffMode.isEnabledFor(player)) {
      event.setCancelled(true);
    }
  }

  @Override
  public boolean isEnabled() {
    return true;
  }
}
