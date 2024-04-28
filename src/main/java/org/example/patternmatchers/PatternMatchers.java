package org.example.patternmatchers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PatternMatchers {

    public String queryType;
    public String matchQuery;

    public PatternMatchers() {}
    public PatternMatchers(String queryType, String matchQuery) {
        this.queryType = queryType;
        this.matchQuery = matchQuery;
    }


    /**
     * Searches for patterns within a given string based on the specified sql query type.
     * This method uses regular expressions to find matches according to the sql query type.
     *
     * @param queryType The type of sql query which defines the pattern to search for.
     * @param matchQuery The string within which to search for patterns.
     * @return A list of strings that match the pattern for the given sql query type.
     *         Returns an empty list if no matches are found or if the sql query type is not recognized.
     */
    public static List<String> getPatternMatcher (String queryType, String matchQuery) {
        if (queryType.equals("insert")) {
            String regex = "INSERT\\s+INTO\\s+(\\w+)\\s+(?:\\((.*)\\))?\\s*VALUES\\s+(.*);";
            Pattern pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
            Matcher matcher = pattern.matcher(matchQuery);
            List<String> matches  = new ArrayList<>();
            if (matcher.find()) {
                for (int i = 1; i <= matcher.groupCount(); i++) {
                    if (matcher.group(i) != null) {
                        matches.add(matcher.group(i));
                    }
                }
            }
            return matches;
        }
        else if (queryType.equals("select")) {
            String regex = "SELECT\\s+(.*)\\s+FROM\\s+(\\w+)(?:\\s+WHERE\\s+(.*))?;";
            Pattern pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
            Matcher matcher = pattern.matcher(matchQuery);
            List<String> matches  = new ArrayList<>();
            if (matcher.find()) {
                for (int i = 1; i <= matcher.groupCount(); i++) {
                    if (matcher.group(i) != null) {
                        matches.add(matcher.group(i));
                    }
                }
            }
            return matches;
        }
        else{
            throw new IllegalArgumentException("Invalid SQL keyword: " + queryType);
        }
    }

}
