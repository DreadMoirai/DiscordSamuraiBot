package samurai.events.listeners;

import samurai.events.GuildMessageEvent;

/**
 * Listens to all messages sent within a channel
 * @author TonTL
 * @version 4.x - 3/10/2017
 * @see GuildMessageEvent
 */
public interface MessageListener extends SamuraiListener {

    void onGuildMessageEvent(GuildMessageEvent event);

}
