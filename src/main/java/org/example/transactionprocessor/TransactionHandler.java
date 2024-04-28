package org.example.transactionprocessor;

import org.example.authentication.Authentication;
import org.example.dataprocessor.SQLQueryDP;
import org.example.dataprocessor.types.CreateQueryDP;
import org.example.dataprocessor.types.InsertQueryDP;
import org.example.dataprocessor.types.SelectQueryDP;
import org.example.utils.Constant;
import org.example.utils.FileOperations;

import java.util.ArrayList;
import java.util.List;

public class TransactionHandler {

    /**
     * Processes a list of transaction-related SQL queries within a single transaction.
     * This method assumes all queries are part of a single transaction and should be either all committed if successful
     * or rolled back in case of any failure.
     *
     * @param tranQueries A list of SQL query strings that represent the transaction. This list may include
     *                    SQL operations such as INSERT, UPDATE, DELETE, and should end with a "commit" or "rollback" command
     *                    to indicate the desired outcome of the transaction.
     */
    public static void handleTransaction(List<String> tranQueries) {
        FileOperations.writeToCsv(Constant.ROOT_DB + "/" + Constant.LOG_FILE + ".csv", "no data", "begin transaction", Authentication.getAuthContext());
        List<SQLQueryDP> sqlQueryDPList = new ArrayList<>();
        for(String query:tranQueries) {
            if(query.equals("commit") || query.equals("rollback")) {
                continue;
            }
            SQLQueryDP parseQuery = QueryParser.parseQuery(query);
            if(query.split(" ")[0].equalsIgnoreCase("insert")) {
                parseQuery.run();
            }
            sqlQueryDPList.add(parseQuery);
        }
        if(tranQueries.contains("commit")) {
            for(SQLQueryDP sqlQueryDP:sqlQueryDPList) {
                if(sqlQueryDP instanceof SelectQueryDP) {
                    sqlQueryDP.run();
                } else if(sqlQueryDP instanceof InsertQueryDP) {
                    sqlQueryDP.commit();
                }
            }
            FileOperations.writeToCsv(Constant.ROOT_DB + "/" + Constant.LOG_FILE + ".csv", "no data", "commit", Authentication.getAuthContext());
            System.out.println("successfully committed..");
        } else if(tranQueries.contains("rollback")) {
            FileOperations.writeToCsv(Constant.ROOT_DB + "/" + Constant.LOG_FILE + ".csv", "no data", "rollback", Authentication.getAuthContext());
            System.out.println("successfully rollback..");
        }
    }
}
