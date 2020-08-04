package MessageHandling;

import net.dv8tion.jda.api.entities.Message;

//A simple abstract class for objects that handle bot queries to extend.
public abstract class QueryHandler {
    public abstract void handleQuery(Message message);
}
