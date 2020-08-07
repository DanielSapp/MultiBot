# DiscordBot
MultiBot is a multi-functional Discord bot built using the JDA API.  Its current functionality includes managing user roles, querying the Wolfram Alpha simple response API, and retrieving stock market quotes from the IEX cloud trading platform.  It was written with extensibility in mind to make adding new functionality simple and limitless.

# Getting started
When the project is first run from the command line it will guide you through the initialization process.  At a minimum you will need a bot token from the Discord developer dashboard, and other API keys will be requested depending on the functionality that you enable.  You will be prompted to save a configuration file at the end of the process that will prevent you from needing to do this again.

# Commands

The following commands are currently supported, although the bot will only respond to commands that have been enabled in its current configuration:

!addrole/!addrank [arg] - Add the role named arg to the current user.  Print errors if a role with that name cannot be found, if it would give the user new permissions, or if the user already has it.

!removerole/!removerank [arg] - Remove the role named arg from the current user.  Print error if a role with that name cannot be found or if the user does not have it.

!createrole/!createrank [arg] - Create a new role named arg.  Print error if the requesting user does not have permissions to modify roles or if a role by that name already exists.

!deleterole/!deleterank [arg] - Delete the role named arg.  Print error if the requesting user does not have permissions to modify roles or if a role by that name cannot be found.

!listroles/!listranks - List all roles in the current Guild.

!wolfram [arg] - Query the Wolfram Alpha Simple Response API with arg and print the response.

!stock [arg] - Query the IEX API for information about the ticker named arg.  Print error if a ticker by that name could not be found.
