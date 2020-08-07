package Initialization;

import QueryHandlers.QueryHandler;
import net.dv8tion.jda.api.requests.GatewayIntent;
import java.io.Serializable;
import java.util.ArrayList;

public class ConfigurationTemplate implements Serializable {
    private ArrayList<GatewayIntent> necessaryGatewayIntents;
    private ArrayList<QueryHandler> queryHandlers;
    private String discordToken;

    public ArrayList<GatewayIntent> getNecessaryGatewayIntents() {
        return necessaryGatewayIntents;
    }

    public ArrayList<QueryHandler> getQueryHandlers() {
        return queryHandlers;
    }

    public String getDiscordToken() {
        return discordToken;
    }

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
}