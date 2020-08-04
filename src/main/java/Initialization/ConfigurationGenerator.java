package Initialization;

import MessageHandling.Query;
import MessageHandling.QueryHandler;
import Roles.RoleQueryHandler;
import Stocks.StockQueryHandler;
import Wolfram.WolframQueryHandler;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

public class ConfigurationGenerator {
    private final Scanner scanner = new Scanner(System.in);

    /**
     * The entry point of ConfigurationGenerator.  Creates a Map that maps a Query types to QueryHandlers that should
     * receive events of that category.  The Map is either read from file, or generated through user prompts if
     * there is no pre-existing configuration file.
     * @return A Map that maps a Query types to QueryHandlers that should receive events of that category.
     */
    public HashMap<Query, ArrayList<QueryHandler>> getQueryHandlerMap() {
        HashMap<Query, ArrayList<QueryHandler>> queryHandlerMap;
        System.out.println("Do you have a configuration file?  y/n");
        if (userAnsweredYes()) {
            queryHandlerMap = createQueryHandlerMapFromFile();
        } else {
            queryHandlerMap = createQueryHandlerMapFromCommandLine();
            System.out.println("Would you like to save your configuration?");
            if (userAnsweredYes()) {
                saveQueryHandlerToFile(queryHandlerMap);
            }
        }
        return queryHandlerMap;
    }

    /**
     * Reads a bot configuration from file and returns a Map representing it.
     * @return A Map that maps a Query types to QueryHandlers that should receive events of that category.
     */
    private HashMap<Query, ArrayList<QueryHandler>> createQueryHandlerMapFromFile() {
        while (true) {
            System.out.println("Enter the path of the file");
            try (ObjectInputStream objectInputStream = new ObjectInputStream(new FileInputStream(scanner.nextLine()))) {
                return (HashMap<Query, ArrayList<QueryHandler>>) objectInputStream.readObject();
            } catch (FileNotFoundException e) {
                System.out.println("Error: invalid file path");
                e.printStackTrace();
            } catch (IOException e) {
                System.out.println("Error: IOException");
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                System.out.println("Error: object type not found during configuration deserialization");
                e.printStackTrace();
            }
        }
    }

    /**
     * Generates a bot configuration through user prompts and returns it as a Map.
     * @return A Map that maps a Query types to QueryHandlers that should receive events of that category.
     */
    private HashMap<Query, ArrayList<QueryHandler>> createQueryHandlerMapFromCommandLine() {
        HashMap<Query, ArrayList<QueryHandler>> queryHandlerMap = new HashMap<>();
        System.out.println("Would you like to enable Wolfram Alpha queries?");
        if (userAnsweredYes()) {
            System.out.println("What is your application key?");
            queryHandlerMap.putIfAbsent(Query.WOLFRAM, new ArrayList<>());
            queryHandlerMap.get(Query.WOLFRAM).add(new WolframQueryHandler(scanner.nextLine()));
        }
        System.out.println("Would you like to enable stock statistics?");
        if (userAnsweredYes()) {
            queryHandlerMap.putIfAbsent(Query.STOCKS, new ArrayList<>());
            queryHandlerMap.get(Query.STOCKS).add(new StockQueryHandler());
        }
        System.out.println("Would you like to enable role management?");
        if (userAnsweredYes()) {
            queryHandlerMap.putIfAbsent(Query.ROLE, new ArrayList<>());
            queryHandlerMap.get(Query.ROLE).add(new RoleQueryHandler());
        }
        //Add prompts for new query handlers here as necessary.
        return queryHandlerMap;
    }

    /**
     * Prompts the user for a file name and saves @param queryHandlerMap there as a serialized object to be read
     * on future bot initializations.
     * @param queryHandlerMap A Map that maps a Query types to QueryHandlers that should receive events of that category.
     */
    private void saveQueryHandlerToFile (HashMap<Query, ArrayList<QueryHandler>> queryHandlerMap) {
        System.out.println("What should the configuration file be named?");
        String fileName = scanner.nextLine();
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(fileName))) {
            oos.writeObject(queryHandlerMap);
        } catch (IOException e) {
            System.out.println("An error occurred while saving the configuration file.  Your settings have not been saved.");
            e.printStackTrace();
        }
    }

    /**
     * Retrieves user input.  Returns true if they input Y/y, false if they input N/n, and loops if any other input
     * is received.
     * @return true if the user indicated a "yes" as their first valid response, false otherwise.
     */
    private boolean userAnsweredYes() {
        while (true) {
            String response = scanner.nextLine().toLowerCase();
            if (response.equals("y")) {
                return true;
            } else if (response.equals("n")) {
                return false;
            } else {
                System.out.println("Invalid input.  Enter y/n");
            }
        }
    }
}