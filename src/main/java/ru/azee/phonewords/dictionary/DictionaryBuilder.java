package ru.azee.phonewords.dictionary;

import java.util.List;

import static ru.azee.phonewords.utils.StringUtils.normalizeWords;

/**
 * Created by azee on 24.10.16.
 */
public class DictionaryBuilder {

    private final static NumbersMap numbersMap = new NumbersMap();

    /**
     * Build a trie tree out of the words list
     * @param words - list of words in dictionary
     * @return - head of the trie-tree
     */
    public static Node build(List<String> words){
        Node head = new Node();
        words.forEach(word -> addToTree(word, head));
        return head;
    }

    /**
     * Traverse down the tree creating nodes on the way
     * @param word - current word
     * @param head - head of the trie tree
     */
    private static void addToTree(String word, Node head) {
        word = normalizeWords(word);
        for (char character : word.toCharArray()){
            head = head.initChild(numbersMap.getDict().get(character));
        }
        head.getWords().add(word);
    }
}
