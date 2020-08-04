package Roles;

import MessageHandling.QueryHandler;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.Role;

public class ListRolesQueryHandler extends QueryHandler {

    /**
     * Returns whether @param message is a query to list roles.
     * @param message The message in question.
     * @return Whether the message is a query to list roles.
     */
    @Override
    public boolean canHandle(Message message) {
        String messageText = message.getContentStripped().toLowerCase();
        return (messageText.startsWith("!listrole") || messageText.startsWith("!listrank"));
    }

    /**
     * Sends a message in the channel of the originating message with all of the roles in the originating guild.
     * @param message The message that initiated this query.
     */
    @Override
    public void handleQuery(Message message) {
        StringBuilder sb = new StringBuilder();
        for (Role r : message.getGuild().getRoles()) {
            if (r.getName().equals("@everyone")) {
                continue;
            }
            sb.append(r.getName());
            sb.append("\n");
        }
        message.getChannel().sendMessage("Roles:\n" + sb.toString()).queue();
    }


}
