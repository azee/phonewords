package ru.azee.phonewords.traversal;

import ru.azee.phonewords.dictionary.DictionaryBuilder;
import ru.azee.phonewords.dictionary.Node;
import ru.azee.phonewords.dictionary.NumbersMap;

import java.util.*;

import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toMap;
import static ru.azee.phonewords.utils.StringUtils.normalizeNumbers;

/**
 * Created by azee on 24.10.16.
 */
public class DictTraversal {

    /** Head node of the dictionary trie tree */
    protected Node dictionary;

    /** Digits-to-letters map */
    protected final NumbersMap numbersMap = new NumbersMap();

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
        getWords(numb, dictionary, new LinkedList<>(), values);
        return values;
    }

    /**
     * Initial recursive traversal method to fetch possible substitutions
     * @param numb - telephone number
     * @param head - head of dictionary trie tree
     * @param tokens - list of words for current branch
     * @param values - set of all possible values for this number
     */
    private void getWords(String numb, Node head, List<String> tokens, Set<String> values) {
        if (numb.length() == 0){
            return;
        }
        //Iterate through all possible letters for current digit
        for (Character character : numbersMap.getDict().get(numb.charAt(0))){
            getWords(numb, head, head.getChildren().get(character), new LinkedList<>(tokens), values);
        }
    }

    /**
     * Recursive method filling up a list of possible substitutions
     * @param numb - telephone number
     * @param head - head of dictionary trie tree
     * @param child - current node of dictionary trie tree
     * @param tokens - list of words for current branch
     * @param values - set of all possible values for this number
     */
    private void getWords(String numb, Node head, Node child, List<String> tokens, Set<String> values) {
        //If there is a letter available for current digit in a dictionary tree path
        if (child == null){
            //No letter found - try to add a digit to tokens if previous one is not a digit
            if (tokens.size() > 0 && (isNumeric(tokens.get(tokens.size() - 1))) || head != dictionary){
                return;
            }

            //Single digit is added - start looking for a new word path from the top of the dictionary
            tokens.add(String.valueOf(numb.charAt(0)));
            head = dictionary;
        } else {
            //Found a suitable letter - dictionary tree node is head now for current tree path
            head = child;

            //GetMore words if possible even if hit the target word - there may be a longer one
            //down the tree
            getWords(numb.substring(1, numb.length()), head, new LinkedList<>(tokens), values);

            //If the word if found - add it to tokens and start traversal from the top of the tree
            if (child.getWord() != null){
                tokens.add(child.getWord());
                head = dictionary;

                //Hit last character in number sequence - job is done
                if (numb.length() == 1){
                    values.add(tokens.stream().collect(joining("-")));
                    return;
                }
            }
        }
        //Hit last character in number sequence - job is done
        if (numb.length() == 1 && head == dictionary){
            values.add(tokens.stream().collect(joining("-")));
            return;
        }
        getWords(numb.substring(1, numb.length()), head, tokens, values);
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
