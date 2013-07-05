package com.swcm.scanandgo.util;
public class Log {

    static final boolean SHOULD_LOG = true; 
    public final static String LOGTAG = "scanandgo";

    public static void v(String logMe) {
            if (SHOULD_LOG) {
                    android.util.Log.v(LOGTAG, /* SystemClock.uptimeMillis() + " " + */
                    logMe);
            }
    }
    
    public static void d(String logMe) {
            if (SHOULD_LOG) {
                    android.util.Log.d(LOGTAG, logMe);
            }
    }

    public static void i(String logMe) {
            if (SHOULD_LOG) {
                    android.util.Log.i(LOGTAG, logMe);
            }
    }
    public static void w(String logMe) {
            if (SHOULD_LOG) {
                    android.util.Log.w(LOGTAG, logMe);
            }
    }

    public static void e(String logMe) {
            if (SHOULD_LOG) {
                    android.util.Log.e(LOGTAG, logMe);
            }
    }

    public static void e(String logMe, Exception ex) {
            if (SHOULD_LOG) {
                    android.util.Log.e(LOGTAG, logMe, ex);
            }
    }

//  public static String formatTime(long millis) {
//          return new SimpleDateFormat("HH:mm:ss.SSS aaa")
//                          .format(new Date(millis));
//  }
//  
}