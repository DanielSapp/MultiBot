package QueryHandlers.Roles;

import QueryHandlers.QueryHandler;
import net.dv8tion.jda.api.entities.*;

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
        Member member = message.getMember();
        String messageText = message.getContentStripped();
        MessageChannel channel = message.getChannel();
        String roleName = messageText.substring(messageText.indexOf(" ")+1);

        //Get all roles that match the Member's query
        List<Role> matchingRoles = message.getGuild().getRolesByName(roleName, true);

        //If one role matches the query, see if the Member has it.  Remove it and print success messages if they do,
        //otherwise print errors.
        if (matchingRoles.size() == 1) {
            if (member.getRoles().contains(matchingRoles.get(0))) {
                message.getGuild().removeRoleFromMember(member, matchingRoles.get(0)).queue();
                System.out.println("Role " + roleName + " has been removed from " + member.getEffectiveName() + " in " + message.getGuild().getName() + ".");
                channel.sendMessage(matchingRoles.get(0).getName() + " has been removed from " + member.getEffectiveName() + ".").queue();
            } else {
                System.out.println("Error: " + member.getEffectiveName() + " in " + message.getGuild().getName() + " tried to remove Role " + roleName + " that they don't have.");
                channel.sendMessage("Error: " + member.getEffectiveName() + " does not have the Role " + matchingRoles.get(0).getName()).queue();
            }
        //Else if no matching roles were found or more than one matching Role was found, print appropriate error messages
        } else if (matchingRoles.size() == 0) {
            System.out.println("Error: " + member.getEffectiveName() + " in " + message.getGuild().getName() + " tried to remove Role " + roleName + " but no Roles matched it.");
            channel.sendMessage("Error: no roles found called " + roleName).queue();
        } else {
            System.out.println("Error: " + member.getEffectiveName() + " in " + message.getGuild().getName() + " tried to remove Role " + roleName + " but more than one Role matched it");
            StringBuilder messageBuilder = new StringBuilder();
            messageBuilder.append("Error: all of the following Roles match " + roleName + "\n");
            for (Role r : matchingRoles) {
                messageBuilder.append(r.getName() + "\n");
            }
            channel.sendMessage(messageBuilder).queue();
        }
    }
}
