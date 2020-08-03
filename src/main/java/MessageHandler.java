import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.requests.GatewayIntent;

import javax.security.auth.login.LoginException;

public class MessageHandler extends ListenerAdapter {
    public static void main(String[] args){
        JDA jda = null;
        try {
            jda = JDABuilder.create(args[0], GatewayIntent.GUILD_MESSAGES).build();
        } catch (LoginException e) {
            System.out.println("An error occurred while trying to login.");
            System.exit(1);
        }
        jda.addEventListener(new MessageHandler());
    }
}
