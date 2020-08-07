package Initialization;

import MessageHandlers.MessageHandler;
import MessageHandlers.QueryHandlers.Roles.*;
import MessageHandlers.QueryHandlers.Stocks.StockQueryHandler;
import MessageHandlers.QueryHandlers.Wolfram.WolframQueryHandler;
import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

public class ConfigurationGenerator {
    private final Scanner scanner = new Scanner(System.in);

    /**
     * The entry point of ConfigurationGenerator.  Ask whether the user has a configuration file.  If yes,
     * call createConfigTemplateFromFile() to generate and return a ConfigurationTemplate.
     * Else, call createConfigTemplateFromCommandLine() to create a ConfigurationTemplate,
     * optionally call saveQueryHandlersToFile() to save it for future initializations, then return it.
     * @return A ConfigurationTemplate with all the information needed to initialize the bot.
     */
    public ConfigurationTemplate getConfigurationTemplate() {
        System.out.println("Do you have a configuration file?  y/n");
        ConfigurationTemplate template;
        if (userAnsweredYes()) {
            template = createConfigTemplateFromFile();
        } else {
            template = createConfigTemplateFromCommandLine();
            System.out.println("Would you like to save your configuration?");
            if (userAnsweredYes()) {
                saveConfigTemplateToFile(template);
            }
        }
        return template;
    }

    /**
     * Read a bot configuration from file and return a ConfigurationTemplate representing it.
     * @return A ConfigurationTemplate with all the information needed to initialize the bot.
     */
    private ConfigurationTemplate createConfigTemplateFromFile() {
        while (true) {
            System.out.println("Enter the path of the file");
            try (ObjectInputStream objectInputStream = new ObjectInputStream(new FileInputStream(scanner.nextLine()))) {
                return (ConfigurationTemplate) objectInputStream.readObject();
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
     * Generate a bot configuration through user prompts and return it as a ConfigurationTemplate
     * @return A ConfigurationTemplate with all the information needed to initialize the bot.
     */
    private ConfigurationTemplate createConfigTemplateFromCommandLine() {
        ArrayList<MessageHandler> handlers = new ArrayList<>();
        System.out.println("What is your Discord token?");
        String token = scanner.nextLine();
        System.out.println("Would you like to enable Wolfram Alpha queries?");
        if (userAnsweredYes()) {
            System.out.println("What is your application key?");
            String applicationKey = scanner.nextLine();
            handlers.add(new WolframQueryHandler(applicationKey));
        }
        System.out.println("Would you like to enable stock statistics?");
        if (userAnsweredYes()) {
            System.out.println("Enter your public IEX key");
            handlers.add(new StockQueryHandler(scanner.nextLine()));
        }
        System.out.println("Would you like to enable adding roles to users?");
        if (userAnsweredYes()) {
            handlers.add(new AddRoleQueryHandler());
        }
        System.out.println("Would you like to enable removing roles from users?");
        if (userAnsweredYes()) {
            handlers.add(new RemoveRoleQueryHandler());
        }
        System.out.println("Would you like to enable creating new roles?");
        if (userAnsweredYes()) {
            handlers.add(new CreateRoleQueryHandler());
        }
        System.out.println("Would you like to enable deleting existing roles?");
        if (userAnsweredYes()) {
            handlers.add(new DeleteRoleQueryHandler());
        }
        System.out.println("Would you like to enable listing guild rules?");
        if (userAnsweredYes()) {
            handlers.add(new ListRolesQueryHandler());
        }
        //Add prompts for new query handlers here as necessary.
        return new ConfigurationTemplate(handlers, token);
    }

    /**
     * Prompt the user for a file name and save the ConfigurationTemplate in @param template there to be read
     * on future initializations.
     * @param template The ConfigurationTemplate to be saved to disk.
     */
    private void saveConfigTemplateToFile(ConfigurationTemplate template) {
        System.out.println("What should the configuration file be named?");
        String fileName = scanner.nextLine();
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(fileName))) {
            oos.writeObject(template);
        } catch (IOException e) {
            System.out.println("An error occurred while saving the configuration file.  Your settings have not been saved.");
            e.printStackTrace();
        }
    }

    /**
     * Retrieve user input.  Return true if they input Y/y, false if they input N/n, and loop if any other input
     * is received.
     * @return True if the user indicated a "yes" as their first valid response, false otherwise.
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