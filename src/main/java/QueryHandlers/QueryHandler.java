package QueryHandlers;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.requests.GatewayIntent;

import java.io.Serializable;

//A simple abstract class for all objects that handle bot queries to extend.
public abstract class QueryHandler implements Serializable {
    public abstract GatewayIntent[] getGatewayIntents();
    public abstract void handleQuery(Message message);
    public abstract boolean canHandle(Message message);
}