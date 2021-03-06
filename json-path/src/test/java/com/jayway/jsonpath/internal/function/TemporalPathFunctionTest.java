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

import static org.junit.runners.Parameterized.Parameters;

/**
 * Defines functional tests around executing:
 *
 * - format
 *
 * for each of the above, executes the test and verifies that the results are as expected based on a static input
 * and static output.
 *
 * Created by ldavip on 11/11/20.
 */
@RunWith(Parameterized.class)
public class TemporalPathFunctionTest extends BaseFunctionTest {

    private static final Logger logger = LoggerFactory.getLogger(TemporalPathFunctionTest.class);

    protected static final String DATE_SERIES = "{\"empty\": [], \"timestamp\": \"2020-11-13 07:31:13\", \"time\": \"17:57\", \"dates\" : [ \"07/12/2015\", \"08/23/2005\", \"12/26/2004\", \"03/11/2011\", \"02/05/2008\" ]}";

    private Configuration conf;

    public TemporalPathFunctionTest(Configuration conf) {
        logger.debug("Testing with configuration {}", conf.getClass().getName());
        this.conf = conf;
    }

    private Object arrayOf(Object... objects) {
        return arrayOf(conf, Arrays.asList(objects));
    }

    @Parameters
    public static Iterable<Configuration> configurations() {
        return Configurations.configurations();
    }

    @Test
    public void testFormatSingleDateWithWrongPattern() {
        // if passed wrong pattern keep original value
        verifyFunction(conf, "$.dates[0].format(\"MM-dd-yyyy\", \"yyyy-MM-dd\")", DATE_SERIES, "07/12/2015");
    }

    @Test
    public void testFormatDateArrayWithWrongPattern() {
        // if passed wrong pattern keep original value
        Object expected = arrayOf("07/12/2015", "08/23/2005", "12/26/2004", "03/11/2011", "02/05/2008");
        verifyFunctionWithArrayResult(conf, "$.dates.format(\"MM-dd-yyyy\", \"yyyy-MM-dd\")", DATE_SERIES, expected);
    }

    @Test
    public void testFormatSingleDate() {
        verifyFunction(conf, "$.dates[0].format(\"MM/dd/yyyy\", \"yyyy-MM-dd\")", DATE_SERIES, "2015-07-12");
    }

    @Test
    public void testFormatDateArray() {
        Object expected = arrayOf("2015-07-12", "2005-08-23", "2004-12-26", "2011-03-11", "2008-02-05");
        verifyFunctionWithArrayResult(conf, "$.dates.format(\"MM/dd/yyyy\", \"yyyy-MM-dd\")", DATE_SERIES, expected);
    }

    @Test
    public void testFormatTimestamp() {
        verifyFunction(conf, "$.timestamp.format(\"yyyy-MM-dd HH:mm:ss\", \"MM/dd/yyyy HH:mm\")", DATE_SERIES, "11/13/2020 07:31");
    }

    @Test
    public void testFormatTime() {
        verifyFunction(conf, "$.time.format(\"HH:mm\", \"KK:mm a\")", DATE_SERIES, "05:57 PM");
    }

    @Test
    public void testFormatSingleArg() {
        verifyFunction(conf, "$.format(\"HH:mm\", \"KK:mm a\", $.time)", DATE_SERIES, "05:57 PM");
    }

    @Test
    public void testFormatArgs() {
        Object expected = arrayOf("10:50 AM", "11:50 AM", "01:50 PM");
        verifyFunctionWithArrayResult(conf, "$.format(\"HH:mm\", \"KK:mm a\", \"10:50\", \"11:50\", \"13:50\")", DATE_SERIES, expected);
    }

    @Test
    public void testFormatArrayArg() {
        String json = "{ \"times\": [ \"10:50\", \"11:50\", \"13:50\" ] }";
        Object expected = arrayOf("10:50 AM", "11:50 AM", "01:50 PM");
        verifyFunctionWithArrayResult(conf, "$.format(\"HH:mm\", \"KK:mm a\", $.times)", json, expected);
    }

    @Test
    public void testFormatResultArrayArg() {
        String json = "{ \"objects\": [ { \"time\": \"10:50\" }, { \"time\": \"11:50\" }, { \"time\": \"13:50\" } ] }";
        Object expected = arrayOf("10:50 AM", "11:50 AM", "01:50 PM");
        verifyFunctionWithArrayResult(conf, "$.format(\"HH:mm\", \"KK:mm a\", $.objects[0:3].time)", json, expected);
    }

    @Test
    public void testFormatResultArrayChain() {
        String json = "{ \"objects\": [ { \"time\": \"10:50\" }, { \"time\": \"11:50\" }, { \"time\": \"13:50\" } ] }";
        Object expected = arrayOf("10:50 AM", "11:50 AM", "01:50 PM");
        verifyFunctionWithArrayResult(conf, "$.objects[0:3].time.format(\"HH:mm\", \"KK:mm a\")", json, expected);
    }

    @Test
    public void testFormatDefiniteArg() {
        String json = "{ \"times\": [ \"10:50\", \"11:50\", \"13:50\" ] }";
        verifyFunctionWithArrayResult(conf, "$.format(\"HH:mm\", \"KK:mm a\", $.times[0])", json, "10:50 AM");
    }

    @Test
    public void testFormatIndefiniteArg() {
        String json = "{ \"times\": [ \"10:50\", \"11:50\", \"13:50\" ] }";
        Object expected = arrayOf("10:50 AM");
        verifyFunctionWithArrayResult(conf, "$.format(\"HH:mm\", \"KK:mm a\", $..times[0])", json, expected);
    }

    @Test(expected = JsonPathException.class)
    public void testMinDateEmptyPattern() {
        verifyFunction(conf, "$.dates.minDate()", DATE_SERIES, "12/26/2004");
    }

    @Test(expected = JsonPathException.class)
    public void testMinDateWrongPattern() {
        verifyFunction(conf, "$.dates.minDate(\"dd/MM/yyyy\")", DATE_SERIES, "12/26/2004");
    }

    @Test(expected = JsonPathException.class)
    public void testMinDateEmptyArray() {
        verifyFunction(conf, "$.empty.minDate(\"dd/MM/yyyy\")", DATE_SERIES, "");
    }

    @Test
    public void testMinDate() {
        verifyFunction(conf, "$.dates.minDate(\"MM/dd/yyyy\")", DATE_SERIES, "12/26/2004");
    }

    @Test
    public void testMinDateWithArrayAsParameter() {
        verifyFunction(conf, "$.minDate(\"MM/dd/yyyy\", $.dates)", DATE_SERIES, "12/26/2004");
    }

    @Test(expected = JsonPathException.class)
    public void testMaxDateEmptyPattern() {
        verifyFunction(conf, "$.dates.maxDate()", DATE_SERIES, "07/12/2015");
    }

    @Test(expected = JsonPathException.class)
    public void testMaxDateWrongPattern() {
        verifyFunction(conf, "$.dates.maxDate(\"dd/MM/yyyy\")", DATE_SERIES, "07/12/2015");
    }

    @Test(expected = JsonPathException.class)
    public void testMaxDateEmptyArray() {
        verifyFunction(conf, "$.empty.maxDate(\"dd/MM/yyyy\")", DATE_SERIES, "");
    }

    @Test
    public void testMaxDate() {
        verifyFunction(conf, "$.dates.maxDate(\"MM/dd/yyyy\")", DATE_SERIES, "07/12/2015");
    }

    @Test
    public void testMaxDateWithArrayAsParameter() {
        verifyFunction(conf, "$.maxDate(\"MM/dd/yyyy\", $.dates)", DATE_SERIES, "07/12/2015");
    }

}
