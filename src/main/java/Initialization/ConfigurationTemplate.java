package Initialization;

import MessageHandlers.MessageHandler;
import net.dv8tion.jda.api.requests.GatewayIntent;
import java.io.Serializable;
import java.util.ArrayList;

//A class representing the data needed to initialize a Discord bot.  This is saved to file after the user
//creates one the first time the bot is run.  necessaryGatewayIntents is derived from messageHandlers in the constructor
//to abstract their determination away from the rest of the program.
public class ConfigurationTemplate implements Serializable {
    private ArrayList<GatewayIntent> necessaryGatewayIntents;
    private ArrayList<MessageHandler> messageHandlers;
    private String discordToken;

    public ConfigurationTemplate(ArrayList<MessageHandler> messageHandlers, String discordToken) {
        this.messageHandlers = messageHandlers;
        this.discordToken = discordToken;
        initializeNecessaryGatewayIntents();
    }

    /**
     * Set necessaryGatewayIntents to the union of all GatewayIntents that messageHandlers require to function.
     */
    private void initializeNecessaryGatewayIntents() {
        necessaryGatewayIntents = new ArrayList<>();
        necessaryGatewayIntents.add(GatewayIntent.GUILD_MESSAGES);
        for (MessageHandler handler : messageHandlers) {
            for (GatewayIntent intent : handler.getGatewayIntents()) {
                if (!necessaryGatewayIntents.contains(intent)) {
                    necessaryGatewayIntents.add(intent);
                }
            }
        }
    }

    public ArrayList<GatewayIntent> getNecessaryGatewayIntents() {
        return necessaryGatewayIntents;
    }

    public ArrayList<MessageHandler> getMessageHandlers() {
        return messageHandlers;
    }

    public String getDiscordToken() {
        return discordToken;
    }
}