package com.jayway.jsonpath.internal.function.array;

import com.jayway.jsonpath.internal.EvaluationContext;
import com.jayway.jsonpath.internal.PathRef;
import com.jayway.jsonpath.internal.function.Parameter;
import com.jayway.jsonpath.internal.function.PathFunction;

import java.util.Iterator;
import java.util.List;

/**
 * Provides the first value of given array
 * <p>
 * Created by ldavip on 11/16/20.
 */
public class First implements PathFunction {

    @Override
    public Object invoke(String currentPath, PathRef parent, Object model, EvaluationContext ctx,
                         List<Parameter> parameters) {

        if (parameters != null && !parameters.isEmpty()) {
            List<Object> objects = Parameter.toList(Object.class, ctx, parameters);
            return objects.get(0);
        } else if (ctx.configuration().jsonProvider().isArray(model)) {
            Iterable<?> objects = ctx.configuration().jsonProvider().toIterable(model);
            Iterator<?> iterator = objects.iterator();
            if (iterator.hasNext()) {
                return iterator.next();
            }
        }

        return model;
    }
}
