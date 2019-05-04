package com.rcastro.customer.management.customermanagement.utils;

import org.springframework.core.convert.converter.Converter;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;

public class DateToZonedDateTimeConverter implements Converter<Date, ZonedDateTime> {

    public static final DateToZonedDateTimeConverter INSTANCE = new DateToZonedDateTimeConverter();

    private DateToZonedDateTimeConverter() {
    }

    @Override
    public ZonedDateTime convert(Date source) {
        return source == null ? null : ZonedDateTime.ofInstant(source.toInstant(), ZoneId.systemDefault());
    }
}
