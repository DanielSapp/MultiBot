package Roles;

import MessageHandling.QueryHandler;
import net.dv8tion.jda.api.entities.*;

public class RoleQueryHandler extends QueryHandler {

    /**
     * Entry point for role management commands. Analyzes message to determine the type of action needed, extracts
     * the necessary components from message to do it, and calls a method to complete the action.
     * @param message The user message that contained this query.
     */
    @Override
    public void handleQuery(Message message) {
        String messageText = message.getContentStripped();
        if (messageText.startsWith("!list")) {
            listRoles(message.getGuild(), message.getChannel());
        } else if (messageText.startsWith("!add")) {
            addRoleToUser(message.getAuthor(), messageText.substring(messageText.indexOf(" ")+1));
        } else if (messageText.startsWith("!remove")) {
            removeRoleFromUser(message.getAuthor(), messageText.substring(messageText.indexOf(" ")+1));
        } else if (messageText.startsWith("!create")) {
            createRole(message);
        }
    }

    /**
     * Sends a message in @param channel with all of the roles in @param guild.
     * @param guild The guild the message originated from.
     * @param channel The channel the message originated from.
     */
    private static void listRoles(Guild guild, MessageChannel channel) {
        StringBuilder sb = new StringBuilder();
        for (Role r : guild.getRoles()) {
            if (r.getName().equals("@everyone")) {
                continue;
            }
            sb.append(r.getName());
            sb.append("\n");
        }
        channel.sendMessage("Roles:\n" + sb.toString()).queue();
    }

    private static void addRoleToUser(User user, String roleName) {
        //TODO
    }

    private static void removeRoleFromUser(User user, String roleName) {
        //TODO
    }

    private static void createRole(Message message) {
        //TODO
    }
}
