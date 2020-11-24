package com.jayway.jsonpath.internal.function.text;

import com.jayway.jsonpath.JsonPathException;
import com.jayway.jsonpath.internal.EvaluationContext;
import com.jayway.jsonpath.internal.PathRef;
import com.jayway.jsonpath.internal.Utils;
import com.jayway.jsonpath.internal.function.Parameter;
import com.jayway.jsonpath.internal.function.PathFunction;

import java.util.List;

import static com.jayway.jsonpath.internal.Utils.isContextParam;

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
                return readObject(ctx, parameters.get(0).getValue());
            }
            boolean isMap = ctx.configuration().jsonProvider().isMap(model);
            Object array = ctx.configuration().jsonProvider().createArray();
            int idx = 0;
            for (Parameter obj : parameters) {
                Object value = obj.getValue();
                if (isContextParam(value)) {
                    if (!isMap) {
                        throw new JsonPathException("Length function attempted to use Context Param with non map object");
                    }
                    value = ctx.configuration().jsonProvider().getMapValue(model, Utils.normalizeString(value.toString()).substring(2));
                }
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