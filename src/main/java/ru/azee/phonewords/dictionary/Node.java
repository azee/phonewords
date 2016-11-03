package ru.azee.phonewords.dictionary;


import java.util.*;

/**
 * Created by azee on 24.10.16.
 */
public class Node {
    private Set<String> words;
    private Map<Character, Node> children;

    public Set<String> getWords() {
        if (words == null){
            words = new HashSet<>();
        }
        return words;
    }

    public Map<Character, Node> getChildren() {
        if (children == null){
            children = new HashMap<>();
        }
        return children;
    }

    public Node initChild(Character character) {
        Node child = getChildren().get(character);
        if (child == null){
            child = new Node();
            getChildren().put(character, child);
        }
        return child;
    }
}
