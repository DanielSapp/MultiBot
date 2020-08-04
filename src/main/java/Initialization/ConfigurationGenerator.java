package Initialization;

import MessageHandling.Query;
import MessageHandling.QueryHandler;

import java.util.HashMap;
import java.util.Scanner;

public class ConfigurationGenerator {
    private final Scanner scanner = new Scanner(System.in);

    /**
     * @return A Map that maps a Query enum representing a bot command type to all of the QueryHandlers that should receive commands of that category.
     */
    public HashMap<Query, QueryHandler[]> getQueryHandlerMap() {
        //TODO
        return null;
    }
}
