package com.jayway.jsonpath.internal.function.array;

import com.jayway.jsonpath.JsonPathException;
import com.jayway.jsonpath.internal.EvaluationContext;
import com.jayway.jsonpath.internal.PathRef;
import com.jayway.jsonpath.internal.Utils;
import com.jayway.jsonpath.internal.function.Parameter;
import com.jayway.jsonpath.internal.function.PathFunction;

import java.util.List;

/**
 * Provides new string composed of array elements joined together by delimiter
 * <p>
 * Created by ldavip on 11/23/20.
 */
public class Join implements PathFunction {

    @Override
    public Object invoke(String currentPath, PathRef parent, Object model, EvaluationContext ctx,
                         List<Parameter> parameters) {
        if (parameters == null || parameters.isEmpty()) {
            throw new JsonPathException("Join function attempted to join values without delimiter");
        }

        String delimiter = Utils.normalizeString(parameters.get(0).getValue(model).toString());

        StringBuilder buffer = new StringBuilder();
        int count = 0;

        if (parameters.size() > 1) {
            for (Object obj : Parameter.toList(Object.class, ctx, parameters.subList(1, parameters.size()), model)) {
                if (obj != null) {
                    buffer.append(Utils.normalizeString(obj.toString())).append(delimiter);
                    count++;
                }
            }
        } else if (ctx.configuration().jsonProvider().isArray(model)) {
            for (Object obj : ctx.configuration().jsonProvider().toIterable(model)) {
                if (obj != null) {
                    buffer.append(Utils.normalizeString(obj.toString())).append(delimiter);
                    count++;
                }
            }
        } else if (model instanceof String) {
            buffer.append(Utils.normalizeString(model.toString()));
        }

        if (count > 0) {
            return buffer.substring(0, buffer.length() - delimiter.length());
        }
        return buffer.toString();
    }
}
