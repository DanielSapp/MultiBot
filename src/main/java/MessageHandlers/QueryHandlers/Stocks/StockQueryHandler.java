package MessageHandlers.QueryHandlers.Stocks;

import MessageHandlers.QueryHandlers.QueryHandler;
import net.dv8tion.jda.api.entities.Message;
import pl.zankowski.iextrading4j.api.exception.IEXTradingException;
import pl.zankowski.iextrading4j.api.stocks.Quote;
import pl.zankowski.iextrading4j.client.IEXCloudClient;
import pl.zankowski.iextrading4j.client.IEXCloudTokenBuilder;
import pl.zankowski.iextrading4j.client.IEXTradingClient;
import pl.zankowski.iextrading4j.client.rest.request.stocks.QuoteRequestBuilder;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;

public class StockQueryHandler extends QueryHandler {
    private transient IEXCloudClient client;
    private String token;
    public StockQueryHandler(String token) {
        this.token = token;
        initializeClient();
    }

    /**
     * Returns whether @param message is a query for stock information.
     * @param message The message in question.
     * @return Whether the message is a query for stock information.
     */
    @Override
    public boolean canHandle(Message message) {
        return message.getContentStripped().toLowerCase().startsWith("!stock");
    }

    /**
     * Receive a query for stock information.  Parse a ticker from it, call client.executeRequest() to get a Quote
     * object, call generateResponse() to format it into a human readable String, and print that to the Discord channel
     * the query originated from.  Called from superclass method handleMessage() after it determines that @param message is a valid query.
     * @param message The query for stock information.
     */
    @Override
    public void executeQuery(Message message) {
        String ticker = message.getContentStripped().substring(message.getContentStripped().indexOf(" ")+1).toUpperCase();
        Quote quote;
        try {
            quote = client.executeRequest(new QuoteRequestBuilder().withSymbol(ticker).build());
        } catch (IEXTradingException e) {
            System.out.println("An exception occurred while fetching a stock quote from IEX");
            e.printStackTrace();
            message.getChannel().sendMessage("Sorry, an error occurred while looking that up").queue();
            return;
        }
        String response = generateResponse(quote);
        message.getChannel().sendMessage(response).queue();
    }

    /**
     * Overrides default writeObject() called during object serialization.
     * @param outputStream The stream this object is being written to.
     * @throws IOException If an error occurs during output.
     */
    private void writeObject(ObjectOutputStream outputStream) throws IOException {
        outputStream.writeUTF(token);
    }

    /**
     * Override default readObject() called during object serialization.  Read private variable token and then
     * use it to initialize client, since IEXTradingClient is not serializable and is therefore transient.
     * @param inputStream The stream this object is being initialized from.
     * @throws IOException If an error occurs during output.
     */
    private void readObject(ObjectInputStream inputStream) throws ClassNotFoundException, IOException {
        token = inputStream.readUTF();
        initializeClient();
    }

    /**
     * Initializes private variable client using the String stored in private variable token.
     */
    private void initializeClient() {
        client = IEXTradingClient.create(new IEXCloudTokenBuilder().withPublishableToken(token).build());
    }

    /**
     * Take a Quote object, return a formatted string to be sent as a response to the query.
     * @param quote A Quote object with information about a stock ticker.
     * @return A formatted string representing some of the information from the Quote.
     */
    private String generateResponse(Quote quote) {
        return "Ticker name: " + quote.getSymbol() + "\n" +
                "Company name: " + quote.getCompanyName() + "\n" +
                "Last price: $" + quote.getLatestPrice().setScale(2, RoundingMode.HALF_UP) + "\n" +
                "Daily change percentage: " + formatDailyChangePercentage(quote.getChangePercent()) + "\n" +
                "Market cap: $" + formatMarketCap(quote.getMarketCap()) + "\n" +
                "P/E Ratio: " + quote.getPeRatio() + "\n\n" +
                "Data provided by IEX API: http://www.iexcloud.io";
    }

    /**
     * Take BigDecimals numerator and denominator, return numerator/denominator rounded to the nearest integer.
     * @param numerator The BigDecimal to divide.
     * @param denominator The BigDecimal to divide by.
     * @return numerator/denominator rounded to the nearest integer.
     */
    private BigDecimal divideAndRound(BigDecimal numerator, BigDecimal denominator) {
        return numerator.divide(denominator).setScale(0, RoundingMode.HALF_UP);
    }

    /**
     * Take a BigDecimal representing a percentage amount, return it formatted as a percentage and rounded to 2 decimal places.
     * @param percentage A BigDecimal representing a percentage amount.
     * @return A String of the formatted percentage amount.
     */
    private String formatDailyChangePercentage(BigDecimal percentage) {
        return percentage.multiply(new BigDecimal(100)).setScale(2, RoundingMode.HALF_UP).toPlainString() + "%";
    }

    /**
     * Take a BigDecimal representing a market cap.  Return a formatted string in the format of ###b, ###m, or ###
     * for billions/millions as appropriate.
     * @param marketCap A BigDecimal representing a market cap amount.
     * @return A formatted string representing the market cap.
     */
    private String formatMarketCap(BigDecimal marketCap) {
        BigDecimal billion = new BigDecimal(1000000000);
        BigDecimal million = new BigDecimal(1000000);
        if (marketCap.compareTo(billion) != -1) {
            return divideAndRound(marketCap, billion).toPlainString() + "b";
        } else if (marketCap.compareTo(million) != -1) {
            return divideAndRound(marketCap, million).toPlainString() + "m";
        } else {
            return marketCap.setScale(0, RoundingMode.HALF_UP).toPlainString();
        }
    }
}