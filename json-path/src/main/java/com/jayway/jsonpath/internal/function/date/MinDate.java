package com.jayway.jsonpath.internal.function.date;

import java.time.LocalDate;

public class MinDate extends LocalDateAggregation {

	private LocalDate min = LocalDate.MAX;

	@Override
	protected void next(LocalDate value) {
		if (min.isAfter(value)) {
			min = value;
		}
	}

	@Override
	protected LocalDate getValue() {
		return min;
	}

}
