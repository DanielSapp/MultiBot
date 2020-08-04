package Roles;

import MessageHandling.QueryHandler;
import net.dv8tion.jda.api.entities.Message;

public class AddRoleQueryHandler extends QueryHandler {
    @Override
    public void handleQuery(Message message) {
        //TODO
    }

    @Override
    public boolean canHandle(Message message) {
        String messageText = message.getContentStripped().toLowerCase();
        return (messageText.startsWith("!addrole") || messageText.startsWith("!addrank"));
    }
}
