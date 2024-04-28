package org.example;

import org.example.authentication.Authentication;
import org.example.transactionprocessor.CLIHandler;

public class Main {

    /**
     * The entry point of the application. This method initiates user authentication and,
     * upon successful authentication, starts the command line interface (CLI) handler to handle SQL queries.
     * The process is as follows:
     * 1. Attempt to authenticate the user using {@code Authentication.authenticateUser()}.
     * 2. Continuously check the authentication context using {@code Authentication.checkAuthContext()}.
     *    If the user is not authenticated, repeat the authentication process.
     * 3. Once authenticated, proceed to start the CLI handling process with {@code CLIHandler.start()}.
     *
     * @param args The array of command-line arguments passed to the application. Currently, this application
     *             does not utilize command-line arguments, but they can be added to extend functionality.
     */
    public static void main(String[] args) {
        Authentication.authenticateUser();
        while (true) {
            if(Authentication.checkAuthContext()) {
                break;
            }
            Authentication.authenticateUser();
        }
        CLIHandler.start();

    }
}