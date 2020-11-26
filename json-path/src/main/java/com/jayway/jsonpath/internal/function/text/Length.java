package com.jayway.jsonpath.internal.function.text;

import com.jayway.jsonpath.internal.EvaluationContext;
import com.jayway.jsonpath.internal.PathRef;
import com.jayway.jsonpath.internal.Utils;
import com.jayway.jsonpath.internal.function.Parameter;
import com.jayway.jsonpath.internal.function.PathFunction;

import java.util.List;

/**
 * Provides the length of a JSONArray Object
 * <p>
 * Created by mattg on 6/26/15.
 */
public class Length implements PathFunction {

    @Override
    public Object invoke(String currentPath, PathRef parent, Object model, EvaluationContext ctx, List<Parameter> parameters) {
        if (parameters != null && !parameters.isEmpty()) {
            if (parameters.size() == 1) {
                return readObject(ctx, parameters.get(0).getValue(model));
            }
            Object array = ctx.configuration().jsonProvider().createArray();
            int idx = 0;
            for (Object value : Parameter.toList(Object.class, ctx, parameters, model)) {
                ctx.configuration().jsonProvider()
                        .setArrayIndex(array, idx++, readObject(ctx, value));
            }
            return array;
        }
        return readObject(ctx, model);
    }

    private Object readObject(EvaluationContext ctx, Object object) {
        if (ctx.configuration().jsonProvider().isArray(object)) {
            return ctx.configuration().jsonProvider().length(object);
        }
        if (ctx.configuration().jsonProvider().isMap(object)) {
            return ctx.configuration().jsonProvider().length(object);
        }
        return Utils.normalizeString(object.toString()).length();
    }
}