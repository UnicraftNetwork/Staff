package cl.bgmp.vanguard.module;

import cl.bgmp.vanguard.exceptions.ModuleLoadException;
import java.util.Collection;
import javax.annotation.Nullable;

/** Class in charge of managing all sort of modules. */
public interface ModuleManager<M extends Module> {

  /**
   * Register a module which can be loaded later on.
   *
   * @param module The module to be registered
   */
  void registerModule(M module);

  /** Load every registered module. */
  void loadModules();

  /** Reload every single registered module. */
  default void reloadModules() {
    this.getModules().forEach(Module::reload);
  }

  /**
   * Get a sorted collection of {@link Module}s.
   *
   * @return A collection of {@link Module}s.
   */
  Collection<M> getModules();

  /**
   * Get a specific {@link Module} from its class.
   *
   * @param key A specific {@link Module} class.
   * @return A {@link Module} or {@code null} if not found.
   */
  @Nullable
  <N extends M> N getModule(Class<? extends N> key);

  /**
   * Require a specific {@link Module} from its class.
   *
   * @param key A specific {@link Module} class.
   * @return A {@link Module}.
   * @throws ModuleLoadException If not found.
   */
  default <N extends M> N needModule(Class<? extends N> key) {
    final N module = this.getModule(key);
    if (module == null) {
      throw new ModuleLoadException(key, "was required but not found.");
    }
    return module;
  }
}
