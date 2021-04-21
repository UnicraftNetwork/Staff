package cl.bgmp.vanguard.staffmode.modules.inventorysee;

import cl.bgmp.butils.items.ItemBuilder;
import cl.bgmp.vanguard.staffmode.modules.StaffModeModule;
import cl.bgmp.vanguard.util.Potions;
import com.google.common.collect.Lists;
import java.time.Duration;
import java.time.Instant;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.HandlerList;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityRegainHealthEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.inventory.DoubleChestInventory;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.plugin.PluginManager;
import org.bukkit.potion.PotionEffect;
import org.bukkit.scheduler.BukkitRunnable;

public class InventorySeeModule extends StaffModeModule {
  public static final Duration TICK = Duration.ofMillis(50);

  protected final HashMap<String, InventoryTrackerEntry> monitoredInventories = new HashMap<>();
  protected final HashMap<String, Instant> updateQueue = new HashMap<>();
  protected BukkitRunnable checkerTask;

  public InventorySeeModule() {}

  private void newCheckerTask() {
    this.checkerTask =
        new BukkitRunnable() {
          @Override
          public void run() {
            checkAllMonitoredInventories();
          }
        };
  }

  private void checkAllMonitoredInventories() {
    if (updateQueue.isEmpty()) return;

    for (Iterator<Map.Entry<String, Instant>> iterator = updateQueue.entrySet().iterator();
        iterator.hasNext(); ) {
      Map.Entry<String, Instant> entry = iterator.next();
      if (entry.getValue().isAfter(Instant.now())) continue;

      Player player = Bukkit.getPlayerExact(entry.getKey());
      if (player != null) {
        checkMonitoredInventories(player);
      }

      iterator.remove();
    }
  }

  @EventHandler
  public void closeMonitoredInventory(final InventoryCloseEvent event) {
    this.monitoredInventories.remove(event.getPlayer().getName());
  }

  @EventHandler(ignoreCancelled = true)
  public void showInventories(final PlayerInteractAtEntityEvent event) {
    if (event.getPlayer().isDead()) return;

    Entity entity = event.getRightClicked();
    if (entity instanceof Player) {
      Player clickedPlayer = (Player) entity;
      if (canPreviewInventory(event.getPlayer())) {
        event.setCancelled(true);
        this.previewPlayerInventory(event.getPlayer(), clickedPlayer.getInventory());
      }
    }
  }

  @EventHandler(ignoreCancelled = true)
  public void showInventories(PlayerInteractEvent event) {
    if (!canPreviewInventory(event.getPlayer())) return;
    if (event.getAction() != Action.RIGHT_CLICK_BLOCK) return;

    Block clickedBlock = event.getClickedBlock();
    if (clickedBlock == null) return;

    if (clickedBlock.getState() instanceof InventoryHolder) {
      event.setCancelled(true);
      this.previewInventory(
          event.getPlayer(), ((InventoryHolder) clickedBlock.getState()).getInventory());
    }
  }

  @EventHandler(priority = EventPriority.MONITOR)
  public void updateMonitoredClick(final InventoryClickEvent event) {
    if (event.getWhoClicked() instanceof Player) {
      Player player = (Player) event.getWhoClicked();

      boolean playerInventory =
          event.getInventory().getType() == InventoryType.CRAFTING; // Crafting Table bug fix
      Inventory inventory;

      if (playerInventory) {
        inventory = player.getInventory();
      } else {
        inventory = event.getInventory();
      }

      invLoop:
      for (Map.Entry<String, InventoryTrackerEntry> entry :
          new HashSet<>(
              this.monitoredInventories.entrySet())) { // avoid ConcurrentModificationException
        String pl = entry.getKey();
        InventoryTrackerEntry tracker = entry.getValue();

        // Because a player can only be viewing one inventory at a time,
        // this is how we determine if we have a match
        if (inventory.getViewers().isEmpty()
            || tracker.getWatched().getViewers().isEmpty()
            || inventory.getViewers().size() > tracker.getWatched().getViewers().size()) continue;

        for (int i = 0; i < inventory.getViewers().size(); i++) {
          if (!inventory.getViewers().get(i).equals(tracker.getWatched().getViewers().get(i))) {
            continue invLoop;
          }
        }

        // a watched user is in a chest
        if (tracker.isPlayerInventory() && !playerInventory) {
          inventory = tracker.getPlayerInventory().getHolder().getInventory();
          playerInventory = true;
        }

        if (playerInventory) {
          this.previewPlayerInventory(
              Bukkit.getServer().getPlayerExact(pl), (PlayerInventory) inventory);
        } else {
          this.previewInventory(Bukkit.getServer().getPlayerExact(pl), inventory);
        }
      }
    }
  }

  @EventHandler(priority = EventPriority.LOWEST)
  public void cancelInventoryClicks(final InventoryClickEvent event) {
    HumanEntity humanEntity = event.getWhoClicked();
    if (!(humanEntity instanceof Player)) return;

    Player player = (Player) humanEntity;
    event.setCancelled(playerIsPreviewing(player) || staffMode.isEnabledFor(player));
  }

  @EventHandler(priority = EventPriority.LOWEST)
  public void cancelInventoryDrags(final InventoryDragEvent event) {
    HumanEntity humanEntity = event.getWhoClicked();
    if (!(humanEntity instanceof Player)) return;

    Player player = (Player) humanEntity;
    event.setCancelled(playerIsPreviewing(player) || staffMode.isEnabledFor(player));
  }

  private boolean playerIsPreviewing(Player player) {
    boolean isPreviewing = false;

    for (InventoryTrackerEntry inventoryTrackerEntry : monitoredInventories.values()) {
      if (inventoryTrackerEntry.getPreview().getHolder() == player) {
        isPreviewing = true;
        break;
      }
    }

    return isPreviewing;
  }

  @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
  public void updateMonitoredInventory(final InventoryClickEvent event) {
    this.scheduleCheck((Player) event.getWhoClicked());
  }

  @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
  public void updateMonitoredInventory(final InventoryDragEvent event) {
    this.scheduleCheck((Player) event.getWhoClicked());
  }

  @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
  public void updateMonitoredPickup(final PlayerPickupItemEvent event) {
    this.scheduleCheck(event.getPlayer());
  }

  @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
  public void updateMonitoredDrop(final PlayerDropItemEvent event) {
    this.scheduleCheck(event.getPlayer());
  }

  @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
  public void updateMonitoredDamage(final EntityDamageEvent event) {
    if (event.getEntity() instanceof Player) {
      this.scheduleCheck((Player) event.getEntity());
    }
  }

  @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
  public void updateMonitoredHealth(final EntityRegainHealthEvent event) {
    if (event.getEntity() instanceof Player) {
      Player player = (Player) event.getEntity();
      if (player.getHealth() == player.getMaxHealth()) return;
      this.scheduleCheck((Player) event.getEntity());
    }
  }

  @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
  public void updateMonitoredHunger(final FoodLevelChangeEvent event) {
    this.scheduleCheck((Player) event.getEntity());
  }

  public boolean canPreviewInventory(Player viewer) {
    return staffMode.isEnabledFor(viewer);
  }

  protected void scheduleCheck(Player updater) {
    if (this.updateQueue.containsKey(updater.getName())) return;

    this.updateQueue.put(updater.getName(), Instant.now().plus(TICK));
  }

  protected void checkMonitoredInventories(Player updater) {
    for (Map.Entry<String, InventoryTrackerEntry> entry : this.monitoredInventories.entrySet()) {
      String pl = entry.getKey();
      InventoryTrackerEntry tracker = entry.getValue();

      if (tracker.isPlayerInventory()) {
        Player holder = (Player) tracker.getPlayerInventory().getHolder();
        assert holder != null;
        if (updater.getName().equals(holder.getName())) {
          this.previewPlayerInventory(
              Bukkit.getServer().getPlayerExact(pl), tracker.getPlayerInventory());
        }
      } else {
        InventoryHolder holder = tracker.getWatched().getHolder();
        assert holder != null;

        this.previewInventory(Bukkit.getServer().getPlayerExact(pl), holder.getInventory());
      }
    }
  }

  protected void previewPlayerInventory(Player viewer, PlayerInventory inventory) {
    if (viewer == null) return;
    Inventory preview = buildPreview(inventory, viewer);
    this.showInventoryPreview(viewer, inventory, preview);
  }

  private Inventory buildPreview(PlayerInventory original, Player viewer) {
    Player holder = (Player) original.getHolder();
    Inventory preview = Bukkit.getServer().createInventory(viewer, 45, getInventoryTitle(holder));

    assert holder != null;

    addItems(preview, original);
    addArmor(preview, original);
    addPotionEffectsItem(preview, holder);
    addHungerItem(preview, holder);
    addHealthItem(preview, holder);

    return preview;
  }

  // Ensures that the title of the inventory is <= 32 characters long to appease Minecraft's
  // restrictions on inventory titles
  private String getInventoryTitle(Player holder) {
    return StringUtils.substring(holder == null ? "Inventory" : holder.getDisplayName(), 0, 32);
  }

  // Handle inventory mapping
  private void addItems(Inventory preview, PlayerInventory original) {
    for (int i = 0; i <= 35; i++) {
      preview.setItem(getInventoryPreviewSlot(i), original.getItem(i));
    }
  }

  // Potion Effects Item
  private void addPotionEffectsItem(Inventory preview, Player holder) {
    boolean hasPotions = holder.getActivePotionEffects().size() > 0;
    ItemBuilder effectsBottle =
        new ItemBuilder(hasPotions ? Material.POTION : Material.GLASS_BOTTLE)
            .setName(ChatColor.AQUA.toString() + ChatColor.ITALIC + "Efectos Activos");

    List<String> lore = Lists.newArrayList();
    if (hasPotions) {
      for (PotionEffect effect : holder.getActivePotionEffects()) {
        lore.add(
            ChatColor.YELLOW
                + Potions.potionEffectTypeName(effect.getType())
                + " "
                + (effect.getAmplifier() + 1));
      }
    } else {
      lore.add(ChatColor.YELLOW + "Sin Efectos");
    }

    effectsBottle.setLore(lore.toArray(new String[0]));
    preview.setItem(6, effectsBottle.build());
  }

  // Hunger Level Item
  private void addHungerItem(Inventory preview, Player holder) {
    ItemBuilder hungerItem =
        new ItemBuilder(Material.COOKED_BEEF)
            .setAmount(holder.getFoodLevel())
            .setName(ChatColor.AQUA.toString() + ChatColor.ITALIC + "Hambre")
            .addFlags(ItemFlag.HIDE_POTION_EFFECTS);
    preview.setItem(7, hungerItem.build());
  }

  // Health Level Item
  private void addHealthItem(Inventory preview, Player holder) {
    ItemBuilder healthItem =
        new ItemBuilder(Material.REDSTONE)
            .setAmount((int) holder.getHealth())
            .setName(ChatColor.AQUA.toString() + ChatColor.ITALIC + "Salud")
            .addFlags(ItemFlag.HIDE_POTION_EFFECTS);
    preview.setItem(8, healthItem.build());
  }

  private void addArmor(Inventory preview, PlayerInventory original) {
    // Set armour manually because CraftBukkit is a derp
    preview.setItem(0, original.getHelmet());
    preview.setItem(1, original.getChestplate());
    preview.setItem(2, original.getLeggings());
    preview.setItem(3, original.getBoots());
  }

  public static int getInventoryPreviewSlot(int inventorySlot) {
    if (inventorySlot < 9) return inventorySlot + 36; // Puts hotbar at the bottom
    else return inventorySlot;
  }

  // FIXME: Custom inventory titles (when anvil-renamed) are not passed over to the fake inventory
  public void previewInventory(Player viewer, Inventory realInventory) {
    if (viewer == null) return;

    if (realInventory instanceof PlayerInventory) {
      previewPlayerInventory(viewer, (PlayerInventory) realInventory);
    } else {
      Inventory fakeInventory;
      if (realInventory instanceof DoubleChestInventory) {
        fakeInventory = Bukkit.createInventory(viewer, realInventory.getSize());
      } else {
        fakeInventory = Bukkit.createInventory(viewer, realInventory.getType());
      }

      fakeInventory.setContents(realInventory.getContents());

      this.showInventoryPreview(viewer, realInventory, fakeInventory);
    }
  }

  protected void showInventoryPreview(
      Player viewer, Inventory realInventory, Inventory fakeInventory) {
    if (viewer == null) {
      return;
    }

    InventoryTrackerEntry entry = this.monitoredInventories.get(viewer.getName());
    if (entry != null
        && entry.getWatched().equals(realInventory)
        && entry.getPreview().getSize() == fakeInventory.getSize()) {
      entry.getPreview().setContents(fakeInventory.getContents());
    } else {
      entry = new InventoryTrackerEntry(realInventory, fakeInventory);
      this.monitoredInventories.put(viewer.getName(), entry);
      viewer.openInventory(fakeInventory);
    }
  }

  @Override
  public void load() {
    PluginManager pm = this.vanguard.getServer().getPluginManager();
    pm.registerEvents(this, this.vanguard);

    this.newCheckerTask();
    this.checkerTask.runTaskTimer(this.vanguard, 0L, 20L);
  }

  @Override
  public void unload() {
    HandlerList.unregisterAll(this);
    this.checkerTask.cancel();
  }

  @Override
  public boolean isEnabled() {
    return true;
  }
}
