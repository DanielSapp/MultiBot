package QueryHandlers.Roles;

import QueryHandlers.NoGatewayIntentQueryHandler;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;

public class CreateRoleQueryHandler extends NoGatewayIntentQueryHandler {

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
     * channel the message originated from.
     * @param message The query to create a new Role.
     */
    @Override
    public void handleQuery(Message message) {
        String messageText = message.getContentStripped();
        String roleName = messageText.substring(messageText.indexOf(" ")+1);
        Member sender = message.getMember();
        MessageChannel channel = message.getChannel();
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
