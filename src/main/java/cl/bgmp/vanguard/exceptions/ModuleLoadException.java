package cl.bgmp.vanguard.exceptions;

import cl.bgmp.vanguard.module.Module;
import javax.annotation.Nullable;

/** Thrown when a module fails to load. */
public class ModuleLoadException extends RuntimeException {

  private final @Nullable Class<? extends Module> key;

  public ModuleLoadException(Class<? extends Module> key, String message, Throwable cause) {
    super(message, cause);
    this.key = key;
  }

  public ModuleLoadException(Class<? extends Module> key, String message) {
    this(key, message, null);
  }

  public @Nullable Class<? extends Module> getModule() {
    return key;
  }

  public ModuleLoadException(Class<? extends Module> key, Throwable cause) {
    this(key, cause.getMessage(), cause);
  }

  public ModuleLoadException(String message, Throwable cause) {
    this(null, message, cause);
  }

  public ModuleLoadException(Throwable cause) {
    this(cause.getMessage(), cause);
  }

  public ModuleLoadException(String message) {
    this(message, null);
  }
}
