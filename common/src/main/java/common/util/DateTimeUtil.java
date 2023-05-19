package common.util;

import java.text.SimpleDateFormat;

import java.util.Calendar;
import java.util.Date;


public class DateTimeUtil {
    public static Date addDate(Date date, int num, int addType) throws Exception {
        if (addType != Calendar.DATE && addType != Calendar.MONTH && addType != Calendar.YEAR && addType != Calendar.YEAR)
            throw new Exception("addType khong hop le");
        Calendar cDate = Calendar.getInstance();
        cDate.setTime(date);
        cDate.add(addType, num);
        return cDate.getTime();
    }

    public static long durationBetween(Date startDate, Date endDate, int iType) throws Exception {
        Calendar start = Calendar.getInstance();
        start.setTime(startDate);

        Calendar end = Calendar.getInstance();
        end.setTime(endDate);

        return durationBetween(start, end, iType);
    }

    public static long duration3Months(Date startDate, Date endDate) throws Exception {
        Calendar start = Calendar.getInstance();
        start.setTime(startDate);

        Calendar end = Calendar.getInstance();
        end.setTime(endDate);

        return duration3Months(start, end);
    }

    public static boolean equalsDate(Date source, Date dest) {
        if (source == null)
            return dest == null;
        if (dest == null)
            return false;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        String sSource = sdf.format(source);
        String sDes = sdf.format(dest);
        return sSource.equals(sDes);
    }

    /**
     * So sanh thoi gian, bo qua gio phut giay
     * @param source
     * @param dest
     * @return
     */
    public static int compareDateIgnoreHh(Date source, Date dest) throws Exception {
        if (source == null && dest == null)
            return 0;
        if (source == null)
            throw new Exception("source is null");
        if (dest == null)
            throw new Exception("dest is null");
        Calendar cSource = Calendar.getInstance();
        cSource.setTime(source);

        Calendar cDest = Calendar.getInstance();
        cDest.setTime(dest);

        return compareDateIgnoreHh(cSource, cDest);
    }

    /**
     * So sanh thoi gian, bo qua gio phut giay
     * @param cSource
     * @param cDest
     * @return
     */
    public static int compareDateIgnoreHh(Calendar cSource, Calendar cDest) throws Exception {
        if (cSource == null && cDest == null)
            return 0;
        if (cSource == null)
            throw new Exception("source is null");
        if (cDest == null)
            throw new Exception("dest is null");
        if (cSource.get(Calendar.YEAR) > cDest.get(Calendar.YEAR))
            return 1;
        if (cSource.get(Calendar.YEAR) < cDest.get(Calendar.YEAR))
            return -1;

        if (cSource.get(Calendar.MONTH) > cDest.get(Calendar.MONTH))
            return 1;
        if (cSource.get(Calendar.MONTH) < cDest.get(Calendar.MONTH))
            return -1;

        if (cSource.get(Calendar.DATE) > cDest.get(Calendar.DATE))
            return 1;
        if (cSource.get(Calendar.DATE) < cDest.get(Calendar.DATE))
            return -1;
        return 0;
    }

    public static long durationBetween(Calendar start, Calendar end, int iType) throws Exception {
        if (iType != Calendar.DATE && iType != Calendar.WEEK_OF_YEAR && iType != Calendar.MONTH && iType != Calendar.YEAR)
            throw new Exception("Kieu khong hop le");
        if (end.before(start))
            return durationBetween(end, start, iType);
        long lRs = 0;
        while (start.compareTo(end) <= 0) {
            lRs++;
            start.add(iType, 1);
        }
        // Khong phai ngay, start da lon hon end nhung van cung week, moth
        if (iType != Calendar.DATE && start.compareTo(end) > 0) {
            if (start.get(iType) == end.get(iType))
                lRs++;
        }
        return lRs;
    }

    public static long duration3Months(Calendar start, Calendar end) {
        if (end.before(start))
            return duration3Months(end, start);
        long lRs = 0;
        while (start.compareTo(end) <= 0) {
            lRs++;
            start.add(Calendar.MONTH, 3);
        }
        // start lon hon end nhung van thuoc cung quy
        if (start.compareTo(end) > 0 && (start.get(Calendar.MONTH) / 3 == end.get(Calendar.MONTH) / 3))
            lRs++;
        return lRs;
    }

}

