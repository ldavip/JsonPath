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
 * String function concat - simple takes a list of arguments and/or an array and concatenates them together to form a
 * single string
 *
 */
public class Concatenate implements PathFunction {
    @Override
    public Object invoke(String currentPath, PathRef parent, Object model, EvaluationContext ctx, List<Parameter> parameters) {
        StringBuilder result = new StringBuilder();
        if(ctx.configuration().jsonProvider().isArray(model)){
            Iterable<?> objects = ctx.configuration().jsonProvider().toIterable(model);
            for (Object obj : objects) {
                if (obj instanceof String) {
                    result.append(obj.toString());
                }
            }
        }
        if (parameters != null) {
            boolean isMap = ctx.configuration().jsonProvider().isMap(model);
            for (Object value : Parameter.toList(Object.class, ctx, parameters)) {
                if (isContextParam(value)) {
                    if (!isMap) {
                        throw new JsonPathException("Concat function attempted to use Context Param with non map object");
                    }
                    Object objValue = ctx.configuration().jsonProvider().getMapValue(model, Utils.normalizeString(value.toString()).substring(2));
                    result.append(objValue);
                } else {
                    result.append(Utils.normalizeString(String.valueOf(value)));
                }
            }
        }
        return result.toString();
    }
}
