package com.jayway.jsonpath.internal.function.array;

import com.jayway.jsonpath.JsonPathException;
import com.jayway.jsonpath.internal.EvaluationContext;
import com.jayway.jsonpath.internal.PathRef;
import com.jayway.jsonpath.internal.function.Parameter;
import com.jayway.jsonpath.internal.function.PathFunction;

import java.util.List;

/**
 * Provides the value of specific array index
 * <p>
 * Created by ldavip on 11/16/20.
 */
public class Get implements PathFunction {

    @Override
    public Object invoke(String currentPath, PathRef parent, Object model, EvaluationContext ctx,
                         List<Parameter> parameters) {

        if (parameters == null || parameters.isEmpty()) {
            throw new JsonPathException("Get function attempted to retrieve non specified index");
        }
        String param = parameters.get(0).getValue().toString();
        int index;
        try {
            index = Integer.parseInt(param);
        } catch (Exception e) {
            throw new JsonPathException("Get function attempted to retrieve invalid index");
        }

        if (ctx.configuration().jsonProvider().isArray(model)) {
            Iterable<?> objects = ctx.configuration().jsonProvider().toIterable(model);
            int count = 0;
            for (Object obj : objects) {
                if (count++ == index) {
                    return obj;
                }
            }
        } else {
            throw new JsonPathException("Get function attempted to retrieve an index from non array value");
        }

        throw new JsonPathException("Get function attempted to retrieve an index out of bounds");
    }
}
