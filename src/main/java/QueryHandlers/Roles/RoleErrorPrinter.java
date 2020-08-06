package QueryHandlers.Roles;

import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.Role;
import java.util.List;

//A class that contains static methods that send error messages that are common between 2+ RoleQueryHandlers.
public class RoleErrorPrinter {

    public static void sendMultipleRolesFoundError(Member sender, MessageChannel channel, String roleName, List<Role> roles, String actionType) {
        StringBuilder matchingRoleListBuilder = new StringBuilder();
        for (Role r : roles) {
            matchingRoleListBuilder.append(r.getName()).append("\n");
        }
        channel.sendMessage("Error: all of the following roles match your query.\n" + matchingRoleListBuilder).queue();
        System.out.println("Error: " + sender.getEffectiveName() + " tried to " + actionType + " Role " + roleName +
                " in " + sender.getGuild().getName() + " but all of these roles matched it:\n" + matchingRoleListBuilder);
    }

    public static void sendNoRolesFoundError(Member sender, MessageChannel mc, String roleName, String actionType) {
        mc.sendMessage("Error: no Roles were found matching the name " + roleName + ".  Check your spelling").queue();
        System.out.println("Error: " + sender.getEffectiveName() + " tried to " + actionType + " role " + roleName + " in " +
                sender.getGuild().getName() + " but no matching roles were found.");
    }

    public static void sendPermissionsError(Member sender, MessageChannel channel, String actionType) {
        channel.sendMessage("Error: you don't have the permission to modify roles.").queue();
        System.out.println("Error: " + sender.getEffectiveName() + " tried to " + actionType + " a role in " +
                sender.getGuild().getName() + " but does not have the modify roles permission.");
    }

    public static void sendRoleAlreadyExistsError(Member sender, MessageChannel channel, String roleName) {
        channel.sendMessage("Error: a role by that name already exists!").queue();
        System.out.println("Error: " + sender.getEffectiveName() + " in " + sender.getGuild().getName() +
                " tried to create a role called " + roleName + " but a role named that already exists.");
    }

    public static void sendUserAlreadyHasRoleError(Member sender, MessageChannel channel, String roleName) {
        System.out.println("Error: " + sender.getEffectiveName() + " in " + sender.getGuild().getName() + " tried to add role " + roleName + " but they already have it.");
        channel.sendMessage("Error: you already have that role!").queue();
    }

    public static void sendUserDoesntHaveRoleError(Member sender, MessageChannel channel, String roleName) {
        System.out.println("Error: " + sender.getEffectiveName() + " in " + sender.getGuild().getName() + " tried to remove Role " + roleName + " that they don't have.");
        channel.sendMessage("Error: " + sender.getEffectiveName() + " does not have the Role " + roleName).queue();
    }
}
