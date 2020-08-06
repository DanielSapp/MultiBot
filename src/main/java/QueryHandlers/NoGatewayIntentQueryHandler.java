package QueryHandlers;

import net.dv8tion.jda.api.requests.GatewayIntent;

//A class for QueryHandlers that don't require additional GatewayIntents to derive from so
//they don't all need to implement getGatewayIntents().
public abstract class NoGatewayIntentQueryHandler extends QueryHandler {
    @Override
    public GatewayIntent[] getGatewayIntents() {
        return new GatewayIntent[0];
    }
}
