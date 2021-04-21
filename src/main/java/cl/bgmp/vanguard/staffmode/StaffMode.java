package cl.bgmp.vanguard.staffmode;

import cl.bgmp.vanguard.Vanguard;
import cl.bgmp.vanguard.staffmode.modules.VanishModule;
import cl.bgmp.vanguard.staffmode.modules.hotbartools.HotBarToolsModule;
import cl.bgmp.vanguard.staffmode.modules.inventorymemory.InventoryMemoryModule;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import java.util.HashSet;
import java.util.Set;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.PluginManager;

@Singleton
public class StaffMode implements Listener {
  private final Set<Player> users = new HashSet<>();
  private final StaffModuleManager smm;

  @Inject
  public StaffMode(Vanguard vanguard, StaffModuleManager smm) {
    this.smm = smm;

    final PluginManager pm = vanguard.getServer().getPluginManager();
    pm.registerEvents(this, vanguard);
  }

  public void shutDown() {
    for (Player user : users) {
      this.disableFor(user);
    }
  }

  public StaffModuleManager getStaffModuleManager() {
    return smm;
  }

  public boolean isEnabledFor(Player player) {
    return this.users.contains(player);
  }

  public void enableFor(Player player) {
    final InventoryMemoryModule imm = this.smm.needModule(InventoryMemoryModule.class);
    final HotBarToolsModule hbtm = this.smm.needModule(HotBarToolsModule.class);
    final VanishModule vm = this.smm.needModule(VanishModule.class);

    if (imm != null) imm.savePlayerInventory(player);
    if (hbtm != null) hbtm.deliverItemsTo(player);
    if (vm != null) vm.enableFor(player);

    this.users.add(player);
  }

  public void disableFor(Player player) {
    final InventoryMemoryModule imm = this.smm.needModule(InventoryMemoryModule.class);
    final VanishModule vm = this.smm.needModule(VanishModule.class);

    if (imm != null) imm.restorePlayerInventory(player);
    if (vm != null) vm.disableFor(player);

    this.users.remove(player);
  }

  @EventHandler(priority = EventPriority.LOWEST)
  public void onPlayerQuit(PlayerQuitEvent event) {
    Player player = event.getPlayer();
    if (this.isEnabledFor(player)) this.disableFor(player);
  }
}
