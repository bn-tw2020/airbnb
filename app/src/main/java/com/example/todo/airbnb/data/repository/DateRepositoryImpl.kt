package com.example.todo.airbnb.data.repository

import com.example.todo.airbnb.data.model.date.CalendarMonth
import com.example.todo.airbnb.data.model.date.CalendarYear
import com.example.todo.airbnb.data.model.date.DayOfWeek
import com.example.todo.airbnb.data.model.date.MonthOfYear
import com.example.todo.airbnb.domain.repository.DateRepository
import java.time.LocalDate
import java.time.temporal.TemporalAdjusters
import javax.inject.Inject

class DateRepositoryImpl @Inject constructor() : DateRepository {

    override fun getDate(): CalendarYear {
        return MonthOfYear.values().map {
            val date = LocalDate.of(2021, it.order, 1)
            CalendarMonth(
                name = it.name,
                year = date.year.toString(),
                numDays = date.with(TemporalAdjusters.lastDayOfMonth()).dayOfMonth,
                monthNumber = it.order,
                startDayOfWeek = DayOfWeek.values(date.dayOfWeek.toString()) ?: DayOfWeek.Sunday
            )
        }
    }
}