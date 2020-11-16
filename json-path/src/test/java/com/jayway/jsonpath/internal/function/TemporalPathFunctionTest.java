package com.jayway.jsonpath.internal.function;

import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.Configurations;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
        Object array = conf.jsonProvider().createArray();
        int idx = 0;
        for (Object obj : objects) {
            conf.jsonProvider().setArrayIndex(array, idx++, obj);
        }
        return array;
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

}
