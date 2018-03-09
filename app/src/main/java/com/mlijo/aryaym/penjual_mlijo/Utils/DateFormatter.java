package com.mlijo.aryaym.penjual_mlijo.Utils;

import java.text.SimpleDateFormat;

/**
 * Created by buivu on 21/03/2017.
 */

public class DateFormatter {
    public static String formatDate(long miliseconds) {
        return new SimpleDateFormat("MM, yyyy").format(miliseconds);
    }

    public static String formatDateByYMD(long miliseconds) {
        return new SimpleDateFormat("dd/ MM/ yyyy").format(miliseconds);
    }
}
