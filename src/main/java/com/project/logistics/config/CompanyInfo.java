package com.project.logistics.config;

import lombok.Getter;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalUnit;

@Component
@Getter
public class CompanyInfo {

    private static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ISO_DATE;

    private LocalDate currentDate = LocalDate.of(2021, 12, 15);
    private Long companyProfit;

    public Long getCurrentDateAsLong() {
        return currentDate.atStartOfDay().toEpochSecond(ZoneOffset.UTC) * 1000;
    }

    public Long getLocalDateAsStringLong(String dateString) {
        LocalDate localDate = LocalDate.parse(dateString, dateTimeFormatter);
        return localDate.atStartOfDay().toEpochSecond(ZoneOffset.UTC) * 1000;
    }

    public void advanceDate() {
        currentDate = currentDate.plusDays(1);
    }

}