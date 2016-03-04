package com.cat.morning.goodmorningcat.item;

import android.media.RingtoneManager;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Comparator;

/**
 * Created by imarie on 16/02/27.
 */
public class AlarmItem implements Serializable {


    public enum Day{
        SUNDAY,
        MONDAY,
        TUESDAY,
        WEDNESDAY,
        THURSDAY,
        FRIDAY,
        SATURDAY;

        @Override
        public String toString() {
            switch(this.ordinal()){
                case 0:
                    return "Sunday";
                case 1:
                    return "Monday";
                case 2:
                    return "Tuesday";
                case 3:
                    return "Wednesday";
                case 4:
                    return "Thursday";
                case 5:
                    return "Friday";
                case 6:
                    return "Saturday";
            }
            return super.toString();
        }
    }


//    private static final long serialVersionUID = 8699489847426803789L;
    private int id;
    private Boolean alarmActive = true;
    private Calendar alarmTime = Calendar.getInstance();
    private Day[] days = {Day.MONDAY,Day.TUESDAY,Day.WEDNESDAY,Day.THURSDAY,Day.FRIDAY,Day.SATURDAY,Day.SUNDAY};
    private String alarmTonePath = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM).toString();
    private Boolean vibrate = true;
    private String alarmName = "Alarm Clock";

    public AlarmItem() {

    }

    /**
     * @return the repeatDays
     */
    public Day[] getDays() {
        return days;
    }

    public void setDays(Day[] days) {
        this.days = days;
    }


    public String getRepeatDaysString() {
        StringBuilder daysStringBuilder = new StringBuilder();
        if(getDays().length == Day.values().length){
            daysStringBuilder.append("Every Day");
        }else{
            Arrays.sort(getDays(), new Comparator<Day>() {
                @Override
                public int compare(Day lhs, Day rhs) {

                    return lhs.ordinal() - rhs.ordinal();
                }
            });
            for(Day d : getDays()){
                switch(d){
                    case TUESDAY:
                    case THURSDAY:
//					daysStringBuilder.append(d.toString().substring(0, 4));
//					break;
                    default:
                        daysStringBuilder.append(d.toString().substring(0, 3));
                        break;
                }
                daysStringBuilder.append(',');
            }
            daysStringBuilder.setLength(daysStringBuilder.length()-1);
        }

        return daysStringBuilder.toString();
    }

}
