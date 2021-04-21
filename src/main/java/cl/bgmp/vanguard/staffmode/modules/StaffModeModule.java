package cl.bgmp.vanguard.staffmode.modules;

import cl.bgmp.vanguard.Vanguard;
import cl.bgmp.vanguard.module.Module;
import cl.bgmp.vanguard.staffmode.StaffMode;
import com.google.inject.Inject;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.plugin.PluginManager;

public abstract class StaffModeModule implements Module, Listener {
  @Inject protected Vanguard vanguard;
  @Inject protected StaffMode staffMode;

  public StaffModeModule() {
    this.configure();
  }

  @Override
  public abstract boolean isEnabled();

  @Override
  public void configure() {}

  @Override
  public void load() {
    PluginManager pm = this.vanguard.getServer().getPluginManager();
    pm.registerEvents(this, this.vanguard);
  }

  @Override
  public void unload() {
    HandlerList.unregisterAll(this);
  }
}
