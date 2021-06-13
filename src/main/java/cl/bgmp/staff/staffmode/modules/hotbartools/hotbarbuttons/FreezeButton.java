package cl.bgmp.staff.staffmode.modules.hotbartools.hotbarbuttons;

import cl.bgmp.butils.gui.GUIButton;
import cl.bgmp.butils.items.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.entity.Player;

public class FreezeButton extends GUIButton {

  public FreezeButton() {
    super(new ItemBuilder(Material.ICE).setName("&b&lPlayer Freeze").build(), 6);
  }

  @Override
  public void clickBy(Player player) {}
}
