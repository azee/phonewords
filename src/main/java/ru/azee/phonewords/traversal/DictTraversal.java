package ru.azee.phonewords.traversal;

import ru.azee.phonewords.data.DataProviderFactory;
import ru.azee.phonewords.dictionary.DictionaryBuilder;
import ru.azee.phonewords.dictionary.DictionaryProviderFactory;
import ru.azee.phonewords.dictionary.Node;
import ru.azee.phonewords.dictionary.NumbersMap;

import java.util.*;

import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toMap;

/**
 * Created by azee on 24.10.16.
 */
public class DictTraversal {
    private Node dictionary;
    private List<String> numbers;
    private NumbersMap numbersMap;

    public DictTraversal() {
        dictionary = DictionaryBuilder.build(DictionaryProviderFactory.getProvider().provide());
        numbers = DataProviderFactory.getDataProvider().provide();
        numbersMap = new NumbersMap();
    }

    public Map<String, Set<String>> getValues() {
        return numbers.stream().collect(toMap(numb -> numb, numb -> getWords(numb)));

    }

    private Set<String> getWords(String numb) {
        Set<String> values = new HashSet<>();
        getWords(numb, dictionary, new LinkedList<>(), values);
        return values;
    }

    private void getWords(String numb, Node head, List<String> tokens, Set<String> values) {
        //ToDo : do something with 1
        if (numb.length() == 0){
            return;
        }
        for (Character character : numbersMap.getDict().get(numb.charAt(0))){
            getWords(numb, head, head.getChildren().get(character), new LinkedList<>(tokens), values);
        }
    }

    private void getWords(String numb, Node head, Node child, List<String> tokens, Set<String> values) {
        if (child == null){
            if (tokens.size() > 0 && (isNumeric(tokens.get(tokens.size() - 1))) || head != dictionary){
                return;
            }
            tokens.add(String.valueOf(numb.charAt(0)));
            head = dictionary;
        } else {
            head = child;

            //GetMore words if possible even if hit the target word - there may be a longer one
            //down the tree
            getWords(numb.substring(1, numb.length()), head, new LinkedList<>(tokens), values);

            if (child.getWord() != null){
                tokens.add(child.getWord());
                head = dictionary;

                //Hit last character in number sequence
                if (numb.length() == 1){
                    values.add(tokens.stream().collect(joining("-")));
                    return;
                }
            }
        }
        getWords(numb.substring(1, numb.length()), head, tokens, values);
    }

    private boolean isNumeric(String str) {
        try {
            Integer.parseInt(str);
        } catch (Exception e){
            return false;
        }
        return true;
    }

}
