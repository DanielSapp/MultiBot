package MessageHandlers;

import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.requests.GatewayIntent;
import java.io.Serializable;

//The class that all classes that handle Messages derive from.
public abstract class MessageHandler implements Serializable {
    //Overridden in subclasses IFF it needs a GatewayIntent that is not initialized by default.
    public GatewayIntent[] getGatewayIntents() {
        return new GatewayIntent[0];
    }

    //Called every time a message is received in a guild.
    public abstract void handleMessage(Message m);
}
