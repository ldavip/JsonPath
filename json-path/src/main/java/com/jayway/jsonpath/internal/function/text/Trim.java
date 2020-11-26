package com.jayway.jsonpath.internal.function.text;

import com.jayway.jsonpath.internal.EvaluationContext;
import com.jayway.jsonpath.internal.PathRef;
import com.jayway.jsonpath.internal.Utils;
import com.jayway.jsonpath.internal.function.Parameter;
import com.jayway.jsonpath.internal.function.PathFunction;

import java.util.ArrayList;
import java.util.List;

public class Trim implements PathFunction {

    @Override
    public Object invoke(String currentPath, PathRef parent, Object model, EvaluationContext ctx,
                         List<Parameter> parameters) {
        Object result = model;
        if (parameters != null && !parameters.isEmpty()) {
            if (parameters.size() > 1) {
                result = readArray(ctx, Parameter.toList(Object.class, ctx, parameters, model));
            } else {
                Object value = parameters.get(0).getValue(model);
                if (ctx.configuration().jsonProvider().isArray(value)) {
                    List<Object> list = new ArrayList<>();
                    Parameter.consume(Object.class, ctx, list, value);
                    result = readArray(ctx, list);
                } else {
                    result = readObject(value);
                }
            }
        } else if (ctx.configuration().jsonProvider().isArray(model)) {
            result = readArray(ctx, ctx.configuration().jsonProvider().toIterable(model));
        } else if (model instanceof String) {
            result = readObject(model);
        }
        return result;
    }

    private Object readObject(Object object) {
        return Utils.normalizeString(object.toString()).trim();
    }

    private Object readArray(EvaluationContext ctx, Iterable<?> iterable) {
        Object array = ctx.configuration().jsonProvider().createArray();
        int idx = 0;
        for (Object obj : iterable) {
            if (ctx.configuration().jsonProvider().isArray(obj) || ctx.configuration().jsonProvider().isMap(obj)) {
                ctx.configuration().jsonProvider()
                        .setArrayIndex(array, idx++, obj);
            } else {
                ctx.configuration().jsonProvider()
                        .setArrayIndex(array, idx++, readObject(obj.toString()));
            }
        }
        return array;
    }

}
