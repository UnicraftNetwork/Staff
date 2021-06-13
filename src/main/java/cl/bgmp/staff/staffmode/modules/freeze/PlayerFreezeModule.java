package cl.bgmp.staff.staffmode.modules.freeze;

import cl.bgmp.staff.staffmode.modules.StaffModeModule;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.inject.Singleton;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerBedEnterEvent;
import org.bukkit.event.player.PlayerBucketEmptyEvent;
import org.bukkit.event.player.PlayerBucketFillEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.vehicle.VehicleDamageEvent;
import org.bukkit.event.vehicle.VehicleEnterEvent;
import org.bukkit.event.vehicle.VehicleExitEvent;
import org.bukkit.event.vehicle.VehicleMoveEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.util.Vector;

// Tell people hitting the frozen player they're hitting
@Singleton
public class PlayerFreezeModule extends StaffModeModule {
  private static final Material FREEZE_MATERIAL = Material.ICE;
  public static final int MAX_TICK = Integer.MAX_VALUE - 20;

  private List<Player> players = new ArrayList<>();
  public Cache<UUID, String> offlineFrozenCache =
      CacheBuilder.newBuilder().expireAfterWrite(3, TimeUnit.MINUTES).build();

  public void freeze(CommandSender freezer, Player player) {
    this.players.add(player);
    player.sendTitle(
        "",
        ChatColor.RED + "You have been frozen by " + ChatColor.RESET + freezer.getName(),
        5,
        MAX_TICK,
        5);
    player.playSound(player.getLocation(), Sound.ENTITY_ENDER_DRAGON_GROWL, 10F, 1F);
  }

  public void unfreeze(CommandSender unfreezer, Player player) {
    this.players.remove(player);
    player.sendTitle("", "", 0, 0, 0);
    player.playSound(player.getLocation(), Sound.ENTITY_ENDER_DRAGON_GROWL, 10F, 10F);
  }

  public boolean isFreezing(Player player) {
    return this.players.contains(player);
  }

  public boolean isPlayerFreezing(Player player) {
    return player.getInventory().getItemInMainHand().getType() == FREEZE_MATERIAL;
  }

  public void cachePlayer(Player player) {
    this.offlineFrozenCache.put(player.getUniqueId(), player.getName());
  }

  public void removeCachedPlayer(UUID uuid) {
    this.offlineFrozenCache.invalidate(uuid);
  }

  private boolean isCached(UUID uuid) {
    return this.offlineFrozenCache.getIfPresent(uuid) != null;
  }

  @EventHandler
  public void onPlayerJoin(PlayerJoinEvent event) {
    Player player = event.getPlayer();

    if (this.isCached(player.getUniqueId())) {
      this.removeCachedPlayer(player.getUniqueId());
      this.freeze(Bukkit.getConsoleSender(), event.getPlayer());
    } else {
      player.sendTitle("", "", 0, 0, 0);
    }
  }

  @EventHandler
  public void onPlayerQuit(PlayerQuitEvent event) {
    if (this.isFreezing(event.getPlayer())) {
      Player player = event.getPlayer();

      this.players.remove(player);
      this.cachePlayer(player);
    }
  }

  @EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
  public void onPlayerInteractAtEntity(PlayerInteractAtEntityEvent event) {
    Player clicker = event.getPlayer();
    if (!staffMode.isEnabledFor(clicker)) {
      if (!this.isFreezing(clicker)) return;

      event.setCancelled(true);
    }

    Entity entity = event.getRightClicked();
    if (!(entity instanceof Player)) return;

    EquipmentSlot hand = event.getHand();
    if (hand == EquipmentSlot.OFF_HAND) return;

    Material material = clicker.getInventory().getItemInMainHand().getType();
    if (material != FREEZE_MATERIAL) return;

    Player clicked = (Player) entity;

    if (this.isFreezing(clicked)) {
      this.unfreeze(clicker, clicked);
    } else {
      this.freeze(clicker, clicked);
    }
  }

  @EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
  public void onPlayerMove(PlayerMoveEvent event) {
    if (this.isFreezing(event.getPlayer())) {
      Location old = event.getFrom();
      old.setPitch(event.getTo().getPitch());
      old.setYaw(event.getTo().getYaw());
      event.setTo(old);
    }
  }

  @EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
  public void onPlayerInteract(PlayerInteractEvent event) {
    Player player = event.getPlayer();
    if (!this.isFreezing(player)) return;

    event.setCancelled(true);
  }

  @EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
  public void onPlayerInteractEntity(PlayerInteractEntityEvent event) {
    Player player = event.getPlayer();
    if (!this.isFreezing(player)) return;

    event.setCancelled(true);
  }

  @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
  public void onVehicleMove(VehicleMoveEvent event) {
    Entity entity = event.getVehicle().getPassenger();
    if (!(entity instanceof Player)) return;

    Player player = (Player) entity;
    if (!event.getVehicle().isEmpty() && this.isFreezing(player)) {
      event.getVehicle().setVelocity(new Vector(0, 0, 0));
    }
  }

  @EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
  public void onVehicleEnter(VehicleEnterEvent event) {
    Entity entity = event.getEntered();
    if (!(entity instanceof Player)) return;

    Player player = (Player) entity;
    if (this.isFreezing(player)) {
      event.setCancelled(true);
    }
  }

  @EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
  public void onVehicleExit(VehicleExitEvent event) {
    LivingEntity entity = event.getExited();
    if (!(entity instanceof Player)) return;

    Player player = (Player) entity;
    if (this.isFreezing(player)) {
      event.setCancelled(true);
    }
  }

  @EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
  public void onVehicleDamage(VehicleDamageEvent event) {
    Entity entity = event.getAttacker();
    if (!(entity instanceof Player)) return;

    Player player = (Player) entity;
    if (this.isFreezing(player)) {
      event.setCancelled(true);
    }
  }

  @EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
  public void onPlayerBucketFill(PlayerBucketFillEvent event) {
    Player player = event.getPlayer();
    if (!this.isFreezing(player)) return;

    event.setCancelled(true);
  }

  @EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
  public void onPlayerBucketEmpty(PlayerBucketEmptyEvent event) {
    Player player = event.getPlayer();
    if (!this.isFreezing(player)) return;

    event.setCancelled(true);
  }

  @EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
  public void onInventoryClick(InventoryClickEvent event) {
    Entity entity = event.getWhoClicked();
    if (!(entity instanceof Player)) return;

    Player player = (Player) entity;
    if (!this.isFreezing(player)) return;

    event.setCancelled(true);
  }

  @EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
  public void onPlayerDropItem(PlayerDropItemEvent event) {
    if (!this.isFreezing(event.getPlayer())) return;

    event.setCancelled(true);
  }

  @EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
  public void onEntityDamage(EntityDamageByEntityEvent event) {
    Entity entity = event.getDamager();
    if (!(entity instanceof Player)) return;

    Player player = (Player) entity;
    if (!this.isFreezing(player)) return;

    event.setCancelled(true);
  }

  @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
  public void onPlayerEnterBed(PlayerBedEnterEvent event) {
    if (!this.isFreezing(event.getPlayer())) return;

    event.setCancelled(true);
  }

  @EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
  public void onBlockBreak(BlockBreakEvent event) {
    Player player = event.getPlayer();

    if (this.isFreezing(player)) {
      event.setCancelled(true);
    }
  }

  @EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
  public void onBlockPlace(BlockPlaceEvent event) {
    Player player = event.getPlayer();

    if (this.isFreezing(player)) {
      event.setCancelled(true);
    }
  }

  @Override
  public boolean isEnabled() {
    return true;
  }
}
