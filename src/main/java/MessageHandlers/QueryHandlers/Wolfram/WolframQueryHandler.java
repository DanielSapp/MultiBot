package MessageHandlers.QueryHandlers.Wolfram;

import MessageHandlers.QueryHandlers.QueryHandler;
import net.dv8tion.jda.api.entities.Message;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class WolframQueryHandler extends QueryHandler {
    private final String appID;

    /**
     * @param appID The application ID that must be passed to the Wolfram API as authentication.
     */
    public WolframQueryHandler(String appID) {
        this.appID = appID;
    }

    /**
     * Return true IFF @param message begins with !wolfram, indicating it is a Wolfram query.
     * @param message The message in question.
     * @return Whether it is a Wolfram query.
     */
    @Override
    public boolean canHandle(Message message) {
        return message.getContentStripped().toLowerCase().startsWith("!wolfram");
    }

    /**
     * Take a Message that is a query for Wolfram Alpha, parse it, call getQueryAnswer() to get Wolfram's response,
     * and send the response in the channel the query was initiated in.  Called from superclass method handleMessage()
     * after it determines that @param message is a valid query.
     * @param message The user message that contained this query.
     */
    @Override
    public void executeQuery (Message message)  {
        String messageText = message.getContentStripped();
        String query = messageText.substring(messageText.indexOf(" ")+1).trim();
        String responseText;
        try {
            responseText = getQueryAnswer(query);
        } catch (IOException e) {
            System.out.println("An error occurred while executing a MessageHandlers.QueryHandlers.Wolfram Alpha query");
            e.printStackTrace();
            message.getChannel().sendMessage("An error occurred while executing that query.").queue();
            return;
        }
        message.getChannel().sendMessage(responseText).queue();
    }

    /**
     * POST @param query to the Wolfram Alpha Simple Response API and returns its response.
     * @param query The text query to be sent to Wolfram Alpha.
     * @return The text answer returned by Wolfram Alpha.
     * @throws IOException if an error occurs while interacting with Wolfram Alpha.
     */
    private String getQueryAnswer(String query) throws IOException {
        URL url = new URL("http://api.wolframalpha.com/v1/result?appid=" + appID + "&i=" + URLEncoder.encode(query, "UTF-8"));
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        StringBuilder responseText = new StringBuilder();
        String nextLine;
        while ((nextLine = br.readLine()) != null) {
            responseText.append(nextLine);
        }
        return responseText.toString();
    }
}
