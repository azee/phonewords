package ru.azee.phonewords.traversal;

import ru.azee.phonewords.dictionary.DictionaryBuilder;
import ru.azee.phonewords.dictionary.Node;

import java.util.*;

import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;
import static ru.azee.phonewords.utils.StringUtils.normalizeNumbers;

/**
 * Created by azee on 24.10.16.
 */
public class DictTraversal {

    /** Head node of the dictionary trie tree */
    protected Node dictionary;

    /**
     * Constructor for already build dictionary trie tree
     * @param dictionary
     */
    public DictTraversal(Node dictionary) {
        this.dictionary = dictionary;
    }

    /**
     * Constructor if dictionary trie tree is not build yet
     * @param dictionary
     */
    public DictTraversal(List<String> dictionary) {
        this.dictionary = DictionaryBuilder.build(dictionary);
    }

    /**
     * Get a list of possible substitutions grouped by numbers
     * @param numbers - list of numbers
     * @return - map of invariants by numbers
     */
    public Map<String, Set<String>> getValues(List<String> numbers) {
        //Collect invariants grouped by number, filter empty invariants lists
        return numbers.stream().map(numb -> new AbstractMap.SimpleEntry<>(numb, getWords(numb)))
                .filter(entry -> entry.getValue().size() > 0)
                .collect(toMap(entry -> entry.getKey(), entry -> entry.getValue()));

    }

    /**
     *
     * @param numb - list of numbers
     * @return - set of possible substitutions
     */
    private Set<String> getWords(String numb) {
        numb = normalizeNumbers(numb);
        Set<String> values = new HashSet<>();
        List<List<String>> tokensList = new LinkedList<>();
        List<String> tokens = new LinkedList<>();
        tokensList.add(tokens);
        getWords(numb, dictionary, tokensList, values);
        return values;
    }

    /**
     * Initial recursive traversal method to fetch possible substitutions
     * @param numb - telephone number
     * @param head - head of dictionary trie tree
     * @param tokensList - list of possible words variations for current branch
     * @param values - set of all possible values for this number
     */
    private void getWords(String numb, Node head, List<List<String>> tokensList, Set<String> values) {
        if (numb.length() == 0){
            return;
        }
        getWords(numb, head, head.getChildren().get(numb.charAt(0)), new LinkedList<>(tokensList), values);
    }

    /**
     * Recursive method filling up a list of possible substitutions
     * @param numb - telephone number
     * @param head - head of dictionary trie tree
     * @param child - current node of dictionary trie tree
     * @param tokensList - list of possible words variations for current branch
     * @param values - set of all possible values for this number
     */
    private void getWords(String numb, Node head, Node child, List<List<String>> tokensList, Set<String> values) {
        //If there is a letter available for current digit in a dictionary tree path
        if (child == null){
            //No letter found - try to add a digit to tokens if previous one is not a digit
            final Node finalHead = head;
            tokensList = tokensList.stream()
                    .filter(tokens -> finalHead == dictionary && (tokens.size() == 0 || !isNumeric(tokens.get(tokens.size() - 1))))
                    .collect(toList());
            tokensList.forEach(tokens -> tokens.add(String.valueOf(numb.charAt(0))));
            head = dictionary;
        } else {
            //Found a suitable letter - dictionary tree node is head now for current tree path
            head = child;

            //GetMore words if possible even if hit the target word - there may be a longer one
            //down the tree
            getWords(numb.substring(1, numb.length()), head, new LinkedList<>(tokensList), values);

            //If the word if found - add it to tokens and start traversal from the top of the tree
            if (child.getWords().size() > 0){
                tokensList = addValuesToTokens(tokensList, child.getWords());
                head = dictionary;
            }
        }
        //Hit last character in number sequence - job is done
        if (numb.length() == 1 && head == dictionary){
            tokensList.forEach(tokens -> {
                values.add(tokens.stream().collect(joining("-")));
            });
            return;
        }
        getWords(numb.substring(1, numb.length()), head, tokensList, values);
    }

    /**
     * Copy values list and add all words to all tokens
     * @param tokensList - list of tokensList
     * @param words - list of words to add
     * @return - created list of tokens
     */
    private List<List<String>> addValuesToTokens(List<List<String>> tokensList, Set<String > words){
            List<List<String>> result = new LinkedList<>();
        tokensList.stream().forEach(tokens -> {
            words.forEach(word -> {
                List<String> newTokens = new LinkedList<>(tokens);
                newTokens.add(word);
                result.add(newTokens);
            });
        });
        return result;
    }


    /**
     * Identify if a string is a number
     * @param str - input string
     * @return - boolean
     */
    private boolean isNumeric(String str) {
        try {
            Integer.parseInt(str);
        } catch (Exception e){
            return false;
        }
        return true;
    }

}
