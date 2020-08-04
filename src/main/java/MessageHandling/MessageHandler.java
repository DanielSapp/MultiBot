package MessageHandling;

import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;

//This class serves as the distributor for queries.  Its main function is to take every message that could potentially
//be a bot command and distribute it to the appropriate QueryHandler(s) if it is one.
public class MessageHandler extends ListenerAdapter {
    private HashMap<Query, ArrayList<QueryHandler>> handlerMap;

    /**
     * @param handlerMap A Map that maps a Query enum representing a bot command category to all of the QueryHandlers that should receive those command types.
     */
    public MessageHandler(HashMap<Query, ArrayList<QueryHandler>> handlerMap) {
        this.handlerMap = handlerMap;
    }

    /**
     * Receives a GuildMessageReceivedEvent every time a message is sent in a guild this bot is in.
     * If it is a command, it passes it to the appropriate QueryHandlers as defined in handlerMap.
     * @param event Any message received in a guild that this bot is in.
     */
    @Override
    public void onGuildMessageReceived(@NotNull GuildMessageReceivedEvent event) {
        String message = event.getMessage().getContentStripped();
        if (message.startsWith("!")) {
            Query q = getQueryType(message);
            if (q != null) {
                for (QueryHandler qh : handlerMap.get(q)) {
                    qh.handleQuery(event.getMessage());
                }
            }
        }
    }

    /**
     * Take the text of a message that may be a bot command.  Return a query enum representing the type of command, or
     * null if it was not a command.
     * @param message The text of the potential bot-commanding message being parsed
     * @return a Query enum representing the type of query that was ordered, or null if it is an invalid command
     */
    private Query getQueryType(String message) {
        message = message.toLowerCase();
        if (message.startsWith("!wolfram")) {
            return Query.WOLFRAM;
        } else if (message.startsWith("!addrole") || message.startsWith("!addrank") || message.startsWith("!removerole") || message.startsWith("!removerank") || message.startsWith("!listroles") || message.startsWith("!listranks")) {
            return Query.ROLE;
        } else if (message.startsWith("!broadcast")) {
            return Query.BROADCAST;
        } else if (message.startsWith("!stock")) {
            return Query.STOCKS;
        } else {
            return null;
        }
    }
}