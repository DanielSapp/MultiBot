package QueryHandlers.Roles;

import QueryHandlers.QueryHandler;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.*;
import java.util.EnumSet;
import java.util.List;

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
        //Extracting information from message
        String messageText = message.getContentStripped();
        String roleName = messageText.substring(messageText.indexOf(" ")+1);
        Member sender = message.getMember();
        MessageChannel channel = message.getChannel();

        //Find all roles that match the given query
        List<Role> roles = message.getGuild().getRolesByName(roleName, true);

        //If there is one role that matches it, check if it would give member new permissions.  Print an error
        //if it would, else give them the role and print a success message.
        if (roles.size() == 1) {
            if (acceptablePermissions(sender.getPermissions(), roles.get(0).getPermissions())) {
                message.getGuild().addRoleToMember(sender, roles.get(0));
                channel.sendMessage("Success! " + roleName + " has been added to " + sender.getNickname()).queue();
            }
        //If no matching roles were found or 2+ were found, print appropriate error messages.
        } else if (roles.size() == 0) {
            channel.sendMessage("Error: No roles found that match that name.  Are you sure you spelled it correctly?").queue();
            System.out.println("Error: role removal failed due to finding no role matching: " + roleName);
        } else {
            channel.sendMessage("Error: More than one role was found matching that name.").queue();
            System.out.println("Error: role removal failed due to finding more than one role matching: " + roleName);
        }
    }

    /**
     * Return whether @param old contains all permissions that @param target does.
     * @param old The set of permissions being compared against.
     * @param target The set of permissions in question.
     * @return Whether old contains all permissions that target does.
     */
    private static boolean acceptablePermissions(EnumSet<Permission> old, EnumSet<Permission> target) {
        for (Permission p : target) {
            if (!old.contains(p)) {
                return false;
            }
        }
        return true;
    }
}
