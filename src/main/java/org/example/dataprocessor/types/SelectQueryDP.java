package org.example.dataprocessor.types;

import org.example.dataprocessor.SQLQueryDP;
import org.example.utils.Constant;
import org.example.utils.FileOperations;

import javax.swing.*;
import java.io.File;
import java.util.*;

public class  SelectQueryDP extends SQLQueryDP {

    public String all;
    public List<String> columns = new ArrayList<>();
    public List<String> conditions = new ArrayList<>();
    public SelectQueryDP(String table, String all) {
        super(table);
        this.all = all;
    }
    public SelectQueryDP(String table, String all, List<String> conditions) {
        super(table);
        this.all = all;
        this.conditions = conditions;
    }
    public SelectQueryDP(String table, List<String> columns) {
        super(table);
        this.columns = columns;
    }
    public SelectQueryDP(String table, List<String> columns, List<String> conditions) {
        super(table);
        this.columns = columns;
        this.conditions = conditions;
    }


    /**
     * Executes the primary operation of the Select query Data Processors.
     * This method contain the core logic that needs Select query to be executed.
     */
    @Override
    public void run() {
        File file = new File(Constant.ROOT_DB + "/" + FileOperations.getDBName() + "/" + Constant.DATA_DIR + table + ".txt");
        if (all != null) {
            List<List<String>> metaRecords = FileOperations.fetchRecords(Constant.ROOT_DB + "/" + FileOperations.getDBName() + "/" + Constant.META_DIR + table + ".txt");
            List<List<String>> records = FileOperations.fetchRecords(Constant.ROOT_DB + "/" + FileOperations.getDBName() + "/" + Constant.DATA_DIR + table + ".txt");
            System.out.println("Table: "+table);
            for(List<String> metaRecord: metaRecords) {
                System.out.print(metaRecord.get(0));
                System.out.print("\t");
            }
            System.out.println();
            if (!conditions.isEmpty()) {
                List<String> actualColumns = new ArrayList<>();
                List<String> colDatatype = new ArrayList<>();
                for (int i = 0; i < metaRecords.size(); i++) {
                    List<String> metaRecord = metaRecords.get(i);
                    actualColumns.add(metaRecord.get(0));
                    colDatatype.add(metaRecord.get(1));
                }
                int matchingIdx = 2;
                for (int i = 0; i < actualColumns.size(); i++) {
                    if (Objects.equals(conditions.get(0), actualColumns.get(i))) {
                        matchingIdx = i;
                    }
                }

                List<List<String>> finalRecord = new ArrayList<>();
                for (List<String> record : records) {
                    if (Objects.equals(colDatatype.get(matchingIdx), "int") && Integer.parseInt(record.get(matchingIdx)) == Integer.parseInt(conditions.get(1))) {
                        finalRecord.add(record);
                    } else if (Objects.equals(colDatatype.get(matchingIdx), "varchar") && Objects.equals(record.get(matchingIdx), conditions.get(1))) {
                        finalRecord.add(record);
                    }
                }
                for(List<String> record: finalRecord) {
                    for(String col: record) {
                        System.out.print(col + "\t");
                    }
                    System.out.println();
                }
            } else {
                for(List<String> record: records) {
                    for(String col: record) {
                        System.out.print(col + "\t");
                    }
                    System.out.println();
                }
            }
        } else {
            List<List<String>> metaRecords = FileOperations.fetchRecords(Constant.ROOT_DB + "/" + FileOperations.getDBName() + "/" + Constant.META_DIR + table + ".txt");
            List<List<String>> records = FileOperations.fetchRecords(Constant.ROOT_DB + "/" + FileOperations.getDBName() + "/" + Constant.DATA_DIR + table + ".txt");
            List<String> printFlag = new ArrayList<>();
            for(List<String> metaRecord: metaRecords) {
                printFlag.add("no");
            }
            System.out.println("Table: "+table);
            for (int i = 0; i < metaRecords.size(); i++) {
                List<String> metaRecord = metaRecords.get(i);
                for (int j = 0; j < columns.size(); j++) {
                    String matchCol = columns.get(j);
                    if (metaRecord.get(0).equals(matchCol)) {
                        printFlag.add(i, "yes");
                        System.out.print(metaRecord.get(0));
                        System.out.print("\t");
                    }
                }
            }
            System.out.println();

            if (!conditions.isEmpty()) {
//                System.out.println("hello");
                List<String> actualColumns = new ArrayList<>();
                List<String> colDatatype = new ArrayList<>();
                for (int i = 0; i < metaRecords.size(); i++) {
                    List<String> metaRecord = metaRecords.get(i);
                    actualColumns.add(metaRecord.get(0));
                    colDatatype.add(metaRecord.get(1));
                }
                int matchingIdx = 2;
                for(int i=0; i<actualColumns.size(); i++) {
                    if(Objects.equals(conditions.get(0), actualColumns.get(i))) {
                        matchingIdx = i;
                    }
                }

                List<List<String>> finalRecord = new ArrayList<>();
                for(List<String> record: records) {
                    if (Objects.equals(colDatatype.get(matchingIdx), "int") && Integer.parseInt(record.get(matchingIdx)) == Integer.parseInt(conditions.get(1))) {
                        finalRecord.add(record);
                    } else if(Objects.equals(colDatatype.get(matchingIdx), "varchar") && Objects.equals(record.get(matchingIdx), conditions.get(1))){
                        finalRecord.add(record);
                    }
                }
                for(List<String> record: finalRecord) {
                    int i=0;
                    for(String col: record) {
                        if(printFlag.get(i) == "yes") {
                            System.out.print(col + "\t");
                        }
                        i++;
                    }
                    System.out.println();
                }
            } else {
                for(List<String> record: records) {
                    int i=0;
                    for(String col: record) {
                        if(printFlag.get(i) == "yes") {
                            System.out.print(col + "\t");
                        }
                        i++;
                    }
                    System.out.println();
                }
            }
        }
    }


    /**
     * Commits changes made during the run phase.
     * This method should ensure that all changes to the Database are finalized and saved properly.
     */
    public void commit() {
        super.commit();
    }
}