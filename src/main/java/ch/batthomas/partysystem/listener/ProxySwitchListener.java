package ch.batthomas.partysystem.listener;

import ch.batthomas.partysystem.PartySystem;
import ch.batthomas.partysystem.util.Party;
import java.util.concurrent.TimeUnit;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.ServerSwitchEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

/**
 *
 * @author batthomas
 */
public class ProxySwitchListener implements Listener {

    private final PartySystem plugin;

    public ProxySwitchListener(PartySystem plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onSwitch(ServerSwitchEvent e) {
        System.err.println("Bae");
        ProxiedPlayer player = e.getPlayer();
        Party party = plugin.getPartyManager().getParty(player);
        if (party != null) {
            if (party.getLeader().equals(player)) {
                plugin.getProxy().getScheduler().schedule(plugin, () -> {
                    party.broadcastMessage("Die Party betritt einen neuen Server");
                    for (ProxiedPlayer other : party.getPlayers()) {
                        other.connect(player.getServer().getInfo());
                    }
                }, 1, TimeUnit.SECONDS);
            }
        }
    }
}
