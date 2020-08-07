package MessageDistribution;

import MessageHandlers.MessageHandler;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;
import java.util.ArrayList;

//This class serves as the distributor for queries.  Its main function is to take every message that could potentially
//be a bot command and distribute it to the appropriate QueryHandler(s) if it is one.
public class MessageDistributor extends ListenerAdapter {
    private ArrayList<MessageHandler> handlers;

    /**
     * @param handlers All MessageHandlers.QueryHandlers that should receive commands.
     */
    public MessageDistributor(ArrayList<MessageHandler> handlers) {
        this.handlers = handlers;
    }

    /**
     * Receive a GuildMessageReceivedEvent every time a message is sent in a guild this bot is in.
     * Call handleMessage() on every EventHandler that is running.  Each one polymorphically handles the message
     * (or does nothing if applicable).
     */
    @Override
    public void onGuildMessageReceived(@NotNull GuildMessageReceivedEvent event) {
        Message eventMessage = event.getMessage();
        for (MessageHandler h : handlers) {
            h.handleMessage(eventMessage);
        }
    }
}