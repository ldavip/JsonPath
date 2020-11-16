package com.jayway.jsonpath.internal.function.date;

import com.jayway.jsonpath.JsonPathException;
import com.jayway.jsonpath.internal.EvaluationContext;
import com.jayway.jsonpath.internal.PathRef;
import com.jayway.jsonpath.internal.function.AbstractAggregation;
import com.jayway.jsonpath.internal.function.Parameter;

import java.time.format.DateTimeParseException;
import java.time.temporal.TemporalAccessor;
import java.util.List;

import static com.jayway.jsonpath.internal.function.date.DateTimeFormatterCacheProvider.getFormatterFromCache;

/**
 * Defines the pattern for processing temporal values as string via an abstract implementation that iterates over the collection
 * of JSONArray entities and verifies that each is a temporal value and then passes that along the abstract methods
 *
 *
 * Created by ldavip on 11/11/20.
 */
public abstract class AbstractTemporalAggregation<T extends TemporalAccessor> extends AbstractAggregation<T> {

    protected abstract T parse(String str, String pattern);

    @Override
    public Object invoke(String currentPath, PathRef parent, Object model, EvaluationContext ctx, List<Parameter> parameters) {
        int count = 0;
        if (parameters == null || parameters.isEmpty()) {
            throw new JsonPathException("Temporal Aggregation function attempted to calculate value using empty parameter");
        }

        String pattern = parameters.get(0).getValue().toString().replace("\"", "");

        if (parameters.size() > 1) {
            List<String> objects = Parameter.toList(String.class, ctx, parameters.subList(1, parameters.size()));
            for (String obj : objects) {
                try {
                    next(parse(obj, pattern));
                    count++;
                } catch (DateTimeParseException e) {
                    throw new JsonPathException("Invalid temporal pattern " + pattern + " for input " + obj);
                }
            }
        } else if (ctx.configuration().jsonProvider().isArray(model)) {
            Iterable<?> objects = ctx.configuration().jsonProvider().toIterable(model);
            for (Object obj : objects) {
                if (obj != null) {
                    try {
                        next(parse(obj.toString(), pattern));
                        count++;
                    } catch (DateTimeParseException e) {
                        throw new JsonPathException("Invalid temporal pattern " + pattern + " for input " + obj);
                    }
                }
            }
        }
        if (count != 0) {
            return getFormatterFromCache(pattern).format(getValue());
        }

        throw new JsonPathException("Aggregation function attempted to calculate value using empty array");
    }
}
