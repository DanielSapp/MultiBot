package QueryHandlers.Stocks;

import QueryHandlers.QueryHandler;
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

    @Override
    public void handleQuery(Message message) {
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

    private String generateResponse(Quote quote) {
        return "Ticker name: " + quote.getSymbol() + "\n" +
                "Company name: " + quote.getCompanyName() + "\n" +
                "Last price: $" + quote.getLatestPrice().setScale(2, RoundingMode.HALF_UP) + "\n" +
                "Daily change percentage: " + formatDailyChangePercentage(quote.getChangePercent()) + "\n" +
                "Market cap: $" + formatMarketCap(quote.getMarketCap()) + "\n" +
                "P/E Ratio: " + quote.getPeRatio() + "\n\n" +
                "Data provided by IEX API: http://www.iexcloud.io";
    }

    @Override
    public boolean canHandle(Message message) {
        return message.getContentStripped().toLowerCase().startsWith("!stock");
    }

    private void readObject(ObjectInputStream inputStream) throws ClassNotFoundException, IOException {
        token = inputStream.readUTF();
        initializeClient();
    }

    private String formatMarketCap(BigDecimal bd) {
        BigDecimal billion = new BigDecimal(1000000000);
        BigDecimal million = new BigDecimal(1000000);
        if (bd.compareTo(billion) != -1) {
            return divideAndRound(bd, billion).toPlainString() + "b";
        } else if (bd.compareTo(million) != -1) {
            return divideAndRound(bd, million).toPlainString() + "m";
        } else {
            return bd.setScale(0, RoundingMode.HALF_UP).toPlainString();
        }
    }

    private void writeObject(ObjectOutputStream outputStream) throws IOException {
        outputStream.writeUTF(token);
    }

    private void initializeClient() {
        client = IEXTradingClient.create(new IEXCloudTokenBuilder().withPublishableToken(token).build());
    }

    private BigDecimal divideAndRound(BigDecimal numerator, BigDecimal denominator) {
        return numerator.divide(denominator).setScale(0, RoundingMode.HALF_UP);
    }

    private String formatDailyChangePercentage(BigDecimal percentage) {
        return percentage.multiply(new BigDecimal(100)).setScale(2, RoundingMode.HALF_UP).toPlainString() + "%";
    }
}