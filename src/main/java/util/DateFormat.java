package util;

import org.apache.commons.lang3.StringUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public enum DateFormat
{
    YYYY_MM_DD_T_HH_MM_SS_SSSZ("yyyy-MM-dd'T'HH:mm:ss.SSSXXX"),  YYYY_MM_DD_HH_MM_SS("yyyy-MM-dd HH:mm:ss"),  YYYYMMDD_HH_MM_SS("yyyyMMdd HH:mm:ss"),  YYYY_MM_DD_HH_MM_SS_2("yyyy:MM:dd HH:mm:ss"),  YYYY_MM_DD_HH_MM("yyyy-MM-dd HH:mm"),  YYYY_MM_DD_HH("yyyy-MM-dd HH"),  YYYY_MM_DD_CN("yyyy��MM��dd��"),  YYYY_MM_DD("yyyy-MM-dd"),  YYYY_MM_DD_2("yyyy/MM/dd"),  YYYYMMDDHHMMSS("yyyyMMddHHmmss"),  YYYYMMDD("yyyyMMdd"),  YYYYMM("yyyyMM");

    private long oneDaySec = 86400000L;
    private String format;

    private DateFormat(String format)
    {
        this.format = format;
    }

    public String getFormat()
    {
        return this.format;
    }

    public void setFormat(String format)
    {
        this.format = format;
    }

    public String format(long timestamp)
    {
        SimpleDateFormat setDayFormat = new SimpleDateFormat();
        String return_date;
        try
        {
            setDayFormat.applyPattern(this.format);
            return_date = setDayFormat.format(new Date(timestamp));
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return null;
        }
        return return_date;
    }

    public String format(int timestamp)
    {
        return format(timestamp * 1000L);
    }

    public String format(String str)
    {
        if (StringUtils.isBlank(str)) {
            return null;
        }
        SimpleDateFormat setDayFormat = new SimpleDateFormat();
        String return_date;
        try
        {
            setDayFormat.applyPattern(getDateFormat(str).getFormat());
            Date date = setDayFormat.parse(str);
            setDayFormat.applyPattern(this.format);
            return_date = setDayFormat.format(date);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return null;
        }
        return return_date;
    }

    public String format(Date date)
    {
        if (date == null) {
            return null;
        }
        SimpleDateFormat setDayFormat = new SimpleDateFormat();
        String return_date;
        try
        {
            setDayFormat.applyPattern(this.format);
            return_date = setDayFormat.format(date);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return null;
        }
        return return_date;
    }

    public Date format2Date(String str)
    {
        SimpleDateFormat setDayFormat = new SimpleDateFormat();
        try
        {
            setDayFormat.applyPattern(this.format);
            String str_date = format(str);
            if (StringUtils.isBlank(str_date)) {
                return null;
            }
            return setDayFormat.parse(str_date);
        }
        catch (ParseException e)
        {
            e.printStackTrace();
        }
        return null;
    }

    public Date format2Date(Date date)
    {
        if (date == null) {
            return null;
        }
        SimpleDateFormat setDayFormat = new SimpleDateFormat();
        try
        {
            setDayFormat.applyPattern(this.format);
            return setDayFormat.parse(setDayFormat.format(date));
        }
        catch (ParseException e)
        {
            e.printStackTrace();
        }
        return null;
    }

    public Date format2Date(long timestamp)
    {
        SimpleDateFormat setDayFormat = new SimpleDateFormat();
        try
        {
            setDayFormat.applyPattern(this.format);
            return setDayFormat.parse(setDayFormat.format(new Date(timestamp)));
        }
        catch (ParseException e)
        {
            e.printStackTrace();
        }
        return null;
    }

    public long format2Long(String str)
    {
        if (StringUtils.isBlank(str)) {
            return 0L;
        }
        Date return_date = format2Date(str);
        if (return_date != null) {
            return return_date.getTime();
        }
        return 0L;
    }

    public SimpleDateFormat getSimpleDateFormat()
    {
        SimpleDateFormat setDayFormat = new SimpleDateFormat();
        setDayFormat.applyPattern(this.format);
        return setDayFormat;
    }

    public String getCurrentLocalDate()
    {
        return format(new Date());
    }

    public int getDifferenceDay(String beginTime, String endTime)
    {
        int difference = 1;
        long begin = format2Date(beginTime).getTime();
        long end = format2Date(endTime).getTime();
        try
        {
            difference = (int)((end - begin) / this.oneDaySec);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            difference = 1;
        }
        return difference;
    }

    public List<String> getBetweenDay(String beginTime, String endTime)
    {
        Set<String> set = new HashSet();
        int difference = getDifferenceDay(beginTime, endTime);
        for (int i = 0; i <= difference; i++) {
            set.add(format(getCustomDate(i, format2Date(beginTime))));
        }
        List<String> list = new ArrayList(set);
        Collections.sort(list);
        return list;
    }

    public List<String> getBetweenDayDesc(String beginTime, String endTime)
    {
        Set<String> set = new HashSet();
        int difference = getDifferenceDay(beginTime, endTime);
        for (int i = difference; i >= 0; i--) {
            set.add(format(getCustomDate(i, format2Date(beginTime))));
        }
        List<String> list = new ArrayList(set);
        Collections.sort(list, Collections.reverseOrder());
        return list;
    }

    public List<String> getBetweenDay(Date beginTime, Date endTime)
    {
        return getBetweenDay(format(beginTime), format(endTime));
    }

    public List<String> getBetweenDayDesc(Date beginTime, Date endTime)
    {
        return getBetweenDayDesc(format(beginTime), format(endTime));
    }

    public Date getCustomDate(int num, Date date)
    {
        Calendar c = new GregorianCalendar();
        c.setTime(format2Date(date));
        long day = c.get(5);

        long unknownDay = day + num;
        c.set(5, (int)unknownDay);
        Date unknownDate = format2Date(c.getTime());
        return unknownDate;
    }

    public String getCustomDateString(int num, Date date)
    {
        String unknownDate = format(getCustomDate(num, date));
        return unknownDate;
    }

    public Date getCustomMonth(int monthNum, Date date)
    {
        int localMonth = getMonth(date);

        int unknownMonth = localMonth + monthNum;

        Date unknownMonthDate = getFirstDayOfMonth(unknownMonth);

        unknownMonthDate = format2Date(unknownMonthDate);
        return unknownMonthDate;
    }

    public Date getCustomWeek(int weekNum, Date date)
    {
        int localWeek = getWeek(date);

        int unknownWeek = localWeek + weekNum;

        Date unknownWeekDate = getFirstDayOfWeek(unknownWeek);

        unknownWeekDate = format2Date(unknownWeekDate);
        return unknownWeekDate;
    }

    public Date getBeginTime(Date date)
    {
        String localDate = YYYY_MM_DD.format(date);
        localDate = localDate + " 00:00:00";
        Date return_date = format2Date(localDate);
        return return_date;
    }

    public Date getEndTime(Date date)
    {
        String localDate = YYYY_MM_DD.format(date);
        localDate = localDate + " 23:59:59";
        date = format2Date(localDate);
        return date;
    }

    public Date getFirstDayOfMonth(int month)
    {
        Calendar c = new GregorianCalendar();
        c.set(2, month - 1);
        c.set(5, 1);
        return format2Date(c.getTime());
    }

    public Date getFirstDayOfWeek(int week)
    {
        Calendar c = new GregorianCalendar();
        c.set(2, 0);
        c.set(5, 1);
        Calendar cal = (GregorianCalendar)c.clone();
        cal.add(5, week * 7);
        return format2Date(getFirstDayOfWeek(cal.getTime()));
    }

    public Date getFirstDayOfWeek(Date date)
    {
        Calendar c = new GregorianCalendar();
        c.setFirstDayOfWeek(2);
        c.setTime(date);
        c.set(7, c.getFirstDayOfWeek());
        return format2Date(c.getTime());
    }

    public int getHour(long dateTime)
    {
        int hour = -1;
        if (dateTime > 0L)
        {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(format2Date(dateTime));
            hour = calendar.get(11);
        }
        return hour;
    }

    public int getHour(String dateTime)
    {
        int hour = -1;
        if (isDate(dateTime)) {
            hour = getHour(format2Long(dateTime));
        }
        return hour;
    }

    public int getHour(Date dateTime)
    {
        return getHour(dateTime.getTime());
    }

    public int getMonth(long dateTime)
    {
        int month = 0;
        if (dateTime > 0L)
        {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(format2Date(dateTime));
            month = calendar.get(2) + 1;
        }
        return month;
    }

    public int getMonth(String dateTime)
    {
        int month = 0;
        if (isDate(dateTime)) {
            month = getMonth(format2Long(dateTime));
        }
        return month;
    }

    public int getMonth(Date dateTime)
    {
        return getMonth(dateTime.getTime());
    }

    public int getYear(long dateTime)
    {
        int year = 0;
        if (year > 0)
        {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(format2Date(dateTime));
            year = calendar.get(1);
        }
        return year;
    }

    public int getYear(String dateTime)
    {
        int year = 0;
        if (isDate(dateTime)) {
            year = getYear(format2Long(dateTime));
        }
        return year;
    }

    public int getYear(Date dateTime)
    {
        return getYear(dateTime.getTime());
    }

    public boolean isDate(String date)
    {
        if (StringUtils.isBlank(date)) {
            return false;
        }
        SimpleDateFormat setDayFormat = new SimpleDateFormat();
        setDayFormat.applyPattern(this.format);
        try
        {
            if (!setDayFormat.format(setDayFormat.parse(date)).equals(date)) {
                return false;
            }
        }
        catch (Exception e)
        {
            return false;
        }
        return true;
    }

    @Override
    public String toString()
    {
        return this.format;
    }

    public static DateFormat getDateFormat(String date)
    {
        if (StringUtils.isBlank(date)) {
            return null;
        }
        SimpleDateFormat setDayFormat = new SimpleDateFormat();
        for (DateFormat dateFormat : values())
        {
            setDayFormat.applyPattern(dateFormat.getFormat());
            setDayFormat.setLenient(false);
            try
            {
                setDayFormat.parse(date.trim());
                return dateFormat;
            }
            catch (Exception e) {}
        }
        return null;
    }

    public static int getWeek(Date date)
    {
        Calendar c = new GregorianCalendar();
        c.setFirstDayOfWeek(2);
        c.setMinimalDaysInFirstWeek(7);
        c.setTime(date);
        return c.get(3);
    }
}

