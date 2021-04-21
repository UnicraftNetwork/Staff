package cl.bgmp.vanguard.staffmode.modules.inventorymemory;

import cl.bgmp.vanguard.staffmode.modules.StaffModeModule;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import org.bukkit.entity.Player;
import org.bukkit.inventory.PlayerInventory;

public class InventoryMemoryModule extends StaffModeModule {
  private Set<InventoryMemoryEntry> entries = new HashSet<>();

  public InventoryMemoryModule() {}

  public void savePlayerInventory(Player player) {
    InventoryMemoryEntry entry = new InventoryMemoryEntry();
    entry.setOwner(player.getName());
    entry.setContents(player.getInventory().getContents());
    entry.setArmor(player.getInventory().getArmorContents());
    entry.setExp(player.getExp());
    entry.setGameMode(player.getGameMode());

    this.wipePlayerInventory(player.getInventory());

    this.entries.add(entry);
  }

  public void restorePlayerInventory(Player player) {
    final Optional<InventoryMemoryEntry> entryOptional = this.getEntry(player);
    if (!entryOptional.isPresent()) return;

    final InventoryMemoryEntry entry = entryOptional.get();
    this.removeEntry(entry);

    player.getInventory().setContents(entry.getContents());
    player.getInventory().setArmorContents(entry.getArmor());
    player.setExp(entry.getExp());
    player.setGameMode(entry.getGameMode());
  }

  private void removeEntry(InventoryMemoryEntry entry) {
    this.entries.remove(entry);
  }

  private Optional<InventoryMemoryEntry> getEntry(Player player) {
    return this.entries.stream().filter(e -> e.getOwner().equals(player.getName())).findFirst();
  }

  private void wipePlayerInventory(PlayerInventory inventory) {
    inventory.clear();
    inventory.setHelmet(null);
    inventory.setChestplate(null);
    inventory.setLeggings(null);
    inventory.setBoots(null);
  }

  @Override
  public boolean isEnabled() {
    return true;
  }
}
