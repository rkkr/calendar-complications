package rkr.calendar.complications;

import android.support.wearable.complications.ComplicationData;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.Set;

public class CalendarProvider {
    public static final String WEEK = "week";
    public static final String YEAR = "year";
    public static final String MONTH_NUMBER = "month_number";
    public static final String MONTH_TEXT = "month_text";
    public static final String DAY = "day";

    public static final String[] TYPES = {WEEK, YEAR, MONTH_NUMBER, MONTH_TEXT, DAY};

    private static final String DATE_SEPARATOR = "-";

    public static List<String> GetRows(Set<String> selection, int complicationType) {
        List<String> temp = new ArrayList<>();
        Calendar calendar = Calendar.getInstance();

        switch (complicationType) {
            case ComplicationData.TYPE_SHORT_TEXT:
                break;

            case ComplicationData.TYPE_LONG_TEXT:
                break;
        }

        for (String type : TYPES)
            if (selection.contains(type)) {
                String line = "";
                switch (type) {
                    case WEEK:
                        String weekday = WeekdayToString(calendar.get(Calendar.DAY_OF_WEEK));
                        line = complicationType == ComplicationData.TYPE_SHORT_TEXT ? weekday.substring(0, 3) : weekday;
                        break;
                    case YEAR:
                        line = String.format(Locale.US, "%d", calendar.get(Calendar.YEAR));
                        if (complicationType == ComplicationData.TYPE_LONG_TEXT) {
                            if (selection.contains(MONTH_NUMBER) && selection.contains(DAY)) {
                                line += DATE_SEPARATOR + String.format(Locale.US, "%02d", calendar.get(Calendar.MONTH) + 1);
                                line += DATE_SEPARATOR + String.format(Locale.US, "%02d", calendar.get(Calendar.DAY_OF_MONTH));
                                selection.remove(DAY);
                                selection.remove(MONTH_NUMBER);
                            }
                            if (selection.contains(MONTH_TEXT) && selection.contains(DAY)) {
                                line += DATE_SEPARATOR + MonthToString(calendar.get(Calendar.MONTH)).substring(0, 3);
                                line += DATE_SEPARATOR + String.format(Locale.US, "%02d", calendar.get(Calendar.DAY_OF_MONTH));
                                selection.remove(DAY);
                                selection.remove(MONTH_TEXT);
                            }
                        }
                        break;
                    case MONTH_NUMBER:
                        line = String.format(Locale.US, "%02d", calendar.get(Calendar.MONTH) + 1);
                        if (selection.contains(DAY) && selection.size() > 2) {
                            line += DATE_SEPARATOR + String.format(Locale.US, "%02d", calendar.get(Calendar.DAY_OF_MONTH));
                            selection.remove(DAY);
                        }
                        break;
                    case MONTH_TEXT:
                        line = MonthToString(calendar.get(Calendar.MONTH));
                        line = complicationType == ComplicationData.TYPE_SHORT_TEXT ? line.substring(0, 3) : line;
                        if (selection.contains(DAY) && selection.size() > 2) {
                            line += DATE_SEPARATOR + String.format(Locale.US, "%02d", calendar.get(Calendar.DAY_OF_MONTH));
                            selection.remove(DAY);
                        }
                        break;
                    case DAY:
                        line = String.format(Locale.US, "%02d", calendar.get(Calendar.DAY_OF_MONTH));
                        break;
                }
                temp.add(line);
            }

        return temp;
    }

    private static String WeekdayToString(int weekday)
    {
        switch (weekday) {
            case Calendar.MONDAY:
                return "MONDAY";
            case Calendar.TUESDAY:
                return "TUESDAY";
            case Calendar.WEDNESDAY:
                return "WEDNESDAY";
            case Calendar.THURSDAY:
                return "THURSDAY";
            case Calendar.FRIDAY:
                return "FRIDAY";
            case Calendar.SATURDAY:
                return "SATURDAY";
            case Calendar.SUNDAY:
                return "SUNDAY";
        }
        return "";
    }

    private static String MonthToString(int month)
    {
        switch (month) {
            case Calendar.JANUARY:
                return "JANUARY";
            case Calendar.FEBRUARY:
                return "FEBRUARY";
            case Calendar.MARCH:
                return "MARCH";
            case Calendar.APRIL:
                return "APRIL";
            case Calendar.MAY:
                return "MAY";
            case Calendar.JUNE:
                return "JUNE";
            case Calendar.JULY:
                return "JULY";
            case Calendar.AUGUST:
                return "AUGUST";
            case Calendar.SEPTEMBER:
                return "SEPTEMBER";
            case Calendar.OCTOBER:
                return "OCTOBER";
            case Calendar.NOVEMBER:
                return "NOVEMBER";
            case Calendar.DECEMBER:
                return "DECEMBER";
        }
        return "";
    }
}
