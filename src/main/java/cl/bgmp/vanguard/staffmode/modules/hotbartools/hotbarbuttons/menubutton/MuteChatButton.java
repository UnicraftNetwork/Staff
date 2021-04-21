package cl.bgmp.vanguard.staffmode.modules.hotbartools.hotbarbuttons.menubutton;

import cl.bgmp.butils.gui.GUIButton;
import cl.bgmp.butils.items.ItemBuilder;
import cl.bgmp.vanguard.staffmode.modules.MuteChatModule;
import com.google.inject.Inject;
import org.bukkit.Material;
import org.bukkit.entity.Player;

public class MuteChatButton extends GUIButton {
  private MuteChatModule mcm;

  @Inject
  public MuteChatButton(MuteChatModule mcm) {
    super(
        new ItemBuilder(Material.GREEN_WOOL)
            .setName("&c&lMute/UnMute Chat")
            .setLore("&7Click derecho para", "&7mutear/desmutear el chat.")
            .build(),
        12);

    this.mcm = mcm;
  }

  @Override
  public void clickBy(Player player) {
    this.mcm.muteChatBy(player);
  }
}
