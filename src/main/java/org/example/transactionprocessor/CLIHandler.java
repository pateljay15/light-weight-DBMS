package org.example.transactionprocessor;

import org.example.dataprocessor.SQLQueryDP;
import org.example.dataprocessor.types.CreateDatabaseQueryDP;
import org.example.utils.Constant;
import org.example.utils.FileOperations;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class CLIHandler {
    /**
     * Starts the application, handles user input for database operations,
     * supports transaction management, and exits the application.
     * This is the main entry point for a lightweight DBMS system.
     */
    public static void start() {

        Scanner sc = new Scanner(System.in);

        FileOperations.createDatabaseIfNotExists(Constant.ROOT_DB);
        String DB_NAME = FileOperations.getDBName();

        if(DB_NAME == null) {
            System.out.println("Start by creating Database: ");
            String createDBQuery = sc.nextLine();
            CreateDatabaseQueryDP parseQuery = QueryParser.createDatabaseQuery(createDBQuery);
            parseQuery.run();
            DB_NAME = FileOperations.getDBName();
        }

        while (true) {
            List<String> query = new ArrayList<>();
            List<String> tranQueries = new ArrayList<>();
            int transactionFlag = 0;
            boolean exitFalg = false;
            System.out.print("("+DB_NAME+")> ");
            while (true) {
                String temp;
                temp = sc.nextLine();
                if(temp.equalsIgnoreCase("commit")) {
                    tranQueries.add("commit");
                    break;
                }
                if (temp.equalsIgnoreCase("rollback")) {
                    tranQueries.add("rollback");
                    break;
                }
                if(temp.equalsIgnoreCase("begin transaction")) {
                    transactionFlag = 1;
                }
                if(temp.equals("exit")) {
                    String authContextLoc = Constant.ROOT_DB + "/" + Constant.AUTH_CONTEXT_FILE + ".txt";
                    try {
                        FileWriter writer = new FileWriter(authContextLoc, false);
                        PrintWriter pw = new PrintWriter(writer, false);
                        pw.flush();
                        pw.close();
                        writer.close();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    exitFalg = true;
                    break;
                }
                if(!temp.equalsIgnoreCase("begin transaction")) {
                    if(transactionFlag == 1) {
                        tranQueries.add(temp);
                    } else {
                        query.add(temp);
                    }
                }
                if(temp.endsWith(";") && transactionFlag == 0) {
                    break;
                }
                System.out.print("-> ");
            }
            if(exitFalg) break;

            String queryString;
            if (query.size() > 1) {
                queryString = String.join("\n", query);
                SQLQueryDP parseQuery = QueryParser.parseQuery(queryString);
                parseQuery.run();
            } else {
                if(transactionFlag == 1) {
                    TransactionHandler.handleTransaction(tranQueries);
                } else {
                    queryString = query.get(0);
                    SQLQueryDP parseQuery = QueryParser.parseQuery(queryString);
                    parseQuery.run();
                    parseQuery.commit();
                }
            }
        }

        System.out.println("LightWeight DBMS system exited...");

    }
}