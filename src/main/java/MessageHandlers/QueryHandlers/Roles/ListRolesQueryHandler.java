package MessageHandlers.QueryHandlers.Roles;

import MessageHandlers.QueryHandlers.QueryHandler;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.Role;

public class ListRolesQueryHandler extends QueryHandler {

    /**
     * Return whether @param message is a query to list Roles.
     * @param message The message in question.
     * @return Whether the message is a query to list Roles.
     */
    @Override
    public boolean canHandle(Message message) {
        String messageText = message.getContentStripped().toLowerCase();
        return (messageText.startsWith("!listrole") || messageText.startsWith("!listrank"));
    }

    /**
     * Send a message in the channel of the originating message with all of the Roles in the originating guild.
     * @param message The message that initiated this query.  Called from superclass method handleMessage()
     * after it determines that @param message is a valid query.
     */
    @Override
    public void executeQuery(Message message) {
        StringBuilder sb = new StringBuilder();
        for (Role r : message.getGuild().getRoles()) {
            if (r.getName().equals("@everyone") || r.getName().equals("MultiBot")) {
                continue;
            }
            sb.append(r.getName());
            sb.append("\n");
        }
        message.getChannel().sendMessage("Roles:\n" + sb.toString()).queue();
    }
}
