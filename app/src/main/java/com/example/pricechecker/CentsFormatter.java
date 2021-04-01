package com.example.pricechecker;

import java.text.NumberFormat;
import java.util.Locale;

public class CentsFormatter {

    public static String centsToString(int cents) {
        NumberFormat n = NumberFormat.getCurrencyInstance(Locale.US);
        String s = n.format(cents / 100.0);
        return s;
    }
}
