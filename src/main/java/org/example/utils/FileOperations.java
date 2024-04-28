package org.example.utils;

import org.example.exception.FileOperationException;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class FileOperations {

    /**
     * Retrieves the name of the database. It will look out for a file at the runtime.
     * that file is the database and it will return the name of that file.
     *
     * @return The name of the database as a {@code String}. Returns {@code null} if the file is not present.
     */
    public static String getDBName() {
        File file = new File(Constant.ROOT_DB);
        String DB_NAME = null;
        if(file.isDirectory()) {
            String[] directories = file.list();
            if(directories.length > 0) {
                for (int i=0; i<directories.length; i++) {
                    File temp = new File(file, directories[i]);
                    if(temp.isDirectory()) {
                        DB_NAME = directories[i];
                        return DB_NAME;
                    }
                }
            }
        }
        return DB_NAME;
    }


    /**
     * Checks if a database exists at the specified path and creates it if it does not.
     * This method assumes a file-based database system where each database is represented by a directory.
     *
     * @param path The filesystem path where the database should be created. This path represents the database.
     * @throws FileOperationException If an error occurs during the creation of the database directory.
     */
    public static void createDatabaseIfNotExists (String path) {
        File directory = new File(path);

        if(!directory.exists()) {
            boolean created = directory.mkdirs();

            if(!created) {
                throw new FileOperationException("Error while creating directory");
            }
        }
    }


    /**
     * Saves a list of records to the specified file.
     * Each record is a list of strings, allowing for flexible data structures.
     * The method can either append to the file or overwrite it based on the {@code appendOrNot} parameter.
     *
     * @param file The file where records will be saved. Must not be {@code null}.
     * @param records The list of records to save. Each record is itself a list of strings.
     * @param appendOrNot If {@code true}, records will be appended to the file; if {@code false}, the file will be overwritten.
     * @throws FileOperationException If an I/O error occurs during writing to the file.
     */
    public static void saveRecords(File file, List<List<String>> records, boolean appendOrNot) {
        try {
            FileWriter fileWriter = new FileWriter(file, appendOrNot);
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);

            for(List<String> record: records) {
                String row = String.join(Constant.DELIMINATOR, record);
                bufferedWriter.write(row);
                bufferedWriter.newLine();
            }

            bufferedWriter.close();
            fileWriter.close();
        } catch (IOException e) {
            throw new FileOperationException("Error occurred while saving records: "+ e);
        }
    }


    /**
     * Reads records from the specified file and returns them as a list of lists of strings.
     * Each line in the file is considered a separate record, and each record is split into strings based on a delimiter (e.g.; semicolon for txt files).
     *
     * @param filepath The path to the file from which records will be read.
     * @return A list of records, with each record being a list of strings.
     * @throws FileOperationException If an I/O error occurs during reading from the file.
     */
    public static List<List<String>> fetchRecords(String filepath) {
        List<List<String>> records = new ArrayList<>();
        FileReader fileReader;
        try {
            fileReader = new FileReader(filepath);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            String line;

            while ((line = bufferedReader.readLine()) != null) {
                List<String> listRow = Arrays.asList(line.split(Constant.DELIMINATOR));
                records.add(listRow);
            }
            bufferedReader.close();
            fileReader.close();
        } catch (IOException e) {
            throw new FileOperationException("File not found or IO error");
        }
        return records;
    }


    /**
     * Writes a line of data to a CSV file at the specified filepath. The line is composed of the provided data, query, and userinfo,
     * separated by commas. If the file does not exist, it will be created. If it exists, the line will be appended to the end.
     *
     * @param filepath The path to the CSV file where the data will be written.
     * @param data The main data to write to the file. This could be a result set, a message, or any relevant data.
     * @param query The SQL query or operation related to the data.
     * @param userinfo Additional user information, such as a username, who has executed this query.
     * @throws IOException If an I/O error occurs during writing to the file.
     */
    public static void writeToCsv (String filepath, String data, String query, String userinfo) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(filepath, true))) {
            String removeNewLine = data.replace("\n", " ");
            bw.write(removeNewLine.replace(",", ";") + "," + query.replace(",",";") + "," + userinfo + "," + new Date());
            bw.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
