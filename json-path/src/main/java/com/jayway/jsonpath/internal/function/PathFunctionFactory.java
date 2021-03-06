package com.jayway.jsonpath.internal.function;

import com.jayway.jsonpath.InvalidPathException;
import com.jayway.jsonpath.internal.function.array.*;
import com.jayway.jsonpath.internal.function.date.Format;
import com.jayway.jsonpath.internal.function.date.MaxDate;
import com.jayway.jsonpath.internal.function.date.MinDate;
import com.jayway.jsonpath.internal.function.json.Append;
import com.jayway.jsonpath.internal.function.numeric.*;
import com.jayway.jsonpath.internal.function.text.Concatenate;
import com.jayway.jsonpath.internal.function.text.Length;
import com.jayway.jsonpath.internal.function.text.Split;
import com.jayway.jsonpath.internal.function.text.Trim;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Implements a factory that given a name of the function will return the Function implementation, or null
 * if the value is not obtained.
 *
 * Leverages the function's name in order to determine which function to execute which is maintained internally
 * here via a static map
 *
 */
public class PathFunctionFactory {

    public static final Map<String, Class> FUNCTIONS;

    static {
        // New functions should be added here and ensure the name is not overridden
        Map<String, Class> map = new HashMap<String, Class>();

        // Math Functions
        map.put("avg", Average.class);
        map.put("stddev", StandardDeviation.class);
        map.put("sum", Sum.class);
        map.put("min", Min.class);
        map.put("max", Max.class);

        // Text Functions
        map.put("concat", Concatenate.class);
        map.put("split", Split.class);
        map.put("trim", Trim.class);

        // Array Functions
        map.put("get", Get.class);
        map.put("first", First.class);
        map.put("last", Last.class);
        map.put("slice", Slice.class);
        map.put("join", Join.class);

        // Temporal Functions
        map.put("format", Format.class);
        map.put("minDate", MinDate.class);
        map.put("maxDate", MaxDate.class);

        // JSON Entity Functions
        map.put("length", Length.class);
        map.put("size", Length.class);
        map.put("append", Append.class);


        FUNCTIONS = Collections.unmodifiableMap(map);
    }

    /**
     * Returns the function by name or throws InvalidPathException if function not found.
     *
     * @see #FUNCTIONS
     * @see PathFunction
     *
     * @param name
     *      The name of the function
     *
     * @return
     *      The implementation of a function
     *
     * @throws InvalidPathException
     */
    public static PathFunction newFunction(String name) throws InvalidPathException {
        Class functionClazz = FUNCTIONS.get(name);
        if(functionClazz == null){
            throw new InvalidPathException("Function with name: " + name + " does not exist.");
        } else {
            try {
                return (PathFunction)functionClazz.newInstance();
            } catch (Exception e) {
                throw new InvalidPathException("Function of name: " + name + " cannot be created", e);
            }
        }
    }
}
