package cl.bgmp.vanguard.staffmode.modules.hotbartools.hotbarbuttons.menubutton;

import cl.bgmp.butils.chat.Chat;
import cl.bgmp.butils.gui.GUI;
import cl.bgmp.butils.gui.GUIButton;
import cl.bgmp.vanguard.Vanguard;
import com.google.inject.Inject;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Nonnull;

public class MenuButtonGUI extends GUI {

  @Inject
  public MenuButtonGUI(Vanguard vanguard, MuteChatButton mcb, ClearChatButton ccb) {
    super(vanguard, Chat.color("&bMenu"), 27);

    this.buttons.add(mcb);
    this.buttons.add(ccb);
  }

  @Override
  @SuppressWarnings("unchecked")
  @Nonnull
  public List<GUIButton> setUpButtons() {
    return new ArrayList<>();
  }
}
