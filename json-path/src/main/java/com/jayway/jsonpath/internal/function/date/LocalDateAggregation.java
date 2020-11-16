package com.jayway.jsonpath.internal.function.date;

import java.time.LocalDate;

import static com.jayway.jsonpath.internal.function.date.DateTimeFormatterCacheProvider.getFormatterFromCache;

public abstract class LocalDateAggregation extends AbstractTemporalAggregation<LocalDate> {

    @Override
    protected LocalDate parse(String str, String pattern) {
        return LocalDate.parse(str, getFormatterFromCache(pattern));
    }

}
