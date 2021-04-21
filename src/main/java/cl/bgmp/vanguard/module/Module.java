package cl.bgmp.vanguard.module;

/** Represents a module, and is to be implemented by other modules specific to some logic. */
public interface Module {

  /**
   * Each {@link Module} can decide whether it's enabled or not based on the logic provided by child
   * classes, which would override this method.
   *
   * @return Whether the module is enabled or not.
   */
  boolean isEnabled();

  /**
   * Everything to this module which depends on configuration. Perhaps the module needs some value
   * to be retrieved from a config file to work, or a message, etc.
   */
  void configure();

  /** To be overridden by child classes. Every module has its unique way of being loaded. */
  void load();

  /** To be overridden by child classes. Every module has its unique way of being unloaded. */
  void unload();

  /** Unload & load back. */
  default void reload() {
    this.unload();
    this.load();
  }
}
