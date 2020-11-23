package com.jayway.jsonpath.internal.function;

import com.jayway.jsonpath.Configuration;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;

import static com.jayway.jsonpath.JsonPath.using;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * Created by mattg on 6/27/15.
 */
public class BaseFunctionTest {
    protected static final String NUMBER_SERIES = "{\"empty\": [], \"numbers\" : [ 1, 2, 3, 4, 5, 6, 7, 8, 9, 10]}";
    protected static final String TEXT_SERIES = "{\"urls\": [\"http://api.worldbank.org/countries/all/?format=json\", \"http://api.worldbank.org/countries/all/?format=json\"], \"text\" : [ \"a\", \"b\", \"c\", \"d\", \"e\", \"f\" ]}";

    /**
     * Verify the function returns the correct result based on the input expectedValue
     *
     * @param pathExpr
     *      The path expression to execute
     *
     * @param json
     *      The json document (actual content) to parse
     *
     * @param expectedValue
     *      The expected value to be returned from the test
     */
    protected void verifyFunction(Configuration conf, String pathExpr, String json, Object expectedValue) {
        Object result = using(conf).parse(json).read(pathExpr);
        assertThat(conf.jsonProvider().unwrap(result)).isEqualTo(expectedValue);
    }

    /**
     * Verify the function returns the correct string result based on the input expectedValue
     *
     * @param pathExpr
     *      The path expression to execute
     *
     * @param json
     *      The json document (actual content) to parse
     *
     * @param expectedValue
     *      The expected string value to be returned from the test
     */
    protected void verifyFunctionAsString(Configuration conf, String pathExpr, String json, String expectedValue) {
        Object result = using(conf).parse(json).read(pathExpr);
        assertThat(String.valueOf(conf.jsonProvider().unwrap(result))).isEqualTo(expectedValue);
    }

    /**
     * Verify the function returns the correct array values based on the input expectedArray
     *
     * @param pathExpr
     *      The path expression to execute
     *
     * @param json
     *      The json document (actual content) to parse
     *
     * @param expectedArray
     *      The expected array to be returned from the test
     */
    protected void verifyFunctionWithArrayResult(Configuration conf, String pathExpr, String json, Object expectedArray) {
        Object result = conf.jsonProvider().unwrap(using(conf).parse(json).read(pathExpr));
        if (conf.jsonProvider().isArray(result) && conf.jsonProvider().isArray(expectedArray)) {
            assertThat(conf.jsonProvider().length(result)).isEqualTo(conf.jsonProvider().length(expectedArray));

            Iterator<?> expectedObjects = conf.jsonProvider().toIterable(expectedArray).iterator();

            Iterable<?> resultObjects = conf.jsonProvider().toIterable(result);
            for (Object resultObj : resultObjects) {
                assertThat(resultObj).isEqualTo(expectedObjects.next());
            }
        } else {
            assertThat(result).isEqualTo(expectedArray);
        }
    }

    protected void verifyMathFunction(Configuration conf, String pathExpr, Object expectedValue) {
        verifyFunction(conf, pathExpr, NUMBER_SERIES, expectedValue);
    }

    protected void verifyTextFunction(Configuration conf, String pathExpr, Object expectedValue) {
        verifyFunction(conf, pathExpr, TEXT_SERIES, expectedValue);
    }

    protected String getResourceAsText(String resourceName) throws IOException {
        return new Scanner(BaseFunctionTest.class.getResourceAsStream(resourceName), "UTF-8").useDelimiter("\\A").next();
    }

    protected Object arrayOf(Configuration conf, List<Object> objects) {
        Object array = conf.jsonProvider().createArray();
        int idx = 0;
        for (Object obj : objects) {
            conf.jsonProvider().setArrayIndex(array, idx++, obj);
        }
        return array;
    }
}
