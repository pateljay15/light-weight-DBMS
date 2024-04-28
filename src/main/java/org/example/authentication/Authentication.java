package org.example.authentication;

import org.example.utils.Constant;

import java.io.*;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.UUID;

public class Authentication {

    /**
     * Handles the process of authenticating a user. This method prompts the user to either signup or login
     * , and depending on the choice specified by the user, it proceeds with either signup or login.
     * Verifies these credentials, and then either grants or denies access based on the verification result.
     * The method interact with a file to validate user credentials.
     */
    public static void authenticateUser() {
        System.out.println("Already have an Account ? Login.. Press 1");
        System.out.println("Register.. Press 2");
        Scanner sc = new Scanner(System.in);
        int choice = sc.nextInt();
        boolean authenticate = false;
        if (choice == 1) {
            authenticate = login();
        } else if(choice == 2) {
            authenticate = registerUser();
        }
        else throw new RuntimeException("Incorrect choice entered..");

        if(authenticate) {
            System.out.print("");
        } else {
            System.out.println("Authentication Failed");
            System.exit(1);
        }
    }


    /**
     * Checks if there is an existing authentication context for the user.
     * This method determines whether the user is currently authenticated by verifying
     * the presence of user information.
     *
     * @return {@code true} if the user is authenticated (an authentication context exists),
     *         {@code false} otherwise.
     */
    public static boolean checkAuthContext() {
        String fileLocation = Constant.ROOT_DB + "/" + Constant.AUTH_CONTEXT_FILE + ".txt";
        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader(fileLocation));
            String line = bufferedReader.readLine();
            if(line != null) {
                System.out.println("Welcome: "+line);
                return true;
            } else {
                System.out.println("Please Login to access..");
                return false;
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    /**
     * Retrieves the current authentication context for the user.
     * This method is responsible for fetching the user information.
     *
     * The returned string represent a username.
     *
     * @return A {@code String} representing the user's authentication context. If no authentication
     *         context is currently established, or if the user is not authenticated, this method
     *         returns {@code null} or an empty string.
     */
    public static String getAuthContext() {
        String fileLocation = Constant.ROOT_DB + "/" + Constant.AUTH_CONTEXT_FILE + ".txt";
        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader(fileLocation));
            String line = bufferedReader.readLine();
            if(line != null) {
                return line;
            } else {
                System.out.println("Please Login to access..");
                return "";
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    /**
     * Collects user login details including the verification of a captcha challenge.
     * This method is intended to be used as part of the authentication process, ensuring that
     * login attempts are accompanied by successful captcha verification to protect against automated attacks.
     *
     * @return A list of strings containing the login details. The list typically includes the username,
     *         password, and the captcha response provided by the user. It will ask user
     *         to enter captcha again and again util user inputs correct captcha.
     */
    private static List<String> getLoginDetails() {
        List<String> authDetails = new ArrayList<>();
        Scanner sc = new Scanner(System.in);
        System.out.print("> username: ");
        String username = sc.nextLine();
        authDetails.add(username);

        System.out.print("> password: ");
        String password = sc.nextLine();
        String hashedPassword = convertRawToHashForm(password);
        authDetails.add(hashedPassword);

        while(true) {
            String captcha = UUID.randomUUID().toString().substring(0,6);
            System.out.print("Enter captcha: "+captcha+": ");
            String inputCaptcha = sc.nextLine();

            if (captcha.equals(inputCaptcha)) {
                break;
            }else {
                System.out.println("Incorrect Captcha");
            }
        }

        return authDetails;
    }


    /**
     * Registers a new user with their credentials, including hashing the password using MD5.
     * This method use the getLoginDetails method to prompts the user for their registration details,
     * hashes the password, and stores the user information.
     *
     * @return {@code true} if the user was successfully registered; {@code false} otherwise.
     */
    private static boolean registerUser() {
        try {
            List<String> authDetails = getLoginDetails();
            String username = authDetails.get(0);
            String password = authDetails.get(1);

            String fileLocation = Constant.ROOT_DB + "/" + Constant.AUTH_FILE + ".txt";
            FileWriter writer = new FileWriter(fileLocation, true);
            writer.write(username + Constant.DELIMINATOR + convertRawToHashForm(password) + "\n");
            writer.close();
            System.out.println("registered successfully.");
            return true;
        } catch (Exception e) {
            System.out.println("Error signing up: " + e.getMessage());
            return false;
        }
    }


    /**
     * Authenticates a user based on username and password input.
     * The method hashes the input password using MD5 and compares it with the stored hash for authentication.
     *
     * @return {@code true} if the user is successfully authenticated; {@code false} otherwise.
     */
    private static boolean login() {
        List<String> authDetails = getLoginDetails();
        String username = authDetails.get(0);
        String password = authDetails.get(1);

        String fileLocation = Constant.ROOT_DB + "/" + Constant.AUTH_FILE + ".txt";
        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader(fileLocation));
            String line = "";
            while ((line = bufferedReader.readLine()) != null) {
                String[] user = line.split(Constant.DELIMINATOR);
                if(user[0].equals(username) && user[1].equals(convertRawToHashForm(password))) {
                    System.out.println("Login Successfull..");
                    String authContextLoc = Constant.ROOT_DB + "/" + Constant.AUTH_CONTEXT_FILE + ".txt";
                    FileWriter writer = new FileWriter(authContextLoc, true);
                    writer.write(username);
                    writer.close();
                    return true;
                }
            }
            System.out.println("Incorrect Username or Password..");
            return false;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    /**
     * Converts a raw password string into its hashed form using a Message Digest algorithm (MD5).
     *
     * @param password The raw password string to be hashed.
     * @return The hashed form of the password as a hexadecimal string. If an error occurs
     *         during hashing, a RuntimeException is thrown.
     */
    private static String convertRawToHashForm(String password){
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] bytesOfMessage = password.getBytes();
            byte[] digest = md.digest(bytesOfMessage);

            StringBuilder sb = new StringBuilder();
            for (byte b : digest) {
                sb.append(String.format("%02x", b));
            }
            String hashValue = sb.toString();
            return hashValue;
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }
}
