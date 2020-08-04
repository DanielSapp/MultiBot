package QueryHandlers.Stocks;

import QueryHandlers.QueryHandler;
import net.dv8tion.jda.api.entities.Message;

public class StockQueryHandler extends QueryHandler {
    @Override
    public void handleQuery(Message message) {
        //TODO
    }

    @Override
    public boolean canHandle(Message message) {
        return message.getContentStripped().toLowerCase().startsWith("!stock");
    }
}
