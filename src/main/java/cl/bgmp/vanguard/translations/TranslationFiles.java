package cl.bgmp.vanguard.translations;

import cl.bgmp.butils.files.PropertiesUtils;
import com.google.common.collect.ImmutableMap;
import java.util.Map;
import java.util.Properties;

public interface TranslationFiles {
  Class<TranslationFiles> thisClass = TranslationFiles.class;
  String ROOT = "i18n/";
  String TEMPLATE = ROOT + "template/strings.properties";
  String TRANSLATIONS = ROOT + "translations/";

  String ES_CL = TRANSLATIONS + "es_cl.properties";

  Properties template = PropertiesUtils.newPropertiesResource(thisClass, TEMPLATE);
  Map<String, Properties> translations =
      ImmutableMap.<String, Properties>builder()
          .put("es_cl", PropertiesUtils.newPropertiesResource(thisClass, ES_CL))
          .build();
}
