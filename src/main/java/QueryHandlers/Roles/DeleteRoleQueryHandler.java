package QueryHandlers.Roles;

import QueryHandlers.NoGatewayIntentQueryHandler;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.Role;
import java.util.List;

public class DeleteRoleQueryHandler extends NoGatewayIntentQueryHandler {
    /**
     * Return whether @param message is a query to delete a Role.
     * @param message The message in question.
     * @return Whether the message is a query to delete a Role.
     */
    @Override
    public boolean canHandle(Message message) {
        String messageText = message.getContentStripped().toLowerCase();
        return messageText.startsWith("!deleterole") || messageText.startsWith("!deleterank");
    }

    /**
     * Receive a query delete a Role.  Delete the Role if the query sender has the modify roles permission
     * and a role by the queried one is found, else print error info to console and in the Discord
     * channel the message originated from.
     * @param message The query to delete a Role.
     */
    @Override
    public void handleQuery(Message message) {
        String messageText = message.getContentStripped();
        String roleName = messageText.substring(messageText.indexOf(" ")+1);
        Member sender = message.getMember();
        MessageChannel channel = message.getChannel();
        if (sender.getPermissions().contains(Permission.MANAGE_ROLES)) {
            List<Role> matchingRoles = message.getGuild().getRolesByName(roleName, true);
            if (matchingRoles.size() == 1) {
                matchingRoles.get(0).delete().queue();
                System.out.println("Role " + roleName + " has been deleted by " + sender.getEffectiveName() + " in " + sender.getGuild().getName());
                channel.sendMessage("Role successfully deleted!").queue();
            } else if (matchingRoles.size() == 0) {
                RoleErrorPrinter.sendNoRolesFoundError(sender, channel, roleName, "delete");
            } else {
                RoleErrorPrinter.sendMultipleRolesFoundError(sender, channel, roleName, matchingRoles, "delete");
            }
        } else {
            RoleErrorPrinter.sendPermissionsError(sender, channel, "delete");
        }
    }
}
