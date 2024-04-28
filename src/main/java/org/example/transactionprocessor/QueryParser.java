package org.example.transactionprocessor;

import org.example.authentication.Authentication;
import org.example.dataprocessor.SQLQueryDP;
import org.example.dataprocessor.types.CreateDatabaseQueryDP;
import org.example.dataprocessor.types.CreateQueryDP;
import org.example.dataprocessor.types.InsertQueryDP;
import org.example.dataprocessor.types.SelectQueryDP;
import org.example.exception.QueryException;
import org.example.patternmatchers.PatternMatchers;
import org.example.utils.Constant;
import org.example.utils.FileOperations;

import java.util.*;

public class QueryParser {
    /**
     * Parses a SQL query string and returns an appropriate SQLQueryDP object based on the query type.
     *
     * @param query The SQL query string to be parsed.
     * @return SQLQueryDP instance representing the parsed query. Returns null if the query type is not supported.
     */
    public static SQLQueryDP parseQuery(String query) {
        String[] tokens = query.split("\\s+");
        if(tokens[0].equalsIgnoreCase("create")) return createTableQueryParser(query);
        else if (tokens[0].equalsIgnoreCase("insert")) return insertQueryParser(query);
        else if (tokens[0].equalsIgnoreCase("select")) return selectQueryParser(query);
        else throw new IllegalArgumentException("Invalid SQL keyword: " + tokens[0]);

    }


    /**
     * Parses an INSERT SQL query string and returns an InsertQueryDP object.
     *
     * @param query The INSERT SQL query string to be parsed.
     * @return InsertQueryDP instance populated with the parsed query information.
     */
    private static InsertQueryDP insertQueryParser(String query) {
        List<String> matches = PatternMatchers.getPatternMatcher("insert", query);
        List<List<String>> colValues = new ArrayList<>();
        String table = matches.get(0);
        if(matches.size() > 2) {
            List<String> columns = new ArrayList<>();
            String[] extractColumns = matches.get(1).split(",");
            columns.addAll(Arrays.asList(extractColumns));

            for(int i=2; i< matches.size(); i++) {
                int startIdx = matches.get(i).indexOf("(")+1;
                int endIdx = matches.get(i).indexOf(")");
                String[] extractColumnsValues = matches.get(i).substring(startIdx, endIdx).split(",");
                colValues.add(Arrays.asList(extractColumnsValues));
            }
            FileOperations.writeToCsv(Constant.ROOT_DB + "/" + Constant.LOG_FILE + ".csv", matches.get(2), query, Authentication.getAuthContext());
            return new InsertQueryDP(table, columns, colValues);
        } else if (matches.size() == 2) {
            for(int i=1; i< matches.size(); i++) {
                int startIdx = matches.get(i).indexOf("(")+1;
                int endIdx = matches.get(i).indexOf(")");
                String[] extractColumnsValues = matches.get(i).substring(startIdx, endIdx).split(",");
                colValues.add(Arrays.asList(extractColumnsValues));
            }
            FileOperations.writeToCsv(Constant.ROOT_DB + "/" + Constant.LOG_FILE + ".csv", matches.get(1), query, Authentication.getAuthContext());
            return new InsertQueryDP(table, colValues);
        } else throw new QueryException("Invalid SQL Insert Query..");
    }


    /**
     * Parses a SELECT SQL query string and returns a SelectQueryDP object.
     *
     * @param query The SELECT SQL query string to be parsed.
     * @return SelectQueryDP instance populated with the parsed query information.
     */
    private static SelectQueryDP selectQueryParser(String query) {
        List<String> matches = PatternMatchers.getPatternMatcher("select", query);
        String table = matches.get(1);
        FileOperations.writeToCsv(Constant.ROOT_DB + "/" + Constant.LOG_FILE + ".csv", "no data", query, Authentication.getAuthContext());
        if(matches.get(0).equals("*")) {
            if(matches.size() == 3) {
                String[] condition = matches.get(2).split("=");
                return new SelectQueryDP(table, matches.get(0), Arrays.asList(condition));
            } else {
                return new SelectQueryDP(table, matches.get(0));
            }
        } else if (matches.size() == 3) {
            String[] cols = matches.get(0).split(",");
            String[] condition = matches.get(2).split("=");
            return new SelectQueryDP(table, Arrays.asList(cols), Arrays.asList(condition));
        } else{
            String[] cols = matches.get(0).split(",");
            return new SelectQueryDP(table, Arrays.asList(cols));
        }
    }


    /**
     * Parses a CREATE TABLE SQL query string and returns a CreateQueryDP object.
     *
     * @param query The CREATE TABLE SQL query string to be parsed.
     * @return CreateQueryDP instance populated with the parsed query information.
     */
    private static CreateQueryDP createTableQueryParser(String query) {
        List<List<String >> colDatatypeRecords = new ArrayList<>();
        String table = query.split("\\s+")[2];
        int startCol = query.indexOf("(")+2;
        int endCol = query.indexOf(")");
        String subString = query.substring(startCol, endCol);
        String[] colDatatypes = subString.split(",\\n");
        List<String> queryParts = Arrays.asList(colDatatypes);
        for(String queryPart: queryParts) {
            List<String> colDatatype = new ArrayList<>();
            colDatatype.add(queryPart.split("\\s+")[0]);
            colDatatype.add(queryPart.split("\\s+")[1]);
            colDatatypeRecords.add(colDatatype);
        }
        FileOperations.writeToCsv(Constant.ROOT_DB + "/" + Constant.LOG_FILE + ".csv", "no data", query, Authentication.getAuthContext());
        return new CreateQueryDP(table, colDatatypeRecords);
    }


    /**
     * Parses a CREATE DATABASE SQL query string and returns a CreateDatabaseQueryDP object.
     *
     * @param query The CREATE DATABASE SQL query string to be parsed.
     * @return CreateDatabaseQueryDP instance populated with the parsed query information.
     */
    public static CreateDatabaseQueryDP createDatabaseQuery(String query) {
        String[] tokens = query.split(";")[0].split("\\s+");
        return new CreateDatabaseQueryDP(tokens[2]);
    }
}
