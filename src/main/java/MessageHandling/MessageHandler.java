package MessageHandling;

import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;
import java.util.ArrayList;

//This class serves as the distributor for queries.  Its main function is to take every message that could potentially
//be a bot command and distribute it to the appropriate QueryHandler(s) if it is one.
public class MessageHandler extends ListenerAdapter {
    private ArrayList<QueryHandler> handlers;

    /**
     * @param handlers All QueryHandlers that should receive commands.
     */
    public MessageHandler(ArrayList<QueryHandler> handlers) {
        this.handlers = handlers;
    }

    /**
     * Receives a GuildMessageReceivedEvent every time a message is sent in a guild this bot is in.
     * If it is a command, it calls canHandle() on every QueryHandler that is running and calls handleQuery()
     * on every one that returns true, indicating that the command is intended for that QueryHandler.
     */
    @Override
    public void onGuildMessageReceived(@NotNull GuildMessageReceivedEvent event) {
        Message eventMessage = event.getMessage();
        if (eventMessage.getContentStripped().startsWith("!")) {
            for (QueryHandler qh : handlers) {
                if (qh.canHandle(eventMessage)) {
                    qh.handleQuery(eventMessage);
                }
            }
        }
    }
}