package ru.azee.phonewords.dictionary;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;

import static org.hamcrest.collection.IsIterableContainingInAnyOrder.containsInAnyOrder;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;

/**
 * Created by azee on 25.10.16.
 */
public class DictionaryBuilderTest {

    @Test
    public void buildTrieTreeTest(){
        Node head = DictionaryBuilder.build(
                Arrays.asList("fat", "not", "fit")
        );

        assertNotNull(head);
        assertNotNull(head.getWords());
        assertThat(head.getWords().size(), is(0));
        Map<Character, Node> children = head.getChildren();
        assertThat(children.size(), is(2));
        assertThat(children.keySet(), containsInAnyOrder('3', '6'));

        Node child = children.get('3');
        assertNotNull(child);
        assertNotNull(child.getWords());
        assertThat(child.getWords().size(), is(0));
        children = child.getChildren();
        assertThat(children.size(), is(2));
        assertThat(children.keySet(), containsInAnyOrder('2', '4'));

        child = children.get('4');
        assertNotNull(child);
        assertNotNull(child.getWords());
        assertThat(head.getWords().size(), is(0));
        children = child.getChildren();
        assertThat(children.size(), is(1));
        assertThat(children.keySet(), containsInAnyOrder('8'));

        child = children.get('8');
        assertNotNull(child);
        assertThat(child.getWords(), containsInAnyOrder("FIT"));
        assertThat(child.getChildren().size(), is(0));
    }

    @Test
    public void buildEmptyTree(){
        Node head = DictionaryBuilder.build(new ArrayList<>());
        assertNotNull(head);
        assertThat(head.getWords().size(),is(0));
        assertThat(head.getChildren().size(), is(0));
    }

    @Test
    public void buildTrieTreeWithDuplicate(){
        Node head = DictionaryBuilder.build(
                Arrays.asList("fin", "final", "fin")
        );

        assertNotNull(head);
        assertThat(head.getWords().size(), is(0));
        assertThat(head.getChildren().size(), is(1));
        assertThat(head.getChildren().keySet(), containsInAnyOrder('3'));

        head = head.getChildren().get('3');
        assertNotNull(head);
        assertThat(head.getWords().size(), is(0));
        assertThat(head.getChildren().size(), is(1));
        assertThat(head.getChildren().keySet(), containsInAnyOrder('4'));

        head = head.getChildren().get('4');
        assertNotNull(head);
        assertThat(head.getWords().size(), is(0));
        assertThat(head.getChildren().size(), is(1));
        assertThat(head.getChildren().keySet(), containsInAnyOrder('6'));

        head = head.getChildren().get('6');
        assertNotNull(head);
        assertThat(head.getWords(), containsInAnyOrder("FIN"));
        assertThat(head.getChildren().size(), is(1));
        assertThat(head.getChildren().keySet(), containsInAnyOrder('2'));

        head = head.getChildren().get('2');
        assertNotNull(head);
        assertThat(head.getWords().size(), is(0));
        assertThat(head.getChildren().size(), is(1));
        assertThat(head.getChildren().keySet(), containsInAnyOrder('5'));

        head = head.getChildren().get('5');
        assertNotNull(head);
        assertThat(head.getWords(), containsInAnyOrder("FINAL"));
        assertThat(head.getChildren().size(), is(0));
    }

    @Test
    public void buildTrieTreeMultipelWords(){
        Node head = DictionaryBuilder.build(
                Arrays.asList("bar", "bas")
        );

        assertNotNull(head);
        assertThat(head.getWords().size(), is(0));
        assertThat(head.getChildren().size(), is(1));
        assertThat(head.getChildren().keySet(), containsInAnyOrder('2'));

        head = head.getChildren().get('2');
        assertNotNull(head);
        assertThat(head.getWords().size(), is(0));
        assertThat(head.getChildren().size(), is(1));
        assertThat(head.getChildren().keySet(), containsInAnyOrder('2'));

        head = head.getChildren().get('2');
        assertNotNull(head);
        assertThat(head.getWords().size(), is(0));
        assertThat(head.getChildren().size(), is(1));
        assertThat(head.getChildren().keySet(), containsInAnyOrder('7'));

        head = head.getChildren().get('7');
        assertNotNull(head);
        assertThat(head.getWords(), containsInAnyOrder("BAR", "BAS"));
        assertThat(head.getChildren().size(), is(0));
    }

}
