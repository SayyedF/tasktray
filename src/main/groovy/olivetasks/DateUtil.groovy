package olivetasks

public class DateUtil
{
    public static Date addDays(Date date, int days)
    {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.DATE, days); //minus number would decrement the days
        //cal.setTimeInMillis(43200000)
        return cal.getTime();
    }

    public static Date setTime(Date date, long time)
    {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.setTimeInMillis(time)
        return cal.getTime();
    }
}