package Initialization;

import QueryHandlers.QueryHandler;
import QueryHandlers.Roles.AddRoleQueryHandler;
import QueryHandlers.Roles.ListRolesQueryHandler;
import QueryHandlers.Roles.RemoveRoleQueryHandler;
import QueryHandlers.Stocks.StockQueryHandler;
import QueryHandlers.Wolfram.WolframQueryHandler;
import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

public class ConfigurationGenerator {
    private final Scanner scanner = new Scanner(System.in);

    /**
     * The entry point of ConfigurationGenerator.  Ask whether the user has a configuration file.  If yes,
     * call createQueryHandlersFromFile() to get the QueryHandlers that should be active and returns them.
     * Else, calls createQueryHandlersFromCommandLine to do the same, optionally calls saveQueryHandlersToFile()
     * to save them for future initializations, then returns them.
     * @return All QueryHandlers that will be active.
     */
    public ArrayList<QueryHandler> getQueryHandlers() {
        ArrayList<QueryHandler> toReturn;
        System.out.println("Do you have a configuration file?  y/n");
        if (userAnsweredYes()) {
            toReturn = createQueryHandlersFromFile();
        } else {
            toReturn = createQueryHandlersFromCommandLine();
            System.out.println("Would you like to save your configuration?");
            if (userAnsweredYes()) {
                saveQueryHandlersToFile(toReturn);
            }
        }
        return toReturn;
    }

    /**
     * Reads a bot configuration from file and returns an ArrayList<QueryHandler> representing it.
     * @return A list of all QueryHandlers that were read from file.
     */
    private ArrayList<QueryHandler> createQueryHandlersFromFile() {
        while (true) {
            System.out.println("Enter the path of the file");
            try (ObjectInputStream objectInputStream = new ObjectInputStream(new FileInputStream(scanner.nextLine()))) {
                return (ArrayList<QueryHandler>) objectInputStream.readObject();
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
     * Generates a bot configuration through user prompts and returns it as an ArrayList<QueryHandler>
     * @return A list of all QueryHandlers that were created by the user.
     */
    private ArrayList<QueryHandler> createQueryHandlersFromCommandLine() {
        ArrayList<QueryHandler> toReturn = new ArrayList<>();
        System.out.println("Would you like to enable Wolfram Alpha queries?");
        if (userAnsweredYes()) {
            System.out.println("What is your application key?");
            String applicationKey = scanner.nextLine();
            toReturn.add(new WolframQueryHandler(applicationKey));
        }
        System.out.println("Would you like to enable stock statistics?");
        if (userAnsweredYes()) {
            System.out.println("Enter your public IEX key");
            toReturn.add(new StockQueryHandler(scanner.nextLine()));
        }
        System.out.println("Would you like to enable adding roles to users?");
        if (userAnsweredYes()) {
            toReturn.add(new AddRoleQueryHandler());
        }
        System.out.println("Would you like to enable removing roles from users?");
        if (userAnsweredYes()) {
            toReturn.add(new RemoveRoleQueryHandler());
        }
        System.out.println("Would you like to enable listing guild rules?");
        if (userAnsweredYes()) {
            toReturn.add(new ListRolesQueryHandler());
        }
        //Add prompts for new query handlers here as necessary.
        return toReturn;
    }

    /**
     * Prompts the user for a file name and saves the QueryHandlers in @param handlers there to be read
     * on future initializations.
     * @param handlers An ArrayList<QueryHandlers> to be saved to disk.
     */
    private void saveQueryHandlersToFile (ArrayList<QueryHandler> handlers) {
        System.out.println("What should the configuration file be named?");
        String fileName = scanner.nextLine();
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(fileName))) {
            oos.writeObject(handlers);
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