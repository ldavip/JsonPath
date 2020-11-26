package com.jayway.jsonpath.internal.function.numeric;

import java.text.NumberFormat;
import java.util.List;

import com.jayway.jsonpath.JsonPathException;
import com.jayway.jsonpath.internal.EvaluationContext;
import com.jayway.jsonpath.internal.PathRef;
import com.jayway.jsonpath.internal.function.AbstractAggregation;
import com.jayway.jsonpath.internal.function.Parameter;

/**
 * Defines the pattern for processing numerical values via an abstract implementation that iterates over the collection
 * of JSONArray entities and verifies that each is a numerical value and then passes that along the abstract methods
 *
 *
 * Created by mattg on 6/26/15.
 */
public abstract class AbstractNumberAggregation extends AbstractAggregation<Number> {

    @Override
    public Object invoke(String currentPath, PathRef parent, Object model, EvaluationContext ctx, List<Parameter> parameters) {
        int count = 0;
        if(ctx.configuration().jsonProvider().isArray(model)){

            Iterable<?> objects = ctx.configuration().jsonProvider().toIterable(model);
            for (Object obj : objects) {
                if (obj instanceof Number) {
                    Number value = (Number) obj;
                    count++;
                    next(value);
                }
            }
        }
        if (parameters != null) {
            for (Object value : Parameter.toList(Object.class, ctx, parameters, model)) {
                try {
                    next(NumberFormat.getInstance().parse(value.toString()));
                    count++;
                } catch (Exception ignored) {
                }
            }
        }
        if (count != 0) {
            return getValue();
        }
        throw new JsonPathException("Aggregation function attempted to calculate value using empty array");
    }
}
