package util;

import com.wy.tools.format.DateFormat;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @ClassName
 * @Author XiaoMing
 * @Date
 * @Vsersion V1.0
 */
public class DateUtil {
    public static List<String> querySevenDays(int beginDay, int endDay){
        //最近七天所有日
        String date = DateFormat.YYYY_MM_DD.format(new Date());
        String begin = getDateStr(getDaySub(date,-beginDay)).get("start");
        String end = getDateStr(getDaySub(date,endDay)).get("end");
        List<String> betweenDay = DateFormat.YYYY_MM_DD.getBetweenDay(begin,end);
        return betweenDay;
    }

    public static Map<String,String> getDateStr(String dateStr){
        Map<String,String> map = new HashMap<>();
        map.put("start", dateStr + " 00:00:00");
        map.put("end", dateStr + " 23:59:59");
        return map;
    }

    public static String getDaySub(String dateStr, int num) {
        String day = "";
        try{
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date dt = sdf.parse(dateStr);
            Calendar rightNow = Calendar.getInstance();
            rightNow.setTime(dt);

            rightNow.add(Calendar.DAY_OF_YEAR, num);// 日期加10天
            day = sdf.format(rightNow.getTime());
        }catch(Exception ex){

        }
        return day;
    }

    public static  int[] getHourList() {
        int[] houtsArray = new int[24];
        for (int i = 00; i <= 23; i++) {
            houtsArray[i] = i;
        }
        return houtsArray;
    }

    public static  String getBeforDayTime(int dayCount){
        String time=null;
        try {
            Calendar ca = Calendar.getInstance();
            ca.setTime(new Date());
            ca.add(Calendar.DATE, -dayCount);
            Date lastMonth = ca.getTime();
            time=DateFormat.YYYY_MM_DD.format(lastMonth);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return  time;
    }

    /**
     * 获取当前时间之后 小时的时间
     *
     * @param afterHour
     * @return
     */
    public static Long getAfterHourTime(int afterHour) {
        Long time = null;
        try {
            Calendar ca = Calendar.getInstance();
            ca.setTime(new Date());
            ca.add(Calendar.HOUR, afterHour);
            Date lastMonth = ca.getTime();
            time = DateFormat.YYYY_MM_DD_HH_MM_SS.format2Long(DateFormat.YYYY_MM_DD_HH_MM_SS.format(lastMonth));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return time;
    }

    /**
     * 获取当前小时
     * @param date
     * @return
     */
    public static int getHour(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar.get(Calendar.HOUR_OF_DAY);
    }
}
