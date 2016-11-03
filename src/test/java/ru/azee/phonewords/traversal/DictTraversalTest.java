package ru.azee.phonewords.traversal;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;
import java.util.Set;

import static junit.framework.Assert.assertNotNull;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

/**
 * Created by azee on 26.10.16.
 */
public class DictTraversalTest {

    @Test
    public void positiveExampleTest(){
        Map<String, Set<String>> result =  new DictTraversal(
                Arrays.asList("call", "me", "now")).getValues(Arrays.asList("2255.63"));

        validateNumberResult(result, 1, "2255.63");
        validateInvariants(result, "2255.63", 1, "CALL-ME");
    }

    @Test
    public void multipleInvariantsTest(){
        Map<String, Set<String>> result =  new DictTraversal(
                Arrays.asList("call", "me", "now", "ball")).getValues(Arrays.asList("2255.63"));

        validateNumberResult(result, 1, "2255.63");
        validateInvariants(result, "2255.63", 2, "CALL-ME", "BALL-ME");
    }

    @Test
    public void longInvariantsTest(){
        Map<String, Set<String>> result =  new DictTraversal(
                Arrays.asList("call", "me", "now", "baklof", "callof")).getValues(Arrays.asList("2255.63"));

        validateNumberResult(result, 1, "2255.63");
        validateInvariants(result, "2255.63", 3, "CALL-ME", "BAKLOF", "CALLOF");
    }

    @Test
    public void oneAsNumberAtTheBeginningTest(){
        Map<String, Set<String>> result =  new DictTraversal(
                Arrays.asList("call", "me", "now")).getValues(Arrays.asList("12255.63"));

        validateNumberResult(result, 1, "12255.63");
        validateInvariants(result, "12255.63", 1, "1-CALL-ME");
    }

    @Test
    public void oneAsNumberInTheMiddleTest(){
        Map<String, Set<String>> result =  new DictTraversal(
                Arrays.asList("call", "me", "now")).getValues(Arrays.asList("2255163"));

        validateNumberResult(result, 1, "2255163");
        validateInvariants(result, "2255163", 1, "CALL-1-ME");
    }

    @Test
    public void oneAsNumberAtTheEndTest(){
        Map<String, Set<String>> result =  new DictTraversal(
                Arrays.asList("call", "me", "now")).getValues(Arrays.asList("2255631"));

        validateNumberResult(result, 1, "2255631");
        validateInvariants(result, "2255631", 1, "CALL-ME-1");
    }

    @Test
    public void randomNumberInDifferentPlacesTheEndTest(){
        Map<String, Set<String>> result =  new DictTraversal(
                Arrays.asList("call", "me", "now")).getValues(Arrays.asList("722558639"));

        validateNumberResult(result, 1, "722558639");
        validateInvariants(result, "722558639", 1, "7-CALL-8-ME-9");
    }

    @Test
    public void oneAsNumberInDifferentPlacesTest(){
        Map<String, Set<String>> result =  new DictTraversal(
                Arrays.asList("call", "me", "now")).getValues(Arrays.asList("122551631"));

        validateNumberResult(result, 1, "122551631");
        validateInvariants(result, "122551631", 1, "1-CALL-1-ME-1");
    }

    @Test
    public void consequenceNumbersTest(){
        Map<String, Set<String>> result =  new DictTraversal(
                Arrays.asList("gL", "g", "L")).getValues(Arrays.asList("2345"));

        validateNumberResult(result, 0);
    }

    @Test
    public void multipleNumbersTest(){
        Map<String, Set<String>> result =  new DictTraversal(
                Arrays.asList("ad", "bf", "beg", "ceg")).getValues(Arrays.asList("123", "234"));

        validateNumberResult(result, 2, "123", "234");
        validateInvariants(result, "123", 2, "1-AD", "1-BF");
        validateInvariants(result, "234", 4, "BEG", "CEG", "AD-4", "BF-4");
    }

    @Test
    public void invalidSymbolsInNumbersTest(){
        final String number = "§±!@#$%4^&*()4_-+=7?/3_6-_3*&^><";
        Map<String, Set<String>> result =  new DictTraversal(
                Arrays.asList("h!i r\ne", "±§?.-'/m190-_=}{|:;.><,)(*&^%$#@!e"))
                .getValues(Arrays.asList(number));
        validateNumberResult(result, 1, number);
        validateInvariants(result, number, 1, "HIRE-ME");
    }

    @Test
    public void emptyDictionaryTest(){
        Map<String, Set<String>> result =  new DictTraversal(
                new ArrayList<>()).getValues(Arrays.asList("1", "5", "77"));
        validateNumberResult(result, 2, "1", "5");
        validateInvariants(result, "1", 1, "1");
        validateInvariants(result, "5", 1, "5");

    }

    @Test
    public void emptyPhonesListTest(){
        Map<String, Set<String>> result =  new DictTraversal(
                Arrays.asList("please", "hire", "me")).getValues(new ArrayList<>());
        validateNumberResult(result, 0);
    }

    @Test
    public void zeroNumberTest(){
        Map<String, Set<String>> result =  new DictTraversal(
                Arrays.asList("a")).getValues(Arrays.asList("0", "01", "02"));
        validateNumberResult(result, 2, "0", "02");
        validateInvariants(result, "0", 1, "0");
        validateInvariants(result, "02", 1, "0-A");
    }

    @Test
    public void wordInNumberTest(){
        String number = "a5b6c2d";
        Map<String, Set<String>> result =  new DictTraversal(
                Arrays.asList("job")).getValues(Arrays.asList(number));
        validateNumberResult(result, 1, number);
        validateInvariants(result, number, 1, "JOB");
    }


    private void validateNumberResult(Map<String, Set<String>> result, int size, String... keys){
        assertNotNull(result);
        assertThat(result.size(), is(size));
        assertThat(result.keySet(), containsInAnyOrder(keys));
    }

    private void validateInvariants(Map<String, Set<String>> result, String key, int size, String... values){
        Set<String> gotValues = result.get(key);
        assertNotNull(gotValues);
        assertThat(gotValues.size(), is(size));
        assertThat(gotValues, containsInAnyOrder(values));
    }
}
