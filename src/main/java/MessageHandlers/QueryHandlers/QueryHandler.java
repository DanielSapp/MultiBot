package MessageHandlers.QueryHandlers;
import MessageHandlers.MessageHandler;
import net.dv8tion.jda.api.entities.Message;

//A subclass of MessageHandler for objects that only take action on messages specifically requesting them
//to execute a query, as opposed to taking action on every message.
public abstract class QueryHandler extends MessageHandler {
    /**
     *
     * @param message A message that may be a query for this object.
     * @return Whether the message is a query for this object.
     */
    public abstract boolean canHandle(Message message);

    /**
     * Called when a Message is confirmed to be a query for this object.
     * @param message A query for this object to handle.
     */
    public abstract void executeQuery(Message message);

    /**
     * Overridden method from MessageHandler.  Calls executeQuery() IFF @param message is a valid query for this object,
     * else returns.
     * @param message A message that may be a query for this object.
     */
    @Override
    public void handleMessage(Message message) {
        if (canHandle(message)) {
            executeQuery(message);
        }
    }
}