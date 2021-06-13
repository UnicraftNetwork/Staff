package cl.bgmp.staff;

public interface Permissions {
  String ROOT = "staff";

  String STAFF_MODE_USE = ROOT + ".staffmode.use";
  String VANISH_MODE_SEE = ROOT + ".vanish.see";
  String MUTE_CHAT_BYPASS = ROOT + ".mutechat.bypass";
  String CLEAR_CHAT = ROOT + ".clearchat";
  String INVSEE = ROOT + ".invsee";
  String MUTE_CHAT = ROOT + ".mutechat";
  String FREEZE = ROOT + ".freeze";
}
