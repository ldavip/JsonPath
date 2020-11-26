package com.jayway.jsonpath.internal.function.array;

import java.util.List;

import com.jayway.jsonpath.internal.EvaluationContext;
import com.jayway.jsonpath.internal.PathRef;
import com.jayway.jsonpath.internal.function.Parameter;
import com.jayway.jsonpath.internal.function.PathFunction;

/**
 * Provides the last value of given array
 * <p>
 * Created by ldavip on 11/16/20.
 */
public class Last implements PathFunction {

    @Override
    public Object invoke(String currentPath, PathRef parent, Object model, EvaluationContext ctx,
                         List<Parameter> parameters) {

        Object result = model;
        if (parameters != null && !parameters.isEmpty()) {
            List<Object> objects = Parameter.toList(Object.class, ctx, parameters, model);
            if (!objects.isEmpty()) {
                return objects.get(objects.size() - 1);
            }
        } else if (ctx.configuration().jsonProvider().isArray(model)) {
            Iterable<?> objects = ctx.configuration().jsonProvider().toIterable(model);
            for (Object obj : objects) {
                result = obj;
            }
        }
        return result;
    }
}
