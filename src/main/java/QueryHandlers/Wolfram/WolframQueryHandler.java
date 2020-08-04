package QueryHandlers.Wolfram;

import QueryHandlers.QueryHandler;
import net.dv8tion.jda.api.entities.Message;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

public class WolframQueryHandler extends QueryHandler {
    private String appID;

    /**
     * @param appID The application ID that must be passed to the QueryHandlers.Wolfram API as authentication.
     */
    public WolframQueryHandler(String appID) {
        this.appID = appID;
    }

    /**
     * Returns true IFF @param message begins with !wolfram, indicating it is a wolfram query.
     * @param message The message in question.
     * @return Whether it is a QueryHandlers.Wolfram query.
     */
    @Override
    public boolean canHandle(Message message) {
        return message.getContentStripped().toLowerCase().startsWith("!wolfram");
    }

    /**
     * Takes a message representing a Message that included a query for QueryHandlers.Wolfram Alpha, parses the query,
     * calls getQueryAnswer() to get QueryHandlers.Wolfram's response, and sends the response in the channel the query was initiated in.
     * @param message The user message that contained this query.
     */
    @Override
    public void handleQuery (Message message)  {
        String messageText = message.getContentStripped();
        String query = messageText.substring(messageText.indexOf("!wolfram")+8).trim();
        String responseText;
        try {
            responseText = getQueryAnswer(query);
        } catch (IOException e) {
            System.out.println("An error occurred while executing a QueryHandlers.Wolfram Alpha query");
            e.printStackTrace();
            return;
        }
        message.getChannel().sendMessage(responseText).queue();
    }

    /**
     * POSTs @param query to the QueryHandlers.Wolfram Alpha simple response API and returns its response.
     * @param query The text query to be sent to QueryHandlers.Wolfram Alpha.
     * @return The text answer returned by QueryHandlers.Wolfram Alpha.
     * @throws IOException if an error occurs while interacting with QueryHandlers.Wolfram Alpha.
     */
    private String getQueryAnswer(String query) throws IOException {
        URL url = new URL("http://api.wolframalpha.com/v1/result?appid=" + appID + "&i=" + URLEncoder.encode(query, StandardCharsets.UTF_8));
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("POST");
        BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        StringBuilder responseText = new StringBuilder();
        String nextLine;
        while ((nextLine = br.readLine()) != null) {
            responseText.append(nextLine);
        }
        return responseText.toString();
    }
}
