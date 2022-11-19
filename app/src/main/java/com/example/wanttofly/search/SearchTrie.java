package com.example.wanttofly.search;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class SearchTrie {
    private final Map<Character, SearchTrie> children;
    private final Set<Integer> indexes;

    SearchTrie() {
        children = new HashMap<>();
        indexes = new HashSet<>();
    }

    public void addIndex(int indexInList) {
        this.indexes.add(indexInList);
    }

    public Set<Integer> getIndexes() {
        return this.indexes;
    }

    // Iterative function to insert a string into a Trie
    public void insert(FlightSummaryData flightInfo, int indexInList) {
        SearchTrie currentNodeForArrival = this;
        SearchTrie currentNodeForAirline = this;
        String arrivalAirport = flightInfo.arrivalAirport;
        String airline = flightInfo.airlineName;

        for (char c: arrivalAirport.toCharArray()) {
            char lowerC = Character.toLowerCase(c);
            currentNodeForArrival.children.putIfAbsent(lowerC, new SearchTrie());
            currentNodeForArrival = currentNodeForArrival.children.get(lowerC);
            currentNodeForArrival.addIndex(indexInList);
        }
        for (char c: airline.toCharArray()) {
            char lowerC = Character.toLowerCase(c);
            currentNodeForAirline.children.putIfAbsent(lowerC, new SearchTrie());
            currentNodeForAirline = currentNodeForAirline.children.get(lowerC);
            currentNodeForAirline.addIndex(indexInList);
        }
    }

    public SearchTrie search(String key) {
        SearchTrie curr = this;

        for (char c: key.toCharArray()) {
            curr = curr.children.get(c);

            // if the string is invalid (reached end of a path in the Trie)
            if (curr == null) {
                return null;
            }
        }

        return curr;
    }
}
