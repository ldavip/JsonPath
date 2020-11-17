package com.jayway.jsonpath.internal.function.array;

import com.jayway.jsonpath.JsonPathException;
import com.jayway.jsonpath.internal.EvaluationContext;
import com.jayway.jsonpath.internal.PathRef;
import com.jayway.jsonpath.internal.function.Parameter;
import com.jayway.jsonpath.internal.function.PathFunction;

import java.util.List;

/**
 * Provides new array from inner bounds of an array
 * <p>
 * Created by ldavip on 11/16/20.
 */
public class Slice implements PathFunction {

    @Override
    public Object invoke(String currentPath, PathRef parent, Object model, EvaluationContext ctx,
                         List<Parameter> parameters) {

        if (parameters == null || parameters.isEmpty()) {
            throw new JsonPathException("Slice function attempted to slice an array without bounds");
        }

        int idxInitial;
        int idxFinal = Integer.MAX_VALUE;

        try {
            idxInitial = Integer.parseInt(parameters.get(0).getValue().toString());
        } catch (Exception e) {
            throw new JsonPathException("Slice function starting index invalid");
        }

        if (parameters.size() > 1) {
            try {
                idxFinal = Integer.parseInt(parameters.get(1).getValue().toString());
                if (idxFinal < idxInitial) {
                    throw new JsonPathException("Slice function final index before initial index");
                }
            } catch (Exception e) {
                throw new JsonPathException("Slice function invalid final index");
            }
        }

        if (ctx.configuration().jsonProvider().isArray(model)) {
            Object array = ctx.configuration().jsonProvider().createArray();
            int count = 0;
            Iterable<?> objects = ctx.configuration().jsonProvider().toIterable(model);
            int idx = 0;
            for (Object obj : objects) {
                if (count >= idxInitial && count < idxFinal) {
                    ctx.configuration().jsonProvider().setArrayIndex(array, idx++, obj);
                }
                count++;
            }
            return array;
        } else if (model instanceof String) {
            if (idxFinal > 0) {
                return model.toString().substring(idxInitial, idxFinal);
            }
            return model.toString().substring(idxInitial);
        }

        throw new JsonPathException("Slice function attempted to retrieve sliced array from not array or string");
    }
}
