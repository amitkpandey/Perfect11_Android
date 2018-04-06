package com.utility;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class DateUtility {

    public static String convertDataToGivitDateFormatForMyAccount(String string_date) {
//        DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        DateFormat formatter = new SimpleDateFormat("dd-MM-yyyy", Locale.US);
        DateFormat outputFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        Date date;
        try {
            formatter.setTimeZone(TimeZone.getDefault());
            date = formatter.parse(string_date);
            return outputFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return "";
    }

    public static String convertDataToServerDateFormat2(String string_date) {
        DateFormat formatter = new SimpleDateFormat("MM-dd-yyyy", Locale.US);
        DateFormat outputFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.US);
        Date date;
        try {
            formatter.setTimeZone(TimeZone.getDefault());
            date = formatter.parse(string_date);
            return outputFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return "";
    }

    public static String convertDataToServerDateFormat3(String string_date) {
        DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        DateFormat outputFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.US);
        Date date;
        try {
            formatter.setTimeZone(TimeZone.getDefault());
            date = formatter.parse(string_date);
            return outputFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return "";
    }

    public static boolean isGreaterThanDate(String d1, String d2) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
            Date date1 = sdf.parse(d1);
            Date date2 = sdf.parse(d2);
            System.out.println("date1.after(date2) " + date1.after(date2));
            return date1.after(date2);
        } catch (ParseException ex) {
            ex.printStackTrace();
        }
        return false;
    }

    public static boolean isEqualToDate(String d1, String d2) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("MM-dd-yyyy", Locale.US);
            Date date1 = sdf.parse(d1);
            Date date2 = sdf.parse(d2);
            return !date1.equals(date2);
        } catch (ParseException ex) {
            ex.printStackTrace();
        }
        return false;
    }

    public static boolean compareDates(String d1, String d2) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("MM-dd-yyyy", Locale.US);
            Date date1 = sdf.parse(d1);
            Date date2 = sdf.parse(d2);

            System.out.println("Date1" + sdf.format(date1));
            System.out.println("Date2" + sdf.format(date2));

            // Date object is having 3 methods namely after, before and equals for comparing
            // after() will return true if and only if date1 is after date 2
            if (date1.after(date2)) {
                System.out.println("Date1 is after Date2");
                return false;
            } else if (date1.before(date2)) {
                // before() will return true if and only if date1 is before date2
                System.out.println("Date1 is before Date2");
                return true;
            } else if (date1.equals(date2)) {
                //equals() returns true if both the dates are equal
                System.out.println("Date1 is equal Date2");
                return false;
            } else
                return false;
        } catch (ParseException ex) {
            ex.printStackTrace();
        }
        return false;
    }

    public static boolean dobDateValidate(String date) {
        boolean result = false;
        SimpleDateFormat sdf = new SimpleDateFormat("MM-dd-yyyy", Locale.ENGLISH);
        try {
            Date parseDate = sdf.parse(date);
            Calendar c2 = Calendar.getInstance();
            c2.add(Calendar.YEAR, -18);
            if (parseDate.before(c2.getTime())) {
                result = true;
            }

        } catch (ParseException e) {
            e.printStackTrace();
        }
        return result;
    }

    public static String getDayOfWeekFromDate(String string_date) {
        String day = "";
        Calendar c = Calendar.getInstance();

        DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US);
        // DateFormat formatter = new SimpleDateFormat("MMMM dd, yyyy", Locale.US);
        DateFormat outputFormat = new SimpleDateFormat("MMMM dd, yyyy", Locale.US);
        Date date;
        try {
            formatter.setTimeZone(TimeZone.getDefault());
            date = formatter.parse(string_date);
            c.setTime(date);
            int dayOfWeek = c.get(Calendar.DAY_OF_WEEK);
            if (dayOfWeek == 1)
                day = "Sunday";
            else if (dayOfWeek == 2)
                day = "Monday";
            else if (dayOfWeek == 3)
                day = "Tuesday";
            else if (dayOfWeek == 4)
                day = "Wednesday";
            else if (dayOfWeek == 5)
                day = "Thursday";
            else if (dayOfWeek == 6)
                day = "Friday";
            else if (dayOfWeek == 7)
                day = "Saturday";
            return day + ", " + outputFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return "";
    }

    public static String convertDateToSocialNetworkingFormat(String datePosted) {
        if (datePosted == null || datePosted.length() == 0)
            return "";
        DateFormat formatter_GMT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US);
        DateFormat formatter_DEFAULT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US);
        formatter_DEFAULT.setTimeZone(TimeZone.getDefault());
        formatter_GMT.setTimeZone(TimeZone.getTimeZone("GMT"));
        StringBuilder sBuffer = new StringBuilder("");
        Date d1, d2;

        try {
            d1 = formatter_GMT.parse(datePosted);
            d1 = formatter_DEFAULT.parse(formatter_DEFAULT.format(d1));

            Calendar c = Calendar.getInstance(TimeZone.getDefault(), Locale.getDefault());
            c.setTime(d1);

            Calendar c1 = Calendar.getInstance(TimeZone.getDefault(), Locale.getDefault());
            d2 = formatter_DEFAULT.parse(getLocalDateTime());
            c1.setTime(d2);
            // in milliseconds
            long diff = c1.getTimeInMillis() - c.getTimeInMillis();
            long diffSeconds = diff / 1000 % 60;
            long diffMinutes = diff / (60 * 1000) % 60;
            long diffHours = diff / (60 * 60 * 1000) % 24;
            long diffDays = diff / (24 * 60 * 60 * 1000);

            if (diffDays > 0)
                sBuffer.append(diffDays).append(" days");
            else if (diffHours > 0 && diffDays <= 0)
                sBuffer.append(diffHours).append(" hours");
            else if (diffMinutes > 0 && diffHours <= 0)
                sBuffer.append(diffMinutes).append(" minutes");
            else if (diffSeconds >= 0 && diffMinutes <= 0)
                sBuffer.append(diffSeconds).append(" secs");

            return sBuffer.toString().length() == 0 ? "0 secs" : sBuffer.toString() + " ago";
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    public static String convertDataToDateMonthFormat(String string_date) {
        DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US);
        DateFormat outputFormat = new SimpleDateFormat("dd MMMM, yyyy", Locale.US);
        Date date;
        try {
            formatter.setTimeZone(TimeZone.getDefault());
            date = formatter.parse(string_date);
            return outputFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return "";
    }

    public static String convertDataToYearMonthDateMonthFormat(String string_date) {
        DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US);
        DateFormat outputFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        Date date;
        try {
            formatter.setTimeZone(TimeZone.getDefault());
            date = formatter.parse(string_date);
            return outputFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return "";
    }

    public static String convertDataFromHourFormat(String string_date) {
        DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        DateFormat outputFormat = new SimpleDateFormat("dd MMMM, yyyy", Locale.US);
        Date date;
        try {
            formatter.setTimeZone(TimeZone.getDefault());
            date = formatter.parse(string_date);
            return outputFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return "";
    }


    public static String convertDataToGivitDateFormat(String string_date) {
        DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        DateFormat outputFormat = new SimpleDateFormat("MMMM dd, yyyy", Locale.US);
        Date date;
        try {
            formatter.setTimeZone(TimeZone.getDefault());
            date = formatter.parse(string_date);
            return outputFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return "";
    }

    public static String convertDataToServerDateFormat(String string_date) {
        DateFormat formatter = new SimpleDateFormat("MM-dd-yyyy", Locale.US);
        DateFormat outputFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        Date date;
        try {
            formatter.setTimeZone(TimeZone.getTimeZone("GMT"));
            date = formatter.parse(string_date);
            return outputFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return "";
    }

    public static boolean isCurrentDate(String date) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        Date strDate = null;
        try {
            strDate = formatter.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        Calendar c = Calendar.getInstance();
        System.out.println("Current time => " + c.getTime());

        Date current_date = null;
        String formattedDate = formatter.format(c.getTime());
        try {
            current_date = formatter.parse(formattedDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        if (current_date.compareTo(strDate) <= 0) {
            return true;
        } else {
            return false;
        }

    }

    public static String convertDateTimeFormat(String string_date) {
        DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH);
        DateFormat outputFormat = new SimpleDateFormat("dd MMM, yyyy 'at' hh:mm a", Locale.ENGLISH);
        Date date;
        try {
            formatter.setTimeZone(TimeZone.getDefault());
            date = formatter.parse(string_date);
            return outputFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return "";
    }

    public static String getLocalDateTime() {
        Calendar cal = Calendar.getInstance(TimeZone.getDefault(), Locale.getDefault());
        Date currentLocalTime = cal.getTime();
        DateFormat date = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
        date.setTimeZone(TimeZone.getDefault());
        return date.format(currentLocalTime);
    }

    public static String getCurrentDateTimeInGMT() {
        String localDateTime = "";
        try {
            localDateTime = getLocalDateTime();
            DateFormat date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH);
            DateFormat outputdate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH);

            date.setTimeZone(TimeZone.getDefault());
            outputdate.setTimeZone(TimeZone.getTimeZone("GMT"));

            return outputdate.format(date.parse(localDateTime));
        } catch (Exception e) {
            e.printStackTrace();
            return localDateTime;
        }
    }

}
