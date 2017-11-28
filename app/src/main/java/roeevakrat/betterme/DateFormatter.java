package roeevakrat.betterme;

import org.joda.time.Days;
import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

/**
 * Created by Administrator on 20/09/2017.
 */

public class DateFormatter {

    private LocalDate date;
    private DateTimeFormatter dateFormatter;
    private String dateStr;

    public DateFormatter(){

        date = LocalDate.now();
        dateFormatter = DateTimeFormat.forPattern("dd.MM.yy");
        dateStr = dateFormatter.print(date);
    }

    public DateFormatter(int interval){

        date = LocalDate.now();
        date = date.plusDays(interval);

        dateFormatter = DateTimeFormat.forPattern("dd.MM.yy");
        dateStr = dateFormatter.print(date);
    }

    public DateFormatter(DateFormatter other){

        date = other.date;
        dateFormatter = other.dateFormatter;
        dateStr = other.dateStr;
    }

    public DateFormatter(String todaysDate){

        dateFormatter = DateTimeFormat.forPattern("dd.MM.yy");
        date = LocalDate.parse(todaysDate, dateFormatter);

        dateStr = dateFormatter.print(date);
    }

    public void updateToTodaysDate(){

        date = LocalDate.now();
        dateStr = dateFormatter.print(date);
    }

    public String getDate(){

        return dateStr;
    }

    public String getDateShort(){

        return dateStr.substring(0, 5);
    }

    public void setDateToDayInCurrentWeek(int dayNum){

        date = date.withDayOfWeek(dayNum);
        dateStr = dateFormatter.print(date);
    }

    public void addDaysToDate(int numOfDays){

        date = date.plusDays(numOfDays);
        dateStr = dateFormatter.print(date);
    }

    public int calculateIntervalBetweenDates(String daysSince){

        LocalDate givenDate = dateFormatter.parseLocalDate(daysSince);

        return Math.abs(Days.daysBetween(date, givenDate).getDays());
    }

    public int calculateIntervalBetweenDates(DateFormatter daysSince) {

        return calculateIntervalBetweenDates(daysSince.dateStr);
    }

    public boolean equals(DateFormatter other){

        return dateStr.equals(other.dateStr);
    }
}
