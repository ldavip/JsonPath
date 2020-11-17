package com.jayway.jsonpath.internal.function;

import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.Configurations;
import com.jayway.jsonpath.JsonPathException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;

import static com.jayway.jsonpath.JsonPath.using;
import static org.junit.Assert.assertTrue;

/**
 * Created by matt@mjgreenwood.net on 12/10/15.
 */
@RunWith(Parameterized.class)
public class NestedFunctionTest extends BaseFunctionTest {
    private static final Logger logger = LoggerFactory.getLogger(NumericPathFunctionTest.class);

    private Configuration conf = Configurations.GSON_CONFIGURATION;

    public NestedFunctionTest(Configuration conf) {
        logger.debug("Testing with configuration {}", conf.getClass().getName());
        this.conf = conf;
    }

    @Parameterized.Parameters
    public static Iterable<Configuration> configurations() {
        return Configurations.configurations();
    }

    @Test
    public void testParameterAverageFunctionCall() {
        verifyMathFunction(conf, "$.avg($.numbers.min(), $.numbers.max())", 5.5);
    }

    @Test
    public void testArrayAverageFunctionCall() {
        verifyMathFunction(conf, "$.numbers.avg()", 5.5);
    }

    /**
     * This test calculates the following:
     *
     * For each number in $.numbers 1 -> 10 add each number up,
     * then add 1 (min), 10 (max)
     *
     * Alternatively 1+2+3+4+5+6+7+8+9+10+1+10 == 66
     */
    @Test
    public void testArrayAverageFunctionCallWithParameters() {
        verifyMathFunction(conf, "$.numbers.sum($.numbers.min(), $.numbers.max())", 66.0);
    }

    @Test
    public void testJsonInnerArgumentArray() {
        verifyMathFunction(conf, "$.sum(5, 3, $.numbers.max(), 2)", 20.0);
    }

    @Test
    public void testSimpleLiteralArgument() {
        verifyMathFunction(conf, "$.sum(5)", 5.0);
        verifyMathFunction(conf, "$.sum(50)", 50.0);
    }

    @Test
    public void testStringConcat() {
        verifyTextFunction(conf, "$.text.concat()", "abcdef");
    }

    @Test
    public void testStringConcatLength() {
        verifyTextFunction(conf, "$.text.concat().length()", 6);
    }

    @Test
    public void testStringConcatWithJSONParameter() {
        verifyTextFunction(conf, "$.text.concat(\"-\", \"ghijk\")", "abcdef-ghijk");
    }

    @Test
    public void testNestedStringConcatWithJSONParameter() {
        verifyTextFunction(conf, "$.text.concat(\"(\", $.concat(\"ghijk\", \"-\", \"lmnop\", \"[\", $.concat(\"qrs\", \"-\", \"tuv\"), \"]\"), \")\", \"wxyz\")", "abcdef(ghijk-lmnop[qrs-tuv])wxyz");
    }

    @Test
    public void testAppendNumber() {
        verifyMathFunction(conf, "$.numbers.append(11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 0).avg()", 10.0);
    }

    /**
     * Aggregation function should ignore text values
     */
    @Test
    public void testAppendTextAndNumberThenSum() {
        verifyMathFunction(conf, "$.numbers.append(\"0\", \"11\").sum()", 55.0);
    }

    @Test
    public void testErrantCloseBraceNegative() {
        try {
            using(conf).parse(this.NUMBER_SERIES).read("$.numbers.append(0, 1, 2}).avg()");
            assert(false);
        }
        catch (Exception e) {
            assertTrue(e.getMessage().startsWith("Unexpected close brace"));
        }
    }

    @Test
    public void testErrantCloseBracketNegative() {
        try {
            using(conf).parse(this.NUMBER_SERIES).read("$.numbers.append(0, 1, 2]).avg()");
            assert(false);
        }
        catch (Exception e) {
            assertTrue(e.getMessage().startsWith("Unexpected close bracket"));
        }
    }

    @Test
    public void testUnclosedFunctionCallNegative() {
        try {
            using(conf).parse(this.NUMBER_SERIES).read("$.numbers.append(0, 1, 2");
            assert(false);
        }
        catch (Exception e) {
            assertTrue(e.getMessage().startsWith("Arguments to function: 'append'"));
        }
    }

    @Test(expected = JsonPathException.class)
    public void testStringSplitWithoutRegex() {
        verifyFunctionWithArrayResult(conf, "$.text.concat().split()", TEXT_SERIES, null);
    }

    @Test
    public void testStringSplit() {
        Object expected = arrayOf(conf, Arrays.asList("abcdef", "ghijk"));
        verifyFunctionWithArrayResult(conf, "$.concat(\"abcdef\", \" \", \"ghijk\").split(\" \")", TEXT_SERIES, expected);
    }

    @Test
    public void testStringSplitWithRegex() {
        Object expected = arrayOf(conf, Arrays.asList("abc", "def ", " ghi", "jk"));
        verifyFunctionWithArrayResult(conf, "$.concat(\"abc1def\", \" 2 \", \"ghi3jk\").split(\"\\d\")", TEXT_SERIES, expected);
    }

    @Test
    public void testStringSplitWithEmptyString() {
        Object expected = arrayOf(conf, Arrays.asList("a", "b", "c", "d", "e", "f"));
        verifyFunctionWithArrayResult(conf, "$.text.concat().split(\"\")", TEXT_SERIES, expected);
    }

    @Test(expected = JsonPathException.class)
    public void testStringTrimNonString() {
        String json = "{ \"text\": {} }";
        verifyFunction(conf, "$.text.trim()", json, "abc def");
    }

    @Test
    public void testStringTrim() {
        String json = "{ \"text\": \"     abc def    \" }";
        verifyFunction(conf, "$.text.trim()", json, "abc def");
    }

    @Test(expected = JsonPathException.class)
    public void testGetFunctionIndexOutOfBounds() {
        verifyFunction(conf, "$.text.get(6)", TEXT_SERIES, "");
    }

    @Test(expected = JsonPathException.class)
    public void testGetFunctionNoIndex() {
        verifyFunction(conf, "$.text.get()", TEXT_SERIES, "");
    }

    @Test
    public void testGetFunction() {
        verifyFunction(conf, "$.text.get(2)", TEXT_SERIES, "c");
    }

    @Test
    public void testFirstFunction() {
        verifyFunction(conf, "$.text.first()", TEXT_SERIES, "a");
    }

    @Test
    public void testLastFunction() {
        verifyFunction(conf, "$.text.last()", TEXT_SERIES, "f");
    }

    @Test(expected = JsonPathException.class)
    public void testSliceArrayNoIndex() {
        verifyFunction(conf, "$.text.slice()", TEXT_SERIES, "");
    }

    @Test(expected = JsonPathException.class)
    public void testSliceArrayInvalidIndex() {
        verifyFunction(conf, "$.text.slice(3,1)", TEXT_SERIES, "");
    }

    @Test
    public void testSliceArrayFromStartingIndex() {
        Object expected = arrayOf(conf, Arrays.asList("d", "e", "f"));
        verifyFunctionWithArrayResult(conf, "$.text.slice(3)", TEXT_SERIES, expected);
    }

    @Test
    public void testSliceArray() {
        Object expected = arrayOf(conf, Arrays.asList("b", "c", "d"));
        verifyFunctionWithArrayResult(conf, "$.text.slice(1,4)", TEXT_SERIES, expected);
    }

}
