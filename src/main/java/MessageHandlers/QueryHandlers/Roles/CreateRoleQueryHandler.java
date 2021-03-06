package MessageHandlers.QueryHandlers.Roles;

import MessageHandlers.QueryHandlers.QueryHandler;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;

public class CreateRoleQueryHandler extends QueryHandler {

    /**
     * Return whether @param message is a query to create a new Role.
     * @param message The message in question.
     * @return Whether the message is a query to create a new Role.
     */
    @Override
    public boolean canHandle(Message message) {
        String messageText = message.getContentStripped().toLowerCase();
        return messageText.startsWith("!createrole") || messageText.startsWith("!createrank");
    }

    /**
     * Receive a query to create a new Role.  Create the Role if the query sender has the modify roles permission
     * and a Role by that name doesn't already exist, else print error info to console and in the Discord
     * channel the message originated from.  Called from superclass method handleMessage()
     * after it determines that @param message is a valid query.
     * @param message The query to create a new Role.
     */
    @Override
    public void executeQuery(Message message) {
        //Extracting information from message
        String messageText = message.getContentStripped();
        String roleName = messageText.substring(messageText.indexOf(" ")+1);
        Member sender = message.getMember();
        MessageChannel channel = message.getChannel();

        //If the sender has permission to manage roles, see if a role by the queried name already exists.
        //Create a new one if there isn't.  Print appropriate errors for all other outcomes.
        if (sender.getPermissions().contains(Permission.MANAGE_ROLES)) {
            if (sender.getGuild().getRolesByName(roleName, true).size() == 0) {
                sender.getGuild().createRole().setName(roleName).queue();
                channel.sendMessage("Role successfully created!").queue();
                System.out.println("Role " + roleName + " has been created by " + sender.getEffectiveName() + " in " + sender.getGuild().getName());
            } else {
                RoleErrorPrinter.sendRoleAlreadyExistsError(sender, channel, roleName);
            }
        } else {
            RoleErrorPrinter.sendPermissionsError(sender, channel, "create");
        }
    }
}
