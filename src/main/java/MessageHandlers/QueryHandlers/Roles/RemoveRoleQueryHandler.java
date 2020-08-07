package MessageHandlers.QueryHandlers.Roles;

import MessageHandlers.QueryHandlers.QueryHandler;
import net.dv8tion.jda.api.entities.*;
import java.util.List;

public class RemoveRoleQueryHandler extends QueryHandler {

    /**
     * Return whether @param message is a query to remove a Role from user.
     * @param message The message in question.
     * @return Whether the message is a query to remove a Role from user.
     */
    @Override
    public boolean canHandle(Message message) {
        String messageText = message.getContentStripped().toLowerCase();
        return (messageText.startsWith("!removerole") || messageText.startsWith("!removerank"));
    }

    /**
     * Receive a query to remove a Role from a Member.  Remove it if possible, otherwise prints error info to
     * console and in the Discord channel the message originated from.  Called from superclass method handleMessage()
     * after it determines that @param message is a valid query.
     * @param message The query to remove a Role from the user.
     */
    @Override
    public void executeQuery(Message message) {
        Member sender = message.getMember();
        String messageText = message.getContentStripped();
        MessageChannel channel = message.getChannel();
        String roleName = messageText.substring(messageText.indexOf(" ")+1);

        //Get all roles that match the Member's query
        List<Role> matchingRoles = message.getGuild().getRolesByName(roleName, true);

        //If one role matches the query, see if the Member has it.  Remove it and print success messages if they do,
        //otherwise print errors.
        if (matchingRoles.size() == 1) {
            if (sender.getRoles().contains(matchingRoles.get(0))) {
                message.getGuild().removeRoleFromMember(sender, matchingRoles.get(0)).queue();
                System.out.println("Role " + roleName + " has been removed from " + sender.getEffectiveName() + " in " + message.getGuild().getName());
                channel.sendMessage(matchingRoles.get(0).getName() + " has been removed from " + sender.getEffectiveName()).queue();
            } else {
                RoleErrorPrinter.sendUserDoesntHaveRoleError(sender, channel, matchingRoles.get(0).getName());
            }
        //Else if no matching roles were found or more than one matching Role was found, print appropriate error messages
        } else if (matchingRoles.size() == 0) {
            RoleErrorPrinter.sendNoRolesFoundError(sender, channel, roleName, "remove");
        } else {
            RoleErrorPrinter.sendMultipleRolesFoundError(sender, channel, roleName, matchingRoles, "remove");
        }
    }
}
