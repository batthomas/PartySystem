package ch.batthomas.partysystem.manager;

import ch.batthomas.partysystem.PartySystem;
import ch.batthomas.partysystem.util.Invitation;
import ch.batthomas.partysystem.util.Party;
import java.util.ArrayList;
import java.util.List;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;

/**
 *
 * @author batthomas
 */
public class PartyManager {

    private final PartySystem plugin;
    private final List<Party> parties;

    public PartyManager(PartySystem plugin) {
        this.plugin = plugin;
        parties = new ArrayList<>();
    }

    public void createParty(ProxiedPlayer player) {
        parties.add(new Party(plugin, player));
    }

    public void invitePlayer(ProxiedPlayer sender, ProxiedPlayer player) {
        if (getParty(sender) == null) {
            createParty(sender);
            sender.sendMessage(new TextComponent(plugin.getPrefix() + "Du hast eine neue Party erstellt"));
        }
        Party party = getParty(sender);
        if (party.getLeader().equals(sender)) {
            if (!party.getPlayers().contains(player)) {
                party.invitePlayer(player);
                sender.sendMessage(new TextComponent(plugin.getPrefix() + "Du hast " + player.getName() + " erfolgreich eingeladen"));
            } else {
                sender.sendMessage(new TextComponent(plugin.getPrefix() + "Dieser Spieler ist schon in deiner Party"));
            }
        } else {
            sender.sendMessage(new TextComponent(plugin.getPrefix() + "Du bist nicht der Leader der Party"));
        }
    }

    public void acceptInvitation(ProxiedPlayer player, ProxiedPlayer leader) {
        for (Party party : parties) {
            if (party.getLeader().equals(leader)) {
                Invitation invitation = party.getInvitation(player);
                if (invitation != null) {
                    party.removeInvitation(invitation);
                    party.acceptInvitation(invitation);
                    player.sendMessage(new TextComponent(plugin.getPrefix() + "Du hast diese Anfrage angenommen"));
                    return;
                }
            }
        }
        player.sendMessage(new TextComponent(plugin.getPrefix() + "Du besitzt keine Anfrage von diesem Spieler"));
    }

    public void denyInvitation(ProxiedPlayer player, ProxiedPlayer leader) {
        for (Party party : parties) {
            if (party.getLeader().equals(leader)) {
                Invitation invitation = party.getInvitation(player);
                if (invitation != null) {
                    party.removeInvitation(invitation);
                    player.sendMessage(new TextComponent(plugin.getPrefix() + "Du hast diese Anfrage abgelehnt"));
                    return;
                }
            }
        }
        player.sendMessage(new TextComponent(plugin.getPrefix() + "Du besitzt keine Anfrage von diesem Spieler"));
    }

    public void kickPlayer(ProxiedPlayer sender, ProxiedPlayer player) {
        Party party = getParty(sender);
        if (party != null) {
            if (party.getLeader().equals(sender)) {
                if (party.getPlayers().contains(player)) {
                    party.kickPlayer(player);
                } else {
                    sender.sendMessage(new TextComponent(plugin.getPrefix() + "Dieser Spieler ist nicht in der Party"));
                }
            } else {
                sender.sendMessage(new TextComponent(plugin.getPrefix() + "Du bist nicht der Leader der Party"));
            }
        } else {
            sender.sendMessage(new TextComponent(plugin.getPrefix() + "Du bist in keiner Party"));
        }
    }

    public Party getParty(ProxiedPlayer player) {
        for (Party party : parties) {
            if (party.getPlayers().contains(player) || party.getLeader().equals(player)) {
                return party;
            }
        }
        return null;
    }

    public List<Party> getParties() {
        return parties;
    }
}
