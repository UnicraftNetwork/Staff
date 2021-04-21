package cl.bgmp.vanguard.staffmode.modules;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FreezeModule extends StaffModeModule{

    private static Map<String, FreezeModule> freezes = new HashMap<>();

    public static FreezeModule getFreeze(Player p) {
        return freezes.get(p.getName());
    }

    public boolean hasFreeze(Player p) {
        if (freezes.containsKey(p.getName())) {
            if (!freezes.get(p.getName()).isCancelled()) {
                return true;
            } else {
                freezes.remove(p.getName());
                return false;
            }
        }
        return false;
    }

    public void removeFreeze(Player p) {
        if (freezes.containsKey(p.getName())) {
            if (hasFreeze(p)) {
                freezes.get(p.getName()).setCancelled(true);
                freezes.remove(p.getName());
            } else {
                freezes.remove(p.getName());
            }
        }
    }

    private final String name;
    private Player p;
    private boolean cancelled;
    private final Location freeze;

    public FreezeModule(Player p) {
        this.p = p;
        this.name = p.getName();
        this.cancelled = false;
        this.freeze = p.getLocation();
        freezes.put(p.getName(), this);
    }

    public FreezeModule(Player p, Location freeze) {
        this.p = p;
        this.name = p.getName();
        this.cancelled = false;
        this.freeze = freeze;
        freezes.put(p.getName(), this);
    }

    public void run() {
        Bukkit.getServer().getPluginManager().registerEvents(this, this.vanguard);
        for (Location loc : getWallBlocks(freeze)) {
            sendWallBlock(p, loc);
        }
    }

    public List<Location> getWallBlocks(final Location loc) {

        final Location l = loc.clone();

        List<Location> locations = new ArrayList<>();
        locations.add(l.clone().subtract(0, 1, 0));
        locations.add(l.clone().add(0, 2, 0));
        locations.add(l.clone().add(1, 1, 0));
        locations.add(l.clone().clone().add(0, 1, 1));
        locations.add(l.clone().add(-1, 1, 0));
        locations.add(l.clone().add(0, 1, -1));

        locations.add(l.clone().add(1, 0, 0));
        locations.add(l.clone().clone().add(0, 0, 1));
        locations.add(l.clone().add(-1, 0, 0));
        locations.add(l.clone().add(0, 0, -1));

        return locations;
    }

    public void sendWallBlock(Player p, Location loc) {
        p.sendBlockChange(loc, Material.RED_STAINED_GLASS.createBlockData());
    }

    public void removeWallBlock(Player p, Location loc) {
        p.sendBlockChange(loc, loc.getWorld().getBlockAt(loc).getType(), loc.getWorld().getBlockAt(loc).getData());
    }

    public Player getPlayer() {
        return p;
    }

    public boolean isCancelled() {
        return cancelled;
    }

    public Location getFreeze() {
        return freeze;
    }

    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
        if (cancelled) {
            if (Bukkit.getPlayer(name) != null) {
                Player p = Bukkit.getPlayer(name);
                for (Location loc : getWallBlocks(freeze)) {
                    assert p != null;
                    removeWallBlock(p, loc);
                }
            }
        }
    }

    @EventHandler
    public void onMove(PlayerMoveEvent e) {
        if (cancelled) {
            e.getHandlers().unregister(this);
            return;
        }
        if (Bukkit.getPlayer(name) != null) {
            Player p = Bukkit.getPlayer(name);
            assert p != null;
            if (e.getPlayer().getName().equals(p.getName())) {

                for (Location loc : getWallBlocks(freeze)) {
                    sendWallBlock(e.getPlayer(), loc);
                }

                if (e.getTo().getBlockX() == e.getFrom().getBlockX() && e.getTo().getBlockY() == e.getFrom().getBlockY() && e.getTo().getBlockZ() == e.getFrom().getBlockZ()) {
                    return;
                }
                e.setTo(freeze);

            }
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onJoin(PlayerJoinEvent e) {
        if (cancelled) {
            if (Bukkit.getPlayer(name) != null) {
                Player p = Bukkit.getPlayer(name);
                this.p = p;
                assert p != null;
                if (e.getPlayer().getName().equals(p.getName())) {
                    e.getPlayer().setAllowFlight(false);
                    for (Location loc : getWallBlocks(freeze)) {
                        removeWallBlock(e.getPlayer(), loc);
                    }
                    e.getHandlers().unregister(this);
                }
            }
        } else {
            if (Bukkit.getPlayer(name) != null) {
                Player p = Bukkit.getPlayer(name);
                assert p != null;
                if (e.getPlayer().getName().equals(p.getName())) {
                    e.getPlayer().teleport(freeze);
                    for (Location loc : getWallBlocks(freeze)) {
                        sendWallBlock(e.getPlayer(), loc);
                    }
                }
            }
        }
    }

    @EventHandler
    public void onDamage(EntityDamageEvent e) {
        if (cancelled) {
            e.getHandlers().unregister(this);
            return;
        }
        if (e.getEntity() instanceof Player) {
            Player p = (Player) e.getEntity();

            if (p.getName().equalsIgnoreCase(name)) {
                e.setDamage(0.0);
                e.setCancelled(true);
            }

        }
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
