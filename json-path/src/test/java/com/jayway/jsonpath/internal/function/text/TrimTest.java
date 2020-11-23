package com.jayway.jsonpath.internal.function.text;

import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.internal.ParameterizedTest;
import org.junit.Test;

import java.util.Arrays;

public class TrimTest extends ParameterizedTest {

    public TrimTest(Configuration conf) {
        super(conf);
    }

    @Test
    public void testStringTrimNonString() {
        String json = "{ \"obj\": {} }";
        verifyFunctionAsString(conf, "$.obj.trim()", json, "{}");
    }

    @Test
    public void testStringTrim() {
        String json = "{ \"text\": \"     abc def    \" }";
        verifyFunction(conf, "$.text.trim()", json, "abc def");
    }

    @Test
    public void testStringArrayTrim() {
        Object expected = arrayOf(conf, Arrays.asList("abc def", "ghi  jkl"));
        String json = "{ \"text\": [ \"     abc def    \", \"  ghi  jkl \" ] }";
        verifyFunctionWithArrayResult(conf, "$.text.trim()", json, expected);
    }

    @Test
    public void testTrimArgs() {
        Object expected = arrayOf(conf, Arrays.asList("abc", "def"));
        verifyFunctionWithArrayResult(conf, "$.trim(\"  abc \", \" def   \")", TEXT_SERIES, expected);
    }

    @Test
    public void testTrimArrayArg() {
        Object expected = arrayOf(conf, Arrays.asList("abc def", "ghi  jkl"));
        String json = "{ \"text\": [ \"     abc def    \", \"  ghi  jkl \" ] }";
        verifyFunctionWithArrayResult(conf, "$.trim($.text)", json, expected);
    }
}
