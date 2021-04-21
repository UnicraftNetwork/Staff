package cl.bgmp.vanguard.staffmode.modules.inventorymemory;

import org.bukkit.GameMode;
import org.bukkit.inventory.ItemStack;

public class InventoryMemoryEntry {
  private String owner;
  private ItemStack[] contents;
  private ItemStack[] armor;
  private float exp;
  private GameMode gameMode;

  public InventoryMemoryEntry() {}

  public String getOwner() {
    return owner;
  }

  public void setOwner(String owner) {
    this.owner = owner;
  }

  public ItemStack[] getContents() {
    return contents;
  }

  public void setContents(ItemStack[] contents) {
    this.contents = contents;
  }

  public ItemStack[] getArmor() {
    return armor;
  }

  public void setArmor(ItemStack[] armor) {
    this.armor = armor;
  }

  public float getExp() {
    return exp;
  }

  public void setExp(float exp) {
    this.exp = exp;
  }

  public GameMode getGameMode() {
    return gameMode;
  }

  public void setGameMode(GameMode gameMode) {
    this.gameMode = gameMode;
  }
}
