package Wolfram;

import MessageHandling.QueryHandler;
import net.dv8tion.jda.api.entities.Message;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

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
    public void handleQuery (Message message)  {
        String messageText = message.getContentStripped();
        String query = messageText.substring(messageText.indexOf("!wolfram")+8).trim();
        String responseText;
        try {
            responseText = getQueryAnswer(query);
        } catch (IOException e) {
            System.out.println("An error occurred while executing a Wolfram Alpha query");
            e.printStackTrace();
            return;
        }
        message.getChannel().sendMessage(responseText).queue();
    }
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
