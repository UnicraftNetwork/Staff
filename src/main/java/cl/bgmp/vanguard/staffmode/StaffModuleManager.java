package cl.bgmp.vanguard.staffmode;

import cl.bgmp.vanguard.module.ModuleManager;
import cl.bgmp.vanguard.staffmode.modules.StaffModeModule;
import com.google.inject.Singleton;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

@Singleton
public class StaffModuleManager implements ModuleManager<StaffModeModule> {
  private Set<StaffModeModule> modules = new HashSet<>();

  @Override
  public void registerModule(StaffModeModule module) {
    this.modules.add(module);
  }

  @Override
  public void loadModules() {
    for (StaffModeModule module : this.modules) {
      if (!module.isEnabled()) continue;

      module.configure();
      module.load();
    }
  }

  @Override
  public Collection<StaffModeModule> getModules() {
    return this.modules;
  }

  @Override
  @SuppressWarnings("unchecked")
  public <N extends StaffModeModule> N getModule(Class<? extends N> key) {
    final StaffModeModule module =
        this.modules.stream()
            .filter(m -> m.getClass().getSimpleName().equals(key.getSimpleName()))
            .findFirst()
            .orElse(null);
    return (N) module;
  }
}
