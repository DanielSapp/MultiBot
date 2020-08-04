package QueryHandlers.Roles;

import QueryHandlers.QueryHandler;
import net.dv8tion.jda.api.entities.Message;

public class RemoveRoleQueryHandler extends QueryHandler {

    @Override
    public boolean canHandle(Message message) {
        String messageText = message.getContentStripped().toLowerCase();
        return (messageText.startsWith("!removerole") || messageText.startsWith("!removerank"));
    }

    @Override
    public void handleQuery(Message message) {
        //TODO
    }
}
