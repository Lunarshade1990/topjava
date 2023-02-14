package ru.javawebinar.topjava.util;

import java.time.LocalDate;
import java.time.LocalTime;

public class DateTimeFilter {
    private final LocalTime startTime;
    private final LocalTime endTime;
    private final LocalDate startDate;
    private final LocalDate endDate;

    public DateTimeFilter(LocalDate startDate, LocalDate endDate, LocalTime startTime, LocalTime endTime) {
        this.startTime = startTime;
        this.endTime = endTime;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public LocalTime getStartTime() {
        return startTime;
    }

    public LocalTime getEndTime() {
        return endTime;
    }

    public boolean isEmpty() {
        return startDate == null &&
                endDate == null &&
                startTime == null &&
                endTime == null;
    }
}
