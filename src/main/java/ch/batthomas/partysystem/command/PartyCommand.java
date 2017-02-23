package ch.batthomas.partysystem.command;

import ch.batthomas.partysystem.PartySystem;
import ch.batthomas.partysystem.util.Party;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

/**
 *
 * @author batthomas
 */
public class PartyCommand extends Command {

    private final PartySystem plugin;

    public PartyCommand(PartySystem plugin) {
        super("party");
        this.plugin = plugin;
    }

    @Override
    public void execute(CommandSender cs, String[] args) {
        if (!(cs instanceof ProxiedPlayer)) {
            cs.sendMessage(new TextComponent("Nur Spieler koennen diesen Command ausführen!"));
            return;
        }
        ProxiedPlayer player = (ProxiedPlayer) cs;
        if (args.length == 1) {
            if (args[0].equalsIgnoreCase("list")) {
                Party party = plugin.getPartyManager().getParty(player);
                if (party != null) {
                    player.sendMessage(new TextComponent(plugin.getPrefix() + "- Party Informationen -"));
                    player.sendMessage(new TextComponent(plugin.getPrefix() + "Leader: " + party.getLeader().getName()));
                    player.sendMessage(new TextComponent(plugin.getPrefix() + "Spieler: "));
                    for (ProxiedPlayer others : party.getPlayers()) {
                        player.sendMessage(new TextComponent(plugin.getPrefix() + "- " + others.getName()));
                    }
                } else {
                    player.sendMessage(new TextComponent(plugin.getPrefix() + "Du bist in keiner Party"));
                }
                return;
            } else if (args[0].equalsIgnoreCase("leave")) {
                Party party = plugin.getPartyManager().getParty(player);
                if (party != null) {
                    party.removePlayer(player);
                } else {
                    player.sendMessage(new TextComponent(plugin.getPrefix() + "Du bist in keiner Party"));
                }
                return;
            }
        } else if (args.length == 2) {
            ProxiedPlayer other = plugin.getProxy().getPlayer(args[1]);
            if (other != null) {
                if (args[0].equalsIgnoreCase("invite")) {
                    plugin.getPartyManager().invitePlayer(player, other);
                    return;
                } else if (args[0].equalsIgnoreCase("accept")) {
                    plugin.getPartyManager().acceptInvitation(player, other);
                    return;
                } else if (args[0].equalsIgnoreCase("deny")) {
                    plugin.getPartyManager().denyInvitation(player, other);
                    return;
                } else if (args[0].equalsIgnoreCase("kick")) {
                    plugin.getPartyManager().kickPlayer(player, other);
                    return;
                }
            } else {
                player.sendMessage(new TextComponent(plugin.getPrefix() + "Der Spieler wurde nicht gefunden"));
                return;
            }
        }
        player.sendMessage(new TextComponent(plugin.getPrefix() + "- Party Verwaltung -"));
        player.sendMessage(new TextComponent(plugin.getPrefix() + "/friend list"));
        player.sendMessage(new TextComponent(plugin.getPrefix() + "/friend invite <Player>"));
        player.sendMessage(new TextComponent(plugin.getPrefix() + "/friend accept <Player>"));
        player.sendMessage(new TextComponent(plugin.getPrefix() + "/friend deny <Player>"));
        player.sendMessage(new TextComponent(plugin.getPrefix() + "/friend kick <Player>"));
    }
}
