package cl.bgmp.vanguard.staffmode.modules.hotbartools.hotbarbuttons;

import cl.bgmp.butils.gui.GUIButton;
import cl.bgmp.butils.items.ItemBuilder;
import cl.bgmp.vanguard.staffmode.StaffModuleManager;
import cl.bgmp.vanguard.staffmode.modules.VanishModule;
import com.google.inject.Inject;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class VanishButton extends GUIButton {
  private final StaffModuleManager smm;
  private final ItemStack vanishOffVariant =
      new ItemBuilder(Material.LEVER).setName("&f&lVanish &c&lOFF").build();

  @Inject
  public VanishButton(StaffModuleManager smm) {
    super(new ItemBuilder(Material.REDSTONE_TORCH).setName("&f&lVanish &a&lON").build(), 2);
    this.smm = smm;
  }

  @Override
  public void clickBy(Player player) {
    final VanishModule vm = this.smm.needModule(VanishModule.class);
    if (vm == null) return;

    if (vm.isEnabledFor(player)) {
      vm.disableFor(player);
      player.getInventory().setItem(this.getSlot(), this.vanishOffVariant);
    } else {
      vm.enableFor(player);
      player.getInventory().setItem(this.getSlot(), this.itemStack);
    }
  }
}
