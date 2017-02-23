package ch.batthomas.partysystem.util;

import ch.batthomas.partysystem.PartySystem;
import java.util.concurrent.TimeUnit;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.scheduler.ScheduledTask;

/**
 *
 * @author batthomas
 */
public class Invitation {

    private final ProxiedPlayer player;
    private final Party party;
    private final PartySystem plugin;
    private boolean valid;

    private ScheduledTask task;

    public Invitation(PartySystem plugin, ProxiedPlayer player, Party party, int minutes) {
        this.player = player;
        this.party = party;
        this.plugin = plugin;
        valid = true;
        startInvitation(minutes);
    }

    private void acceptInvitation() {
        if (valid) {
            task.cancel();
            party.removeInvitation(this);
            party.acceptInvitation(this);
            player.sendMessage(new TextComponent("Du hast die Party Anfrage angenommen"));
        }
    }

    private void denyInvitation() {
        if (valid) {
            task.cancel();
            party.removeInvitation(this);
            player.sendMessage(new TextComponent("Du hast die Party Anfrage abgelehnt"));
        }
    }

    private void startInvitation(int minutes) {
        player.sendMessage(new TextComponent(plugin.getPrefix() + "Du wurdest von " + party.getLeader().getName() + " in eine Party eingeladen"));

        TextComponent accept = new TextComponent("§l§a[Annehmen]");
        accept.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/party accept " + party.getLeader().getName()));
        accept.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("Nehme die Anfrage an").create()));

        TextComponent deny = new TextComponent("§l§4[Ablehnen]");
        deny.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/party deny " + party.getLeader().getName()));
        deny.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("Lehne die Anfrage ab").create()));

        TextComponent full = new TextComponent(plugin.getPrefix());
        full.addExtra(accept);
        full.addExtra(new TextComponent(" §r§7oder "));
        full.addExtra(deny);
        player.sendMessage(full);
        task = plugin.getProxy().getScheduler().schedule(plugin, () -> {
            party.removeInvitation(this);
            valid = false;
        }, minutes, TimeUnit.MINUTES);
    }

    public ProxiedPlayer getPlayer() {
        return player;
    }
}
