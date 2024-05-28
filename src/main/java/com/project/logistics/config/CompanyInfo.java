package com.project.logistics.config;

import lombok.Getter;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.atomic.AtomicLong;

@Component
@Getter
public class CompanyInfo {

    private static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ISO_DATE;

    private LocalDate currentDate = LocalDate.of(2021, 12, 15);
    private AtomicLong companyProfit = new AtomicLong(0L);

    public Long getCurrentDateAsLong() {
        return currentDate.atStartOfDay().toEpochSecond(ZoneOffset.UTC) * 1000;
    }

    public Long getLocalDateAsStringLong(String dateString) {
        LocalDate localDate = LocalDate.parse(dateString, dateTimeFormatter);
        return localDate.atStartOfDay().toEpochSecond(ZoneOffset.UTC) * 1000;
    }

    public LocalDate advanceDate() {
        currentDate = currentDate.plusDays(1);
        return currentDate;
    }

    public Long calculateCompanyProfit(Long profit) {
        return companyProfit.addAndGet(profit);
    }
}
