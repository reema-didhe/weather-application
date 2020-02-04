package com.example.utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DateTime {

    public static String dateFormat(){
        String inputDate = "09/01/2014";
        String inputFormat = "MM/dd/yyyy";
        String outputFormat = "d ' ' MMMM ' ' yyyy";

        Date parsed = null;
        String outputDate = "";
        try {
            SimpleDateFormat df_input = new SimpleDateFormat(inputFormat, new Locale("en", "US"));
            SimpleDateFormat df_output = new SimpleDateFormat(outputFormat, new Locale("en", "US"));
            parsed = df_input.parse(inputDate);
            outputDate = df_output.format(parsed);
        } catch (Exception e) {
            outputDate = inputDate;
        }
        return outputDate;
    }

}
