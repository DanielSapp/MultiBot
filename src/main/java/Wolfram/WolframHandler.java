package Wolfram;

import MessageHandling.QueryHandler;
import net.dv8tion.jda.api.entities.Message;

public class WolframHandler extends QueryHandler {
    private String appID;

    /**
     * @param appID The application ID that must be passed to the Wolfram API as authentication.
     */
    public WolframHandler(String appID) {
        this.appID = appID;
    }

    /**
     * @param message The user message that contained this query.
     */
    @Override
    public void handleQuery(Message message) {
        String messageText = message.getContentStripped();
        String query = messageText.substring(messageText.indexOf("!wolfram")+8).trim();
    }
}
