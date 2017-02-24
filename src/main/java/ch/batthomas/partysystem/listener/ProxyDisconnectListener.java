package ch.batthomas.partysystem.listener;

import ch.batthomas.partysystem.PartySystem;
import ch.batthomas.partysystem.util.Party;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.PlayerDisconnectEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

/**
 *
 * @author batthomas
 */

public class ProxyDisconnectListener implements Listener {
    
    private final PartySystem plugin;
    
    public ProxyDisconnectListener(PartySystem plugin){
        this.plugin = plugin;
    }
    
    @EventHandler
    public void onDisconnect(PlayerDisconnectEvent e){
        ProxiedPlayer player = e.getPlayer();
        Party party = plugin.getPartyManager().getParty(player);
        if(party != null){
            if(party.getLeader().equals(player)){
                party.removeLeader();
            }else{
                party.removePlayer(player);
            }
        }
    }
}
