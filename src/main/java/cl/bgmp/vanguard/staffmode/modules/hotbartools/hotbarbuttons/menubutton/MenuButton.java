package cl.bgmp.vanguard.staffmode.modules.hotbartools.hotbarbuttons.menubutton;

import cl.bgmp.butils.gui.GUIButton;
import cl.bgmp.butils.items.ItemBuilder;
import com.google.inject.Inject;
import org.bukkit.Material;
import org.bukkit.entity.Player;

public class MenuButton extends GUIButton {
  private MenuButtonGUI menuButtonGUI;

  @Inject
  public MenuButton(MenuButtonGUI menuButtonGUI) {
    super(
        new ItemBuilder(Material.CHEST)
            .setName("&e&lMenu")
            .setLore("&7&lClick derecho para abrir.")
            .build(),
        7);
    this.menuButtonGUI = menuButtonGUI;
  }

  @Override
  public void clickBy(Player player) {
    player.openInventory(this.menuButtonGUI.getInventory());
  }
}
