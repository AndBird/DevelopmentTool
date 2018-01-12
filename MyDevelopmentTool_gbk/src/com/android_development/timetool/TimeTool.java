package com.android_development.timetool;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Administrator on 2016/4/12.
 * ����: ����ʱ�������
 */
public class TimeTool {
    /**
     * Format:  yy-MM-dd HH:mm
     * */
    public static String getFormatTime(long time) {
        SimpleDateFormat format = new SimpleDateFormat("yy-MM-dd HH:mm");
        return format.format(new Date(time));
    }

    /**
     * return HH:mm
     * */
    public static String getHourAndMin(long time) {
        SimpleDateFormat format = new SimpleDateFormat("HH:mm");
        return format.format(new Date(time));
    }

    /**
     * Date
     * */
    public static String getDateTime(long timesamp) {
        String result = "";
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("dd");
            Date today = new Date(System.currentTimeMillis());
            Date otherDay = new Date(timesamp);
            int temp = Integer.parseInt(sdf.format(today))
                    - Integer.parseInt(sdf.format(otherDay));

            switch (temp) {
                case 0:
                    result = "���� " + getHourAndMin(timesamp);
                    break;
                case 1:
                    result = "���� " + getHourAndMin(timesamp);
                    break;
                case 2:
                    result = "ǰ�� " + getHourAndMin(timesamp);
                    break;

                default:
                    // result = temp + "��ǰ ";
                    result = getFormatTime(timesamp);
                    break;
            }
        } catch (Exception e) {
           e.printStackTrace();
        }
        return result;
    }

    /**
     * Date
     * */
    public static String getRaiderTime(long timesamp) {
        String result = "";
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("dd");
            Date today = new Date(System.currentTimeMillis());
            Date otherDay = new Date(timesamp);
            int temp = Integer.parseInt(sdf.format(today))
                    - Integer.parseInt(sdf.format(otherDay));

            switch (temp) {
                case 0:
                    result = "���� ";
                    break;
                case 1:
                    result = "���� ";
                    break;
                case 2:
                    result = "ǰ�� ";
                    break;

                default:
                    // result = temp + "��ǰ ";
                    result = getRaiderDate(timesamp);
                    break;
            }
        } catch (Exception e) {
           e.printStackTrace();
        }
        return result;
    }

    /**
     * Date
     * */
        public static String getRaiderDate(long time) {
        SimpleDateFormat format = new SimpleDateFormat("MM/dd");
        return format.format(new Date(time));
    }

    /**
     * �������ʱ�䣬���в�ͬ�Ļ���
     * @param time ʱ�䣬��msΪ��λ
     * @return ����ʱ��ı�ʾ
     */
    public static String getDifferentTime(long time) {
        SimpleDateFormat format1 = new SimpleDateFormat("MM.dd");
        SimpleDateFormat format2 = new SimpleDateFormat("HH:mm");
        long MIN = 1000 * 60;
        long DAY = MIN * 60 * 24;
        long currentTime = System.currentTimeMillis();
        long today = currentTime / DAY;
        long theTime = time / DAY;
        long days = today - theTime;
        if (days > 7) {
            return format1.format(time);
        } else if (days > 2){
            return "3��ǰ";
        } else if (days == 2) {
            return "ǰ��";
        } else if (days == 1) {
            return "����";
        } else if (days == 0) {
            long min = (currentTime - time) / MIN;
            if (min > 30) {
                return format2.format(time);
            } else if (min > 15) {
                return "15����ǰ";
            } else if (min > 3) {
                return "3����ǰ";
            } else if (min > 1) {
                return "1����ǰ";
            } else {
                return "�ո�";
            }
        } else { //�������ʱ�䲻�ԣ�ֱ����ʾ��������ʱ��
            return format1.format(time);
        }
    }

    /**
     * Date
     * */
    public static String getCustomTime(long timesamp)
    {
        // 2014-01-02 13:05:25
        String time = "";
        try
        {
            Date now = new Date(System.currentTimeMillis());
            Date date = new Date(timesamp);
            long l = now.getTime() - date.getTime();
            long day = l / (24 * 60 * 60 * 1000);
            long hour = (l / (60 * 60 * 1000) - day * 24);
            long min = ((l / (60 * 1000)) - day * 24 * 60 - hour * 60);
            long s = (l / 1000 - day * 24 * 60 * 60 - hour * 60 * 60 - min * 60);
            if(day == 0)
            {
                if(hour == 0)
                {
                    if(min == 0)
                    {
                        time = "�ո�";
                    } else {
                        time = min + "����ǰ";
                    }
                }
                else
                {
                    time = hour + "Сʱǰ";
                }
            }
            else if(day <= 7)
            {
                time = day + "��ǰ";
            }
            else
            {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                time = sdf.format(timesamp);
            }
        } catch (Exception e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return time;
        }
        return time;
    }


    /**
     * Convert time to a string
     * @param millis e.g.time/length from file
     * @return formated string (hh:)mm:ss
     */
    public static String getPlayTime(long millis)
    {
        return TimeTool.millisToString(millis, false);
    }

    /**
     * Convert time to a string
     * @param millis e.g.time/length from file
     * @return formated string "[hh]h[mm]min" / "[mm]min[s]s"
     */
    public static String getPlayTime2(long millis)
    {
        return TimeTool.millisToString(millis, true);
    }

    private static String millisToString(long millis, boolean text) {
        boolean negative = millis < 0;
        millis = java.lang.Math.abs(millis);

        millis /= 1000;
        int sec = (int) (millis % 60);
        millis /= 60;
        int min = (int) (millis % 60);
        millis /= 60;
        int hours = (int) millis;

        String time;
        DecimalFormat format = (DecimalFormat) NumberFormat.getInstance(Locale.CHINA);
        format.applyPattern("00");
        if (text) {
            if (millis > 0)
                time = (negative ? "-" : "") + hours + "h" + format.format(min) + "min";
            else if (min > 0)
                time = (negative ? "-" : "") + min + "min";
            else
                time = (negative ? "-" : "") + sec + "s";
        }
        else {
            if (millis > 0)
                time = (negative ? "-" : "") + hours + ":" + format.format(min) + ":" + format.format(sec);
            else
                time = (negative ? "-" : "") + min + ":" + format.format(sec);
        }
        return time;
    }


    /**
     * ����ʱ��(by mySelf)
     * */
    public static String getHour(int time){
        int hour = time / 1000 * 3600;
        if(hour == 0){
            return "";
        }
        return "" + hour + ":";
    }

    /**
     * ����ʱ��(by mySelf)
     * */
    public static String getMin(int time){
        int hour = time / 1000 * 60;
        if(hour == 0){
            return "";
        }
        return "" + hour + ":";
    }

    /**
     * ����ʱ��(by mySelf)
     * */
    public static String getSecond(int time){
        int hour = time / 1000;
        if(hour == 0){
            return "";
        }
        return "" + hour + ":";
    }

    /**
     * ����ʱ��(by mySelf)
     * */
    public static String getPlayTime3(int time){
        String timeStr = null;
        try {
            int hour = 0;
            int min = 0;
            int second = 0;
            hour = time / (1000 * 3600);
            min = (time % (1000 * 3600)) / (1000 * 60);
            second = (time % (1000 * 60)) / 1000;
            if(hour == 0){
                timeStr = "";
            }else{
                timeStr = "" + (hour < 10 ? "0" : "") + hour + ":";
            }

            if(min == 0){
                if(hour > 0){
                    timeStr = timeStr + "00:";
                }
            }else if(min < 10) {
                timeStr = timeStr + "0" + min + ":";
            }else if(min >= 10){
                timeStr = timeStr + min + ":";
            }

            if(second == 0){
                timeStr = timeStr + "00";
            }else if(second < 10){
                timeStr = timeStr + "0" + second;
            }else{
                timeStr = timeStr + second;
            }
            return timeStr;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * ����ʱ�� ע��Locale.US
     * */
    public static String getPlayTime4(long position) {
        int totalSeconds = (int) (position / 1000);

        int seconds = totalSeconds % 60;
        int minutes = (totalSeconds / 60) % 60;
        int hours = totalSeconds / 3600;

        if (hours > 0) {
            return String.format(Locale.US, "%02d:%02d:%02d", hours, minutes, seconds).toString();
        } else {
            return String.format(Locale.US, "%02d:%02d", minutes, seconds).toString();
        }
    }

}
