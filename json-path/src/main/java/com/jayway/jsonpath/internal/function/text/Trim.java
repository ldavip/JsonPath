package com.jayway.jsonpath.internal.function.text;

import com.jayway.jsonpath.JsonPathException;
import com.jayway.jsonpath.internal.EvaluationContext;
import com.jayway.jsonpath.internal.PathRef;
import com.jayway.jsonpath.internal.Utils;
import com.jayway.jsonpath.internal.function.Parameter;
import com.jayway.jsonpath.internal.function.PathFunction;

import java.util.List;

public class Trim implements PathFunction {

    @Override
    public Object invoke(String currentPath, PathRef parent, Object model, EvaluationContext ctx,
                         List<Parameter> parameters) {

        if (model == null) {
            return null;
        }
        if (!(model instanceof String)) {
            throw new JsonPathException("Trim function attempted to accept non text value");
        }
        return Utils.normalizeString(model.toString()).trim();
    }

}
