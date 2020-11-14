package com.jayway.jsonpath.internal.function;

/**
 * Defines the pattern for processing generic values via an abstract implementation that iterates over the collection
 * of JSONArray entities and verifies that each is a generic value and then passes that along the abstract methods
 *
 * @param <T> the type of elements handled
 * 
 *
 * Created by mattg on 6/26/15.
 */
public abstract class AbstractAggregation<T> implements PathFunction {

    /**
     * Defines the next value in the array to the aggregation function
     *
     * @param value
     *      The generic value to process next
     */
    protected abstract void next(T value);

    /**
     * Obtains the value generated via the series of next value calls
     *
     * @return
     *      A generic answer based on the input value provided
     */
    protected abstract T getValue();

}
