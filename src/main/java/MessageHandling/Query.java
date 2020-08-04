package MessageHandling;

//This enum represents the different categories of commands that this bot can handle.  They are used
//as keys that are mapped to a set of QueryHandlers that are sent that category of commands.
public enum Query {
    WOLFRAM,
    BROADCAST,
    ROLE
}
