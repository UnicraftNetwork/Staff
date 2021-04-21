package cl.bgmp.vanguard.staffmode.modules.hotbartools.hotbarbuttons.menubutton;

import cl.bgmp.butils.gui.GUIButton;
import cl.bgmp.butils.items.ItemBuilder;
import cl.bgmp.vanguard.staffmode.modules.ClearChatModule;
import com.google.inject.Inject;
import org.bukkit.Material;
import org.bukkit.entity.Player;

public class ClearChatButton extends GUIButton {
  private ClearChatModule ccm;

  @Inject
  public ClearChatButton(ClearChatModule ccm) {
    super(
        new ItemBuilder(Material.GREEN_WOOL)
            .setName("&e&lClear Chat")
            .setLore("&7Click derecho para limpiar el chat.")
            .build(),
        14);

    this.ccm = ccm;
  }

  @Override
  public void clickBy(Player player) {
    this.ccm.clearChatBy(player);
  }
}
