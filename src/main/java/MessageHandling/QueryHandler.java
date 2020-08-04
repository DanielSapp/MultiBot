package MessageHandling;
import net.dv8tion.jda.api.entities.Message;
import java.io.Serializable;

//A simple abstract class for objects that handle bot queries to extend.
public abstract class QueryHandler implements Serializable {
    public abstract void handleQuery(Message message);
}