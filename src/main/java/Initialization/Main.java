package Initialization;

import MessageHandling.MessageHandler;
import MessageHandling.QueryHandler;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.requests.GatewayIntent;
import javax.security.auth.login.LoginException;
import java.util.ArrayList;

public class Main {

    public static void main(String[] args){
        //Initialize a new Initialization.ConfigurationGenerator and have it generate all QueryHandlers that
        //the user wants active.
        ConfigurationGenerator cg = new ConfigurationGenerator();
        ArrayList<QueryHandler> queryHandlers = cg.getQueryHandlers();

        //Initialize a JDA object.  Exit if initialization is unsuccessful.
        JDA jda = null;
        try {
            jda = JDABuilder.create(args[0], GatewayIntent.GUILD_MESSAGES).build();
        } catch (LoginException e) {
            System.out.println("An error occurred while trying to login.");
            System.exit(1);
        }

        //Register a new instance of this class as an EventListener and pass in the ArrayList of QueryHandlers.
        jda.addEventListener(new MessageHandler(queryHandlers));
    }
}