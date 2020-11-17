package com.jayway.jsonpath.internal.function.date;

import com.jayway.jsonpath.JsonPathException;
import com.jayway.jsonpath.internal.EvaluationContext;
import com.jayway.jsonpath.internal.PathRef;
import com.jayway.jsonpath.internal.Utils;
import com.jayway.jsonpath.internal.function.Parameter;
import com.jayway.jsonpath.internal.function.PathFunction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;

import static com.jayway.jsonpath.internal.function.date.DateTimeFormatterCacheProvider.getFormatterFromCache;

/**
 * Provides the format function of a temporal
 *
 * Created by ldavip on 11/11/20.
 */
public class Format implements PathFunction {

	private static final Logger logger = LoggerFactory.getLogger(Format.class);

	@Override
	public Object invoke(String currentPath, PathRef parent, Object model, EvaluationContext ctx,
			List<Parameter> parameters) {

		if (parameters == null || parameters.size() < 2) {
			throw new JsonPathException("Format function should have parameters (sourcePattern, targetPattern)");
		}

		String sourcePattern = parameters.get(0).getValue().toString().replace("\"", "");
		String targetPattern = parameters.get(1).getValue().toString().replace("\"", "");

		DateTimeFormatter sourceFormatter = getFormatterFromCache(sourcePattern);
		DateTimeFormatter targetFormatter = getFormatterFromCache(targetPattern);

		Object result = model;

		if (parameters.size() > 2) {
			List<String> objects = Parameter.toList(String.class, ctx, parameters.subList(2, parameters.size()));
			result = ctx.configuration().jsonProvider().createArray();
			int idx = 0;
			for (String obj : objects) {
				String value;
				try {
					value = targetFormatter.format(Utils.parse(sourceFormatter, obj));
				} catch (DateTimeParseException e) {
					if (logger.isDebugEnabled()) {
						logger.debug("Unable to parse value {}, error: {}", obj, e.getMessage());
					}
					value = obj;
				}
				ctx.configuration().jsonProvider()
						.setArrayIndex(result, idx++, value);
			}
		} else if (ctx.configuration().jsonProvider().isArray(model)) {
			Iterable<?> objects = ctx.configuration().jsonProvider().toIterable(model);
			result = ctx.configuration().jsonProvider().createArray();
			int idx = 0;
			for (Object obj : objects) {
				if (obj != null) {
					String value;
					try {
						value = targetFormatter.format(Utils.parse(sourceFormatter, obj.toString()));
					} catch (DateTimeParseException e) {
						if (logger.isDebugEnabled()) {
							logger.debug("Unable to parse value {}, error: {}", model, e.getMessage());
						}
						value = obj.toString();
					}
					ctx.configuration().jsonProvider()
							.setArrayIndex(result, idx++, value);
				}
			}
		} else if (model != null) {
			String value = model.toString();
			if (value.startsWith("\"") && value.endsWith("\"")) {
				value = value.substring(1, value.length() - 1);
			}
			try {
				return targetFormatter.format(Utils.parse(sourceFormatter, value));
			} catch (DateTimeParseException e) {
				if (logger.isDebugEnabled()) {
					logger.debug("Unable to parse value {}, error: {}", model, e.getMessage());
				}
			}
		}
		return result;
	}
}