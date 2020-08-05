package QueryHandlers.Roles;

import QueryHandlers.QueryHandler;
import net.dv8tion.jda.api.entities.*;

public class RemoveRoleQueryHandler extends QueryHandler {

    /**
     * Returns whether @param message is a query to remove a Role from user.
     * @param message The message in question.
     * @return Whether the message is a query to remove a Role from user.
     */
    @Override
    public boolean canHandle(Message message) {
        String messageText = message.getContentStripped().toLowerCase();
        return (messageText.startsWith("!removerole") || messageText.startsWith("!removerank"));
    }

    @Override
    public void handleQuery(Message message) {

    }
}
