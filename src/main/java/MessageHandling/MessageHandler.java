package MessageHandling;

import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;
import java.util.HashMap;

//This class serves as the distributor for queries.  Its main function is to take every message that could potentially
//be a bot command and distribute it to the appropriate QueryHandlers if it is one.
public class MessageHandler extends ListenerAdapter {
    private HashMap<Query, QueryHandler[]> handlerMap;

    /**
     * @param handlerMap A Map that maps a Query enum representing a bot command type to all of the QueryHandlers that should receive commands of that category.
     */
    public MessageHandler(HashMap<Query, QueryHandler[]> handlerMap) {
        this.handlerMap = handlerMap;
    }

    /**
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
     * @param message The text of the potential bot-commanding message being parsed
     * @return a Query enum representing the type of query that was ordered, or null if it is an invalid command
     */
    private Query getQueryType(String message) {
        if (message.startsWith("!wolfram")) {
            return Query.WOLFRAM;
        } else if (message.startsWith("!addrole") || message.startsWith("!addrank") || message.startsWith("!removerole") || message.startsWith("!removerank") || message.startsWith("!listroles") || message.startsWith("!listranks")) {
            return Query.ROLE;
        } else if (message.startsWith("!broadcast")) {
            return Query.BROADCAST;
        } else {
            return null;
        }
    }
}
