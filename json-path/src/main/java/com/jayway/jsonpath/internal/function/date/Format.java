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
import java.util.ArrayList;
import java.util.List;

import static com.jayway.jsonpath.internal.function.date.DateTimeFormatterCacheProvider.getFormatterFromCache;

/**
 * Provides the format function of a temporal
 * <p>
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

        List<Parameter> parametersLeft = parameters.subList(2, parameters.size());

        if (parametersLeft.size() > 1) {
            List<Object> objects = Parameter.toList(Object.class, ctx, parametersLeft);
            result = readArray(ctx, sourceFormatter, targetFormatter, objects);
        } else if (parametersLeft.size() == 1) {
            Object value = parametersLeft.get(0).getValue();
            if (ctx.configuration().jsonProvider().isArray(value)) {
                List<Object> list = new ArrayList<>();
                Parameter.consume(Object.class, ctx, list, value);
                result = readArray(ctx, sourceFormatter, targetFormatter, list);
            } else {
                result = readObject(sourceFormatter, targetFormatter, value);
            }
        } else if (ctx.configuration().jsonProvider().isArray(model)) {
            result = readArray(ctx, sourceFormatter, targetFormatter, ctx.configuration().jsonProvider().toIterable(model));
        } else if (model != null) {
            result = readObject(sourceFormatter, targetFormatter, model);
        }
        return result;
    }

    private Object readObject(DateTimeFormatter sourceFormatter, DateTimeFormatter targetFormatter, Object object) {
        String value = object.toString();
        if (value.startsWith("\"") && value.endsWith("\"")) {
            value = value.substring(1, value.length() - 1);
        }
        try {
            return targetFormatter.format(Utils.parse(sourceFormatter, value));
        } catch (DateTimeParseException e) {
            if (logger.isDebugEnabled()) {
                logger.debug("Unable to parse value {}, error: {}", object, e.getMessage());
            }
        }
        return object;
    }

    private Object readArray(EvaluationContext ctx, DateTimeFormatter sourceFormatter, DateTimeFormatter targetFormatter, Iterable<?> iterable) {
        Object array = ctx.configuration().jsonProvider().createArray();
        int idx = 0;
        for (Object obj : iterable) {
            if (obj != null) {
                String strObj = Utils.normalizeString(obj.toString());
                String value;
                try {
                    value = targetFormatter.format(Utils.parse(sourceFormatter, strObj));
                } catch (DateTimeParseException e) {
                    if (logger.isDebugEnabled()) {
                        logger.debug("Unable to parse value {}, error: {}", strObj, e.getMessage());
                    }
                    value = strObj;
                }
                ctx.configuration().jsonProvider()
                        .setArrayIndex(array, idx++, value);
            }
        }
        return array;
    }
}