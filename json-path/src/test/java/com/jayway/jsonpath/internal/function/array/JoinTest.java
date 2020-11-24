package com.jayway.jsonpath.internal.function.array;

import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.JsonPathException;
import com.jayway.jsonpath.internal.ParameterizedTest;
import org.junit.Test;

public class JoinTest extends ParameterizedTest {

    public JoinTest(Configuration conf) {
        super(conf);
    }

    @Test(expected = JsonPathException.class)
    public void testJoinWithoutDelimiter() {
        String json = "{ \"obj\": {} }";
        verifyFunction(conf, "$.obj.join()", json, "{}");
    }

    @Test
    public void testSingleStringJoin() {
        String json = "{ \"text\": \"abc def\" }";
        verifyFunction(conf, "$.text.join(\",\")", json, "abc def");
    }

    @Test
    public void testStringArrayJoin() {
        Object expected = "abc, def, ghi, jkl";
        String json = "{ \"text\": [ \"abc\", \"def\", \"ghi\", \"jkl\" ] }";
        verifyFunction(conf, "$.text.join(\", \")", json, expected);
    }

    @Test
    public void testJoinArgs() {
        verifyFunction(conf, "$.join(\",\", \"abc\", \"def\")", "{}", "abc,def");
    }

    @Test
    public void testJoinArrayArg() {
        String json = "{ \"text\": [ \"abc\", \"def\", \"ghi\", \"jkl\" ] }";
        verifyFunction(conf, "$.join(\"-\", $.text)", json, "abc-def-ghi-jkl");
    }

    @Test
    public void testJoinWithDoubleQuoteDelimiter() {
        String json = "{ \"text\": [ \"abc\", \"def\", \"ghi\", \"jkl\" ] }";
        // $.join("\"", $.text)
        verifyFunction(conf, "$.join(\"\\\"\", $.text)", json, "abc\"def\"ghi\"jkl");
    }

    @Test
    public void testJoinWithParamContext() {
        String json = "{ \"obj\": { \"str1\": \"abc\", \"str2\": \"def\" } }";
        verifyFunction(conf, "$.obj.join(\"-\", ?.str1, ?.str2)", json, "abc-def");
    }
}
