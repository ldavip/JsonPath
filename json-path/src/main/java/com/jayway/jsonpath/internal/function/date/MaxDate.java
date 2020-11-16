package com.jayway.jsonpath.internal.function.date;

import java.time.LocalDate;

public class MaxDate extends LocalDateAggregation {

    private LocalDate max = LocalDate.MIN;

    @Override
    protected void next(LocalDate value) {
        if (max.isBefore(value)) {
            max = value;
        }
    }

    @Override
    protected LocalDate getValue() {
        return max;
    }

}
