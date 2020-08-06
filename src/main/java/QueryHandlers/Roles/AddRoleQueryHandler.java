package QueryHandlers.Roles;

import QueryHandlers.NoGatewayIntentQueryHandler;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.Role;
import java.util.EnumSet;
import java.util.List;

public class AddRoleQueryHandler extends NoGatewayIntentQueryHandler {

    /**
     * Return whether @param message is a query to add a Role to user.
     * @param message The message in question.
     * @return Whether the message is a query to add a Role to user.
     */
    @Override
    public boolean canHandle(Message message) {
        String messageText = message.getContentStripped().toLowerCase();
        return messageText.startsWith("!addrole") || messageText.startsWith("!addrank");
    }

    /**
     * Receive a query to add a Role to a user.  Add it if the role is found and it does not give the Member new
     * permissions, else print error info to console and in the Discord channel the message originated from.
     * @param message The query to add a Role to the user.
     */
    @Override
    public void handleQuery(Message message) {
        //Extracting information from message
        String messageText = message.getContentStripped();
        String roleName = messageText.substring(messageText.indexOf(" ")+1);
        Member sender = message.getMember();
        MessageChannel channel = message.getChannel();

        //Find all roles that match the given query
        List<Role> matchingRoles = message.getGuild().getRolesByName(roleName, true);

        //If there is one role that matches it, check if it would give member new permissions.  Print an error
        //if it would, else give them the role and print a success message.
        if (matchingRoles.size() == 1) {
            if (sender.getRoles().contains(matchingRoles.get(0))){
                RoleErrorPrinter.sendUserAlreadyHasRoleError(sender, channel, matchingRoles.get(0).getName());
            } else if (acceptablePermissions(sender.getPermissions(), matchingRoles.get(0).getPermissions())) {
                message.getGuild().addRoleToMember(sender, matchingRoles.get(0)).queue();
                channel.sendMessage(roleName + " has been added to " + sender.getEffectiveName()).queue();
                System.out.println("Role " + roleName + " has been added to user " + sender.getEffectiveName() + " in " + message.getGuild().getName());
            }
        //If no matching roles were found or 2+ were found, print appropriate error messages.
        } else if (matchingRoles.size() == 0) {
            RoleErrorPrinter.sendNoRolesFoundError(sender, channel, roleName, "add");
        } else {
            RoleErrorPrinter.sendMultipleRolesFoundError(sender, channel, roleName, matchingRoles, "add");
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