package Initialization;

import MessageDistribution.MessageDistributor;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import javax.security.auth.login.LoginException;

public class Main {

    public static void main(String[] args){
        //Initialize a new ConfigurationGenerator and have it generate a ConfigurationTemplate
        //that contains all necessary information to initialize the bot.
        ConfigurationTemplate template = new ConfigurationGenerator().getConfigurationTemplate();

        //Initialize a JDA object.  Exit if initialization is unsuccessful.
        JDA jda = null;
        try {
            jda = JDABuilder.create(template.getDiscordToken(), template.getNecessaryGatewayIntents()).build();
        } catch (LoginException e) {
            System.out.println("An error occurred while trying to login.");
            System.exit(1);
        }

        //Register a new instance of this class as an EventListener and pass in all MessageHandlers.QueryHandlers that will be active.
        jda.addEventListener(new MessageDistributor(template.getMessageHandlers()));
    }
}