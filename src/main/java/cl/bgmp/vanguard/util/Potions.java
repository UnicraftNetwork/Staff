package cl.bgmp.vanguard.util;

import com.google.common.collect.ImmutableMap;
import java.util.Map;
import org.bukkit.potion.PotionEffectType;

public interface Potions {

  static String potionEffectTypeName(final PotionEffectType type) {
    String name = POTION_EFFECT_MAP.get(type);
    if (name != null) {
      return name;
    } else {
      return "Unknown";
    }
  }

  Map<PotionEffectType, String> POTION_EFFECT_MAP =
      ImmutableMap.<PotionEffectType, String>builder()
          .put(PotionEffectType.BLINDNESS, "Blindness")
          .put(PotionEffectType.CONFUSION, "Nausea")
          .put(PotionEffectType.DAMAGE_RESISTANCE, "Resistance")
          .put(PotionEffectType.FAST_DIGGING, "Haste")
          .put(PotionEffectType.FIRE_RESISTANCE, "Fire Resistance")
          .put(PotionEffectType.HARM, "Instant Damage")
          .put(PotionEffectType.HEAL, "Instant Health")
          .put(PotionEffectType.HUNGER, "Hunger")
          .put(PotionEffectType.INCREASE_DAMAGE, "Strength")
          .put(PotionEffectType.INVISIBILITY, "Invisibility")
          .put(PotionEffectType.JUMP, "Jump Boost")
          .put(PotionEffectType.NIGHT_VISION, "Night Vision")
          .put(PotionEffectType.POISON, "Poison")
          .put(PotionEffectType.REGENERATION, "Regeneration")
          .put(PotionEffectType.SLOW, "Slowness")
          .put(PotionEffectType.SLOW_DIGGING, "Mining Fatigue")
          .put(PotionEffectType.SPEED, "Speed")
          .put(PotionEffectType.WATER_BREATHING, "Water Breating")
          .put(PotionEffectType.WEAKNESS, "Weakness")
          .put(PotionEffectType.WITHER, "Wither")
          .put(PotionEffectType.HEALTH_BOOST, "Health Boost")
          .put(PotionEffectType.ABSORPTION, "Absorption")
          .put(PotionEffectType.SATURATION, "Saturation")
          .put(PotionEffectType.GLOWING, "Glowing")
          .put(PotionEffectType.LUCK, "Luck")
          .put(PotionEffectType.UNLUCK, "Bad Luck")
          .put(PotionEffectType.BAD_OMEN, "Bad Omen")
          .put(PotionEffectType.CONDUIT_POWER, "Conduit Power")
          .put(PotionEffectType.DOLPHINS_GRACE, "Dolphin's Grace")
          .put(PotionEffectType.HERO_OF_THE_VILLAGE, "Hero of The Village")
          .build();
}
