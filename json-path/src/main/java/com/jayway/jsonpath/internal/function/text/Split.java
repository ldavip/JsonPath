package com.jayway.jsonpath.internal.function.text;

import com.jayway.jsonpath.JsonPathException;
import com.jayway.jsonpath.internal.EvaluationContext;
import com.jayway.jsonpath.internal.PathRef;
import com.jayway.jsonpath.internal.Utils;
import com.jayway.jsonpath.internal.function.Parameter;
import com.jayway.jsonpath.internal.function.PathFunction;

import java.util.List;

public class Split implements PathFunction {

	@Override
	public Object invoke(String currentPath, PathRef parent, Object model, EvaluationContext ctx,
			List<Parameter> parameters) {

		if (parameters == null || parameters.isEmpty()) {
			throw new JsonPathException("Split function attempted to split without regex pattern");
		}

		Object array = ctx.configuration().jsonProvider().createArray();
		String regex = Utils.normalizeString(parameters.get(0).getJson());

		if (parameters.size() == 2) {
			model = Utils.normalizeString(parameters.get(1).getValue().toString());
		}
		if (model != null) {
			String[] split = Utils.normalizeString(model.toString()).split(regex);
			for (int idx = 0; idx < split.length; idx++) {
				ctx.configuration().jsonProvider().setArrayIndex(array, idx, split[idx]);
			}
		}
		return array;
	}

}
