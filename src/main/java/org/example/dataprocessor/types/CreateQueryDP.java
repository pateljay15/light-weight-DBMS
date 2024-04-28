package org.example.dataprocessor.types;

import org.example.dataprocessor.SQLQueryDP;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.example.exception.FileOperationException;
import org.example.utils.Constant;
import org.example.utils.FileOperations;

public class CreateQueryDP extends SQLQueryDP {
    List<List<String>> metaData;

    public CreateQueryDP(String table, List<List<String>> metaData) {
        super(table);
        this.metaData = metaData;
    }


    /**
     * Executes the primary operation of the Create query Data Processors.
     * This method contain the core logic that needs Create query to be executed.
     */
    @Override
    public void run() {
        File metaFile = new File(Constant.ROOT_DB + "/" + FileOperations.getDBName() + "/" + Constant.META_DIR + table + ".txt");
        File dataFile = new File(Constant.ROOT_DB + "/" + FileOperations.getDBName() + "/" + Constant.DATA_DIR + table + ".txt");

        try {
            metaFile.createNewFile();
            dataFile.createNewFile();
            FileOperations.saveRecords(metaFile, metaData, false);
        } catch (IOException e) {
            throw new FileOperationException("Error occurred while creating "+ table + " table");
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
