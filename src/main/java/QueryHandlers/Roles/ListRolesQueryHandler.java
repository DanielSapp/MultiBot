package QueryHandlers.Roles;

import QueryHandlers.NoGatewayIntentQueryHandler;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.Role;

public class ListRolesQueryHandler extends NoGatewayIntentQueryHandler {

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
     * @param message The message that initiated this query.
     */
    @Override
    public void handleQuery(Message message) {
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
