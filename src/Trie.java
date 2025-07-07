
package com.trainreservation;

import java.util.HashMap;
import java.util.Map;

class TrieNode {
    Map<Character, TrieNode> children;
    boolean isEndOfWord;
    Station station;

    public TrieNode() {
        children = new HashMap<>();
        isEndOfWord = false;
        station = null;
    }
}

public class Trie {
    private TrieNode root;

    public Trie() {
        root = new TrieNode();
    }

    public void insert(Station station) {
        TrieNode current = root;
        for (char c : station.getStationName().toLowerCase().toCharArray()) {
            current.children.putIfAbsent(c, new TrieNode());
            current = current.children.get(c);
        }
        current.isEndOfWord = true;
        current.station = station;
    }

    public Station search(String stationName) {
        TrieNode current = root;
        for (char c : stationName.toLowerCase().toCharArray()) {
            if (!current.children.containsKey(c)) {
                return null;
            }
            current = current.children.get(c);
        }
        return current.isEndOfWord ? current.station : null;
    }
}
