package cl.bgmp.vanguard.injection;

import cl.bgmp.butils.translations.Translations;
import cl.bgmp.vanguard.Vanguard;
import cl.bgmp.vanguard.translations.AllTranslations;
import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;

public class StaffModule extends AbstractModule {
  private final Vanguard vanguard;
  private final AllTranslations translations;

  public StaffModule(Vanguard vanguard, AllTranslations translations) {
    this.vanguard = vanguard;
    this.translations = translations;
  }

  public Injector createInjector() {
    return Guice.createInjector(this);
  }

  @Override
  protected void configure() {
    this.bind(Vanguard.class).toInstance(this.vanguard);
    this.bind(Translations.class).toInstance(this.translations);
  }
}
