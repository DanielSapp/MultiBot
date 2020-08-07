package Initialization;

import QueryHandlers.QueryHandler;
import net.dv8tion.jda.api.requests.GatewayIntent;
import java.io.Serializable;
import java.util.ArrayList;

//A class representing the data needed to initialize a Discord bot.  This is saved to file after the user
//creates one the first time the bot is run.  necessaryGatewayIntents is derived from queryHandlers in the constructor
//to abstract their determination away from the rest of the program
public class ConfigurationTemplate implements Serializable {
    private ArrayList<GatewayIntent> necessaryGatewayIntents;
    private ArrayList<QueryHandler> queryHandlers;
    private String discordToken;

    public ConfigurationTemplate(ArrayList<QueryHandler> queryHandlers, String discordToken) {
        this.queryHandlers = queryHandlers;
        this.discordToken = discordToken;
        initializeNecessaryGatewayIntents();
    }

    private void initializeNecessaryGatewayIntents() {
        necessaryGatewayIntents = new ArrayList<>();
        for (QueryHandler handler : queryHandlers) {
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

    public ArrayList<QueryHandler> getQueryHandlers() {
        return queryHandlers;
    }

    public String getDiscordToken() {
        return discordToken;
    }
}