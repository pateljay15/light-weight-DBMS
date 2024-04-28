package org.example.dataprocessor;

public abstract class SQLQueryDP {
    public String table;

    public SQLQueryDP(String table) {
        this.table = table;
    }


    /**
     * Executes the primary operation of the Data Processors process.
     * This method should contain the core logic that needs to be executed.
     */
    public void run() {}


    /**
     * Commits changes made during the run phase.
     * This method should ensure that all changes to the Database are finalized and saved properly.
     */
    public void commit() {}
}
