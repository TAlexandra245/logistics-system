package com.project.logistics.config;

import lombok.Getter;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.ZoneOffset;

@Component
@Getter
public class CompanyInfo {

    private LocalDate currentDate = LocalDate.of(2021, 12, 21);
    private Long companyProfit;

    public Long getCurrentDateAsLong() {
        return currentDate.atStartOfDay().toEpochSecond(ZoneOffset.UTC) * 1000;
    }
}
