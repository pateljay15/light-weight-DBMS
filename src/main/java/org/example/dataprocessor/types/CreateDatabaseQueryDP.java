package org.example.dataprocessor.types;

import org.example.utils.Constant;
import org.example.utils.FileOperations;

public class CreateDatabaseQueryDP {

    String DB_NAME;
    public CreateDatabaseQueryDP(String DB_NAME) {
        this.DB_NAME = DB_NAME;
    }


    /**
     * Executes the primary operation of the Create Database query Data Processors.
     * This method contain the core logic that needs Create Database query to be executed.
     */
    public void run() {
        FileOperations.createDatabaseIfNotExists(Constant.ROOT_DB + "/" + DB_NAME + "/" + Constant.META_DIR);
        FileOperations.createDatabaseIfNotExists(Constant.ROOT_DB + "/" + DB_NAME + "/" + Constant.DATA_DIR);
    }
}
