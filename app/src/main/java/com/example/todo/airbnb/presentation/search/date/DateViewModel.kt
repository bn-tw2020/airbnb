package com.example.todo.airbnb.presentation.search.date

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.todo.airbnb.data.model.date.DaySelected
import com.example.todo.airbnb.data.model.date.DaySelected.Companion.DaySelectedEmpty
import com.example.todo.airbnb.domain.repository.DateRepository
import com.example.todo.airbnb.presentation.search.date.components.DatesSelectedState
import com.example.todo.airbnb.presentation.search.date.components.DaySelectedStatus
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class DateViewModel @Inject constructor(private val datesRepository: DateRepository) :
    ViewModel() {

    private val calendarYear = datesRepository.getDate()
    private var _dates: MutableState<DatesSelectedState> =
        mutableStateOf(DatesSelectedState(calendarYear))
    val dates: State<DatesSelectedState> = _dates

    fun onDaySelected(daySelected: DaySelected) = daySelected(daySelected)

    fun onClear() = clearDates()

    private fun daySelected(newDate: DaySelected) {
        val from = _dates.value.from
        val to = _dates.value.to

        if (from == DaySelectedEmpty && to == DaySelectedEmpty) {
            setDates(newDate, DaySelectedEmpty)
        } else if (from != DaySelectedEmpty && to != DaySelectedEmpty) {
            clearDates()
            daySelected(newDate = newDate)
        } else if (from == DaySelectedEmpty) {
            if (newDate < to) setDates(newDate, to)
            else if (newDate > to) setDates(to, newDate)
        } else if (to == DaySelectedEmpty) {
            if (newDate < from) setDates(newDate, from)
            else if (newDate > from) setDates(from, newDate)
        }
    }

    private fun setDates(newFrom: DaySelected, newTo: DaySelected) {
        if (newTo == DaySelectedEmpty) {
            newFrom.calendarDay.value.status = DaySelectedStatus.FirstLastDay
            _dates.value = _dates.value.copy(from = newFrom)
        } else {
            _dates.value = _dates.value.copy(from = newFrom.apply {
                calendarDay.value.status = DaySelectedStatus.FirstDay
            })
            selectDatesInBetween(newFrom, newTo)
            _dates.value = _dates.value.copy(to = newTo.apply {
                calendarDay.value.status = DaySelectedStatus.LastDay
            })
        }
    }

    private fun selectDatesInBetween(from: DaySelected, to: DaySelected) {
        val year = _dates.value.year
        if (from.month == to.month) {
            for (i in (from.day + 1) until to.day)
                from.month.getDay(i).status = DaySelectedStatus.Selected
        } else {
            for (i in (from.day + 1) until from.month.numDays + 1) {
                from.month.getDay(i).status = DaySelectedStatus.Selected
            }
            for (i in (from.month.monthNumber + 1) until to.month.monthNumber) {
                val month = year[i - 1]
                for (j in 1 until month.numDays) {
                    month.getDay(j).status = DaySelectedStatus.Selected
                }
                month.getDay(month.numDays).status = DaySelectedStatus.LastDay
            }
            for (i in 1 until to.day) {
                to.month.getDay(i).status = DaySelectedStatus.Selected
            }
        }
    }

    private fun clearDates() {
        val from = _dates.value.from
        val to = _dates.value.to
        val year = _dates.value.year
        if (from != DaySelectedEmpty || to != DaySelectedEmpty) {
            if (from.month == to.month) {
                for (i in from.day..to.day) {
                    from.month.getDay(i).status = DaySelectedStatus.NoSelected
                }
            } else {
                for (i in from.day..from.month.numDays) {
                    from.month.getDay(i).status = DaySelectedStatus.NoSelected
                }
                for (i in (from.month.monthNumber + 1) until to.month.monthNumber) {
                    val month = year[i - 1]
                    for (j in 1..month.numDays) {
                        month.getDay(j).status = DaySelectedStatus.NoSelected
                    }
                }

                for (i in 1..to.day) {
                    to.month.getDay(i).status = DaySelectedStatus.NoSelected
                }
            }
        }
        from.calendarDay.value.status = DaySelectedStatus.NoSelected
        _dates.value = _dates.value.copy(from = DaySelectedEmpty)
        to.calendarDay.value.status = DaySelectedStatus.NoSelected
        _dates.value = dates.value.copy(to = DaySelectedEmpty)
    }
}