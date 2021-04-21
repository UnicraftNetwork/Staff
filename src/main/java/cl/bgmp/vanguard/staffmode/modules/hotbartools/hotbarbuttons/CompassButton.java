package cl.bgmp.vanguard.staffmode.modules.hotbartools.hotbarbuttons;

import cl.bgmp.butils.gui.GUIButton;
import cl.bgmp.butils.items.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.entity.Player;

public class CompassButton extends GUIButton {

  public CompassButton() {
    super(
        new ItemBuilder(Material.COMPASS)
            .setName("&9&lTeleportation Tool")
            .setLore("&7Click derecho para teletransportarte.")
            .build(),
        0);
  }

  @Override
  public void clickBy(Player player) {} // WorldEdit's teleport tool
}
