package ch.batthomas.partysystem.command;

import ch.batthomas.partysystem.PartySystem;
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
        if (args.length == 1) { //friend list / leave

        } else if (args.length == 2) { //friend invite / remove / accept / deny / kick
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
                }
            } else {
                player.sendMessage(new TextComponent(plugin.getPrefix() + "Der Spieler wurde nicht gefunden"));
                return;
            }
        }
        player.sendMessage(new TextComponent(plugin.getPrefix() + "- Party Verwaltung -"));
    }

}
