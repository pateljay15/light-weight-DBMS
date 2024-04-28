package org.example.dataprocessor.types;

import org.example.dataprocessor.SQLQueryDP;
import org.example.utils.Constant;
import org.example.utils.FileOperations;

import java.io.File;
import java.util.*;

public class InsertQueryDP extends SQLQueryDP {

    List<List<String>> colValues;
    List<String> columns;
    private List<List<String>> records = new ArrayList<>();

    public InsertQueryDP(String table, List<List<String>> colValues) {
        super(table);
        this.colValues = colValues;
    }

    public InsertQueryDP(String table, List<String> columns, List<List<String>> colValues) {
        super(table);
        this.colValues = colValues;
        this.columns = columns;
    }


    /**
     * Executes the primary operation of the Insert query Data Processors.
     * This method contain the core logic that needs Insert query to be executed.
     */
    @Override
    public void run() {
        List<List<String>> actualColumns;
        File file = new File(Constant.ROOT_DB + "/" + FileOperations.getDBName() + "/" + Constant.DATA_DIR + table + ".txt");
        if (columns == null) {
            records.addAll(colValues);
        } else {
            List<List<String>> reorderedColValues = new ArrayList<>();
            Map<String, Integer> reorderedCols = new HashMap<>();
            actualColumns = FileOperations.fetchRecords(Constant.ROOT_DB + "/" + FileOperations.getDBName() + "/" + Constant.META_DIR + table + ".txt");
            for (String col: columns) {
                for(int i=0; i<actualColumns.size(); i++) {
                    String tempCol = actualColumns.get(i).get(0);
                    if (col.equals(tempCol)) {
                        reorderedCols.put(col, i);
                    }
                }
            }
            for(List<String> data: colValues) {
                String[] tempVals = data.toArray(new String[0]);
                for (Map.Entry<String, Integer> set : reorderedCols.entrySet()) {
                    tempVals[set.getValue()] = data.get(set.getValue());
                }
                records.add(Arrays.asList(tempVals));
            }
        }
    }


    /**
     * Commits changes made during the run phase.
     * This method should ensure that all changes to the Database are finalized and saved properly.
     */
    public void commit() {
        File file = new File(Constant.ROOT_DB + "/" + FileOperations.getDBName() + "/" + Constant.DATA_DIR + table + ".txt");
        FileOperations.saveRecords(file, records, true);
    }
}
