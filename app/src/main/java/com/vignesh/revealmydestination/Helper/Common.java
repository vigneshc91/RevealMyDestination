package com.vignesh.revealmydestination.Helper;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by vignesh on 18/7/17.
 */

public class Common {

    private static SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public static String getFormattedDate(Date date){
        return simpleDateFormat.format(date);
    }
}
